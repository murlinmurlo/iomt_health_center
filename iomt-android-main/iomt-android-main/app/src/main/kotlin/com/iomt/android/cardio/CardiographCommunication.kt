package com.iomt.android.cardio


import android.content.Context
import android.util.Log
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime.Companion
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.util.Arrays
import java.lang.Thread.sleep



class CardiographCommunication(context: Context, fileHandler: FileHandler) {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private val TAG = "WatcherThread"
    var context = context
    val fileHandler = fileHandler

    private val fileName = "ecg.txt"
    private val timestamps = "timestamps.txt"
    private val export = "export.txt"
    private val ecgChannel_1 = "channel_1.txt"
    private val ecgChannel_2 = "channel_2.txt"
    private val timestampEcg = "timestamp_ecg.txt"

    lateinit var currentTimestamp: kotlinx.datetime.LocalDateTime
    val time_mas = mutableListOf<String>()


    fun setStreams(inp: InputStream?, outp: OutputStream?) {
        inputStream = inp
        outputStream = outp
    }


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
            else -> {
                Log.d(TAG, "Monitoring is off")
                return ByteArray(0) // Added return here to handle the 'else' case properly
            }
        }

        try {
            var availableBytes: Int
            while (inputStream?.available().also { availableBytes = it!! } == 0) {
                if (Thread.currentThread().isInterrupted) {
                    // Поток был прерван, возвращаем пустой массив
                    return ByteArray(0)
                }
                //Log.d(TAG, "Доступных байтов нет")
                Thread.sleep(10)
            }

            val buffer = ByteArray(availableBytes)
            Log.d(TAG, "Buffer = $buffer")
            val bytesRead = inputStream?.read(buffer)

            Log.d(TAG, "Buffer = $buffer")
            val hexString = buffer.joinToString(separator = " ") { String.format("%02X", it) }
            Log.d(TAG, "Buffer = $hexString")

            if (bytesRead == -1) {
                // Обработка случая, когда в потоке больше нет данных
                return ByteArray(0)
            }


            for (i in 1..count) {
                val currentTimestamp = kotlinx.datetime.LocalDateTime.now()
                time_mas.add(currentTimestamp.toString())
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
//            fileHandler.writeFile("", timestamps, false)
//            fileHandler.writeFile("", export, false)
            val s1 = fileHandler.readFile("ecg.txt")
            fileHandler.writeFile("", "ecg.txt", false)
            val s2 = fileHandler.readFile("ecg.txt")
        Log.d("STATE1", s1)
        Log.d("STATE2", s2)}



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


                while (isMonitoring) { ///НЕ ЗАБЫТЬ ПОМЕНЯТЬ
                    Log.d(TAG, "МОНИТОРИНГ")
                    val response = reciveEcgData(frequency)
                    val hexResponce = response.joinToString(separator = " ") { String.format("%02X", it) }
                    val (decodedData, bytesProcessed) = RFC1055.decode(response.toList(), response.size)
                    Log.d(TAG, Arrays.toString(decodedData))

                    val hexString = decodedData?.joinToString(separator = " ") { String.format("%02X", it) }
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
