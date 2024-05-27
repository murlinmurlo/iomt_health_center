package com.iomt.android.cardio


import android.content.ContentValues
import android.content.Context
import android.util.Log
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.io.IOException
import java.security.Timestamp

class FileHandler(context: Context, fileToHandl: String, ecgChannel_1: String, ecgChannel_2: String) {
    val fileName = fileToHandl
    val ecgChannel_1 = ecgChannel_1
    val ecgChannel_2 = ecgChannel_2
    val context = context

    lateinit var currentTimestamp: kotlinx.datetime.LocalDateTime


    //запись в файл
    fun writeFile(text: String, fileNameDefault: String = fileName, appendDefault: Boolean = true) {
        try {
            val fileOutputStream: FileOutputStream = when (appendDefault) {
                true -> context.openFileOutput(fileNameDefault, Context.MODE_APPEND)
                false -> context.openFileOutput(fileNameDefault, Context.MODE_PRIVATE)
            }

            fileOutputStream.write(text.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //чтение из файла
    fun readFile(fileNameDefault: String = fileName): String {
        var fileInputStream: FileInputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        val stringBuilder = StringBuilder()
        try {
            fileInputStream = context.openFileInput(fileNameDefault)
            inputStreamReader = InputStreamReader(fileInputStream)
            bufferedReader = BufferedReader(inputStreamReader)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
            inputStreamReader?.close()
            bufferedReader?.close()
        }
        return stringBuilder.toString()
    }


    //прочитать и очистить
    fun readAndClearFile(fileNameDefault: String = fileName): String {
        var fileInputStream: FileInputStream? = null
        var inputStreamReader: InputStreamReader? = null
        var bufferedReader: BufferedReader? = null
        val stringBuilder = StringBuilder()
        try {
            fileInputStream = context.openFileInput(fileNameDefault)
            inputStreamReader = InputStreamReader(fileInputStream)
            bufferedReader = BufferedReader(inputStreamReader)
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            // Очистить содержимое файла
            val file = File(context.filesDir, fileName)
            file.writeText("") // Записать пустую строку, чтобы очистить файл
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
            inputStreamReader?.close()
            bufferedReader?.close()
        }
        return stringBuilder.toString()
    }


    //для графика
    fun getInt24_Diskrete(byteArray: ByteArray): List<Float> {
        Log.d("GET_INT", byteArray.size.toString())
        if (byteArray.size < 3) {
            throw IllegalArgumentException("Byte array must be at least 3 bytes long")
        }

        val DISCRETE: Float = 4f / 8388607 / 8

        return (0 until byteArray.size - (byteArray.size % 3) step 3)
            .map { index ->
                val int24 = ((byteArray[index].toInt() and 0xFF) shl 16) or
                        ((byteArray[index + 1].toInt() and 0xFF) shl 8) or
                        (byteArray[index + 2].toInt() and 0xFF)

                if (int24 and 0x800000 != 0) {
                    (int24 or 0xFF000000.toInt()) * DISCRETE
                } else {
                    int24 * DISCRETE
                }
            }
    }

    //для сервера
    fun getInt24(byteArray: ByteArray): List<Int> {
        Log.d("GET_INT", byteArray.size.toString())
        if (byteArray.size < 3) {
            throw IllegalArgumentException("Byte array must be at least 3 bytes long")
        }

        return (0 until byteArray.size - (byteArray.size % 3) step 3)
            .map { index ->
                val int24 = ((byteArray[index].toInt() and 0xFF) shl 16) or
                        ((byteArray[index + 1].toInt() and 0xFF) shl 8) or
                        (byteArray[index + 2].toInt() and 0xFF)

                if (int24 and 0x800000 != 0) {
                    (int24 or 0xFF000000.toInt())
                } else {
                    int24
                }
            }
    }




    fun kotlinx.datetime.LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())


    //для сервера
    fun readPartial(file: String, frequency: Int): Pair<List<Int>, List<Int>> {
        //var ecg_string = ""
        var ecg_mas_1 = ByteArrayOutputStream()
        var ecg_mas_2 = ByteArrayOutputStream()

        var count = 0
        when (frequency) {
            0x01 -> count = 25
            0x02 -> count = 50
            0x03 -> count = 100
        }
        var fileInputStream: FileInputStream? = null

        try {
            fileInputStream = context.openFileInput(file)
            val inputString = fileInputStream.bufferedReader().use { it.readText() }
            val outputArray = inputString.split(" ").toTypedArray()
            var index = 0
            while (index < outputArray.size) {
                index += 8
                repeat(count) {
                    repeat(3) {
                        ecg_mas_1.write(outputArray[index].toByteArray())
                        index++
                    }
                    repeat(3) {
                        ecg_mas_2.write(outputArray[index].toByteArray())
                        index++
                    }
                }
                index += 47
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }

        Log.d(ContentValues.TAG, "ecg_channel_1.size: ${ecg_mas_1.size()}, ecg_channel_2.size: ${ecg_mas_2.size()}")
        val ecg_mas_1_24 = getInt24(ecg_mas_1.toByteArray())
        val ecg_mas_2_24 = getInt24(ecg_mas_2.toByteArray())

        return Pair(ecg_mas_1_24, ecg_mas_2_24)

    }


    //для графика
    fun readPartial_Float(file: String, frequency: Int): Pair<List<Float>, List<Float>> {
        var ecg_mas_1 = ByteArrayOutputStream()
        var ecg_mas_2 = ByteArrayOutputStream()

        var count = 0
        when (frequency) {
            0x01 -> count = 25
            0x02 -> count = 50
            0x03 -> count = 100
        }
        var fileInputStream: FileInputStream? = null

        try {
            fileInputStream = context.openFileInput(file)
            val inputString = fileInputStream.bufferedReader().use { it.readText() }
            val outputArray = inputString.split(" ").toTypedArray()

            // Пропускаем первые 8 чисел
            var index = 0

            while (index < outputArray.size) {
                index += 8
                repeat(count) {
                    repeat(3) {
                        ecg_mas_1.write(outputArray[index].toByteArray())
                        index++
                    }
                    repeat(3) {
                        ecg_mas_2.write(outputArray[index].toByteArray())
                        index++
                    }
                }
                index += 47
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }

        Log.d(ContentValues.TAG, "ecg_channel_1.size: ${ecg_mas_1.size()}, ecg_channel_2.size: ${ecg_mas_2.size()}")
        val ecg_mas_1_24 = getInt24_Diskrete(ecg_mas_1.toByteArray())
        val ecg_mas_2_24 = getInt24_Diskrete(ecg_mas_2.toByteArray())

        return Pair(ecg_mas_1_24, ecg_mas_2_24)
    }


}
