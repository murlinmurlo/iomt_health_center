package com.my.test

import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.IOException
import java.sql.Timestamp
import java.util.Date

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import java.io.ByteArrayOutputStream


class FileHandler(context: Context, fileToHandl: String, ecgChannel_1: String, ecgChannel_2: String, timestampEcg: String) {
    val fileName = fileToHandl
    val ecgChannel_1 = ecgChannel_1
    val ecgChannel_2 = ecgChannel_2
    val context = context

    val timestampEcg = timestampEcg
    lateinit var currentTimestamp: kotlinx.datetime.LocalDateTime //Date(System.currentTimeMillis())


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

    /*
    fun getInt24(byteArray: ByteArray): Int {
        if (byteArray.size < 3) {
            throw IllegalArgumentException("Byte array must be at least 3 bytes long")
        }

        val int24 = ((byteArray[0].toInt() and 0xFF) shl 16) or
                ((byteArray[1].toInt() and 0xFF) shl 8) or
                (byteArray[2].toInt() and 0xFF)

        return if (int24 and 0x800000 != 0) {
            int24 or 0xFF000000.toInt()
        } else {
            int24
        }
    }*/

    fun getInt24(byteArray: ByteArray): List<Float> {
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


    fun kotlinx.datetime.LocalDateTime.Companion.now() =
        Clock.System.now().toLocalDateTime(currentSystemDefault())

    //запись данных ЭКГ в файлы
    fun readPartial(frequency: Int): Pair<List<Float>, List<Float>> {
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
            fileInputStream = context.openFileInput(fileName)
            val inputString = fileInputStream.bufferedReader().use { it.readText() }
            val outputArray = inputString.split(" ").toTypedArray()

            // Пропускаем первые 8 чисел
            var index = 0

            while (index < outputArray.size) {
                index += 8
                repeat(count) {
                    // Читаем данные по каналу 1
                    repeat(3) {
                        ecg_mas_1.write(outputArray[index].toByteArray())
                        //writeFile(outputArray[index] + "  ", ecgChannel_1)
                        //ecg_string += outputArray[index] + " "
                        index++
                    }

                    //ecg_string += "   "

                    // Читаем данные по каналу 2
                    repeat(3) {
                        ecg_mas_2.write(outputArray[index].toByteArray())
                        //writeFile(outputArray[index] + " ", ecgChannel_2)
                        //ecg_string += outputArray[index] + " "
                        index++
                    }
                    //ecg_string += "\n"
                }
                // Пропускаем следующие 46 чисел
                index += 31 //46
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }

        Log.d(
            ContentValues.TAG,
            "ecg_channel_1.size: ${ecg_mas_1.size()}, ecg_channel_2.size: ${ecg_mas_2.size()}"
        )
        val ecg_mas_1_24 = getInt24(ecg_mas_1.toByteArray())
        val ecg_mas_2_24 = getInt24(ecg_mas_2.toByteArray())

        return Pair(ecg_mas_1_24, ecg_mas_2_24)
        //return ecg_string
    }
}

    /*
    fun convertAndDivideToString(inputString: String): String {
        val DISCRETE: Float = 4f / 8388607 / 8
        return runCatching {
            val byteArray = inputString
                .replace("][", ", ")
                .replace("[", "")
                .replace("]", "")
                .split(", ")
                .map { it.toInt().toByte() }
                .toByteArray()

            val result = mutableListOf<Float>()
            for (i in byteArray.indices step 3) {
                if (i + 2 < byteArray.size) { // Проверяем, чтобы индекс не выходил за границы массива
                    val num =
                        (byteArray[i].toInt() shl 16) + (byteArray[i + 1].toInt() shl 8) + byteArray[i + 2].toInt()
                    val dividedNum = num * DISCRETE
                    result.add(dividedNum)
                }
            }
            result.joinToString(" ")
        }.getOrElse { e ->
            e.printStackTrace()
            "" // Вернуть пустую строку в случае ошибки
        }
    }
     */

