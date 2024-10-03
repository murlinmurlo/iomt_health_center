package com.my.test


import android.content.Context
import android.util.Log
import android.view.ActionMode
import android.view.View
import com.my.readdeviceinformation.RFC1055
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime.Companion
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.Arrays
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.lang.Thread.sleep
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime


class CardiogaphCommunication(context: Context, fileHandler: FileHandler, timestamps: String, export: String) {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val TAG = "WatcherThread"
    var context = context
    val fileHandler = fileHandler
    val timestamps = timestamps
    val export = export

    lateinit var currentTimestamp: kotlinx.datetime.LocalDateTime
    //var time_str = ""
    val time_mas = mutableListOf<String>()


    fun setStreams(inp: InputStream?, outp: OutputStream?) {
        inputStream = inp
        outputStream = outp
    }

//    //функция для получения ответа в виде байтового массива
//    private fun reciveData(): ByteArray {
//        try {
//            var availableBytes: Int
//            while (inputStream?.available().also { availableBytes = it!! } == 0) {
//                Thread.sleep(10)
//            }
//            val buffer = ByteArray(availableBytes)
//            val bytesRead = inputStream?.read(buffer)
//            Log.d(TAG, "Buffer ="+Arrays.toString(buffer));
//            if (bytesRead == -1) {
//                // Обработка случая, когда в потоке больше нет данных
//                return ByteArray(0)
//            }
//            return buffer.copyOf(bytesRead ?: 0)
//        } catch (e: IOException) {
//            // Обработка ошибки ввода-вывода
//            e.printStackTrace()
//            return ByteArray(0)
//        }
//    }



    private fun reciveData(): ByteArray {
        try {
            var availableBytes: Int
            while (inputStream?.available().also { availableBytes = it!! } == 0) {
                if (Thread.currentThread().isInterrupted) {
                    // Поток был прерван, возвращаем пустой массив
                    return ByteArray(0)
                }
                //Log.d(TAG, "Доступных  байтов нет")
                sleep(10)
            }

            val buffer = ByteArray(availableBytes)
            Log.d(TAG, "Buffer =$buffer")
            val bytesRead = inputStream?.read(buffer)

            Log.d(TAG, "Buffer =$buffer")
            val hexString = buffer.joinToString(separator = " ") { String.format("%02X", it) }
            Log.d(TAG, "Buffer = $hexString")

            if (bytesRead == -1) {
                // Обработка случая, когда в потоке больше нет данных
                return ByteArray(0)
            }
            val result = buffer.copyOf(bytesRead ?: 0)
            buffer.fill(0)

            return result
        } catch (e: IOException) {
            e.printStackTrace()
            return ByteArray(0)
        } catch (e: InterruptedException) {
            // Поток был прерван, возвращаем пустой массив
            return ByteArray(0)
        }
    }


   fun Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())


    private fun reciveEcgData(frequency: Int): ByteArray {

        if (frequency == 0) return ByteArray(0)

        val count = when (frequency) {
            1 -> 25
            2 -> 50
            3 -> 100
            else -> {Log.d(TAG, "Monitoring is off")}
        }


        try {
            var availableBytes: Int
            while (inputStream?.available().also { availableBytes = it!! } == 0) {
                if (Thread.currentThread().isInterrupted) {
                    // Поток был прерван, возвращаем пустой массив
                    return ByteArray(0)
                }
                //Log.d(TAG, "Доступных байтов нет")
                sleep(10)
            }

            val buffer = ByteArray(availableBytes)
            Log.d(TAG, "Buffer =$buffer")
            val bytesRead = inputStream?.read(buffer)

            Log.d(TAG, "Buffer =$buffer")
            val hexString = buffer.joinToString(separator = " ") { String.format("%02X", it) }
            Log.d(TAG, "Buffer = $hexString")

//            fileHandler.writeFile(hexString, export, true)
//            fileHandler.writeFile("\n", export, true)

            if (bytesRead == -1) {
                // Обработка случая, когда в потоке больше нет данных
                return ByteArray(0)
            }



//            fileHandler.writeFile("$currentTimestamp", timestamps, true)
//            fileHandler.writeFile("\n", timestamps, true)

            currentTimestamp = kotlinx.datetime.LocalDateTime.now()
            for (i in 1..count) {
                //time_str += "$currentTimestamp" + "\n"
                time_mas.add(currentTimestamp.toString())

//                //fileHandler.writeFile("Отсчёт:\n", "timestamps.txt", true)
//                fileHandler.writeFile("$currentTimestamp" + " ", timestamps, true)
//                fileHandler.writeFile("\n", timestamps, true)
            }


            val result = buffer.copyOf(bytesRead ?: 0)
            buffer.fill(0)

            return result
        } catch (e: IOException) {
            e.printStackTrace()
            return ByteArray(0)
        } catch (e: InterruptedException) {
            // Поток был прерван, возвращаем пустой массив
            return ByteArray(0)
        }
    }


    fun GetTimeArray(): MutableList<String> {
        return time_mas
    }


    fun ReadDeviceInformation(context: Context): ByteArray? {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x00.toByte(),
            //0x02.toByte(),
            0x02.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response = reciveData()
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
        return decodedData

        /*
        val thread = Thread {
            val response = reciveData()
            val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
            Log.d("tag2", decodedData.toString())
            Log.d("tag2", response.toString())
        }
        thread.start()*/
    }

    fun ReadDeviceStatus(): ByteArray? {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x01.toByte(),
            0x03.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response: ByteArray = reciveData()
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
        Log.d("tag2", response.toString())
        return decodedData
    }

    fun ReadComponentStatus(): ByteArray?{
        val command = listOf<Byte>(
            0x00.toByte(),
            0x02.toByte(),
            0x04.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)
        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val response: ByteArray = reciveData()
        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
        Log.d("tag2", response.toString())
        return decodedData
    }

    fun PowerOff() {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x0A.toByte(),
            0x0C.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())
    }

    fun MonitoringModeCRC(frequency: Int): Byte {
        var crc: Byte = 0
        when (frequency) {
            0 -> crc = 0x08.toByte()
            1 -> crc = 0x09.toByte()
            2 -> crc = 0x10.toByte()
            3 -> crc = 0x0a.toByte()
        }
        return crc
    }


    private var monitoringThread: Thread? = null
    private var isMonitoring = false

    fun MonitoringMode(frequency: Int, callback: (ByteArray) -> Unit) {

        if (frequency != 0) {
            fileHandler.writeFile("", timestamps, false)
            fileHandler.writeFile("", export, false)
            fileHandler.writeFile("", "ecg.txt", false)}

        if (!isMonitoring) {
            isMonitoring = true
            monitoringThread = Thread {
                var crc = MonitoringModeCRC(frequency)
                val command = listOf<Byte>(
                    0x00.toByte(),
                    0x04.toByte(),
                    frequency.toByte(),
                    crc,
                )
                val encodedCommand = RFC1055.encode(command)

                var buf = ByteBuffer.allocate(encodedCommand.size)
                buf.put(encodedCommand)
                buf.flip()
                outputStream?.write(buf.array())
                Log.d("tag2", "Послала Monitoring Mode с частотой ${frequency}")

                val count = when (frequency) {
                    1 -> 25
                    2 -> 50
                    3 -> 100
                    else -> {Log.d(TAG, "Monitoring is off")}
                }

                while (isMonitoring) { ///НЕ ЗАБЫТЬ ПОМЕНЯТЬ
                    Log.d(TAG, "МОНИТОРИНГ")
                    val response = reciveEcgData(frequency)
//                    for (i in 1..1) {
//                        currentTimestamp = kotlinx.datetime.LocalDateTime.now()
//                        time_str += "$currentTimestamp" + "\n"
//                    }
                    val hexResponce = response.joinToString(separator = " ") { String.format("%02X", it) }
                    //fileHandler.writeFile(" \n ПАКЕТ \n")
                    //fileHandler.writeFile(hexResponce)



                    val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

                    Log.d(TAG, Arrays.toString(decodedData))

                    val hexString = decodedData?.joinToString(separator = " ") { String.format("%02X", it) }
                    //Log.d(TAG, "ДЕКОДИРОВАННОЕ: $hexString")

                    if (decodedData != null) {
                        if (hexString != null) {
                            fileHandler.writeFile(hexString + " ")

                        }
                    }

                    callback(decodedData ?: ByteArray(0))

                    buf.clear()
                }
            }
            monitoringThread?.start()
        } else {
            isMonitoring = false
            monitoringThread?.interrupt()
            monitoringThread = null
        }
    }


    fun MonitoringStop() {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x04.toByte(),
            0x00.toByte(),
            0x08.toByte()
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())
    }


    fun FlashRead() {
        val command = listOf<Byte>(
            0x00.toByte(),
            0x03.toByte(),
            0x01.toByte(),
            0x07.toByte(),
        )
        val encodedCommand = RFC1055.encode(command)

        val buf = ByteBuffer.allocate(encodedCommand.size)
        buf.put(encodedCommand)
        buf.flip()
        outputStream?.write(buf.array())

        val buffer = ByteArray(1204)
        val bytesRead = inputStream?.read(buffer)
        val hexString = buffer.joinToString(separator = " ") { String.format("%02X", it) }
        Log.d(TAG, "Buffer3 = $hexString")

//        val response: ByteArray = reciveData()
//        val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
//        Log.d("tag2", response.toString())
//        return decodedData
    }


    fun ImpedanseMode(mode: Int, callback: (ByteArray) -> Unit) {

        if (!isMonitoring) {
            isMonitoring = true
            monitoringThread = Thread {
                val command = listOf<Byte>(
                    0x00.toByte(),
                    0x03.toByte(),
                    mode.toByte(),
                    0x07.toByte(),
                )
                val encodedCommand = RFC1055.encode(command)

                var buf = ByteBuffer.allocate(encodedCommand.size)
                buf.put(encodedCommand)
                buf.flip()
                outputStream?.write(buf.array())



                while (isMonitoring) { ///НЕ ЗАБЫТЬ ПОМЕНЯТЬ

                    Log.d(TAG, "ИМПЕДАНС")
                    val response = reciveEcgData(mode)

                    val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)

                    Log.d(TAG, Arrays.toString(decodedData))

                    val hexString = decodedData?.joinToString(separator = " ") { String.format("%02X", it) }
                    //Log.d(TAG, "ДЕКОДИРОВАННОЕ: $hexString")

                    if (decodedData != null) {
                        if (hexString != null) {
                            fileHandler.writeFile(hexString + " ")

                        }
                    }

                    callback(decodedData ?: ByteArray(0))

                    buf.clear()
                }
            }
            monitoringThread?.start()
        } else {
            isMonitoring = false
            monitoringThread?.interrupt()
            monitoringThread = null
        }
    }
}
