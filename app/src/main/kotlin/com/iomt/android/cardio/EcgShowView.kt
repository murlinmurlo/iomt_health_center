package com.iomt.android.cardio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay

@Composable
fun EcgShowView(dataStr: String) {
    val data = remember(dataStr) { processData(dataStr) }
    val scrollIndex = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(50)  // Animation frame delay
            scrollIndex.value = (scrollIndex.value + 1) % data.size
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val maxValue = 20f
        val gridColor = Color.Gray
        val ecgColor = Color.Green
        val strokeWidth = 3f
        val gridWidth = 10f

        drawGrid(width, height, gridWidth, gridColor, strokeWidth)
        drawEcgLine(data, scrollIndex.value, width, height, maxValue, ecgColor, strokeWidth)
    }
}

private fun processData(dataStr: String): List<Float> =
    dataStr.split(",").mapNotNull { it.toFloatOrNull() }

private fun DrawScope.drawGrid(width: Float, height: Float, gridWidth: Float, color: Color, strokeWidth: Float) {
    val columns = (width / gridWidth).toInt()
    val rows = (height / gridWidth).toInt()

    for (i in 0..columns) {
        val x = i * gridWidth
        drawLine(color, start = Offset(x, 0f), end = Offset(x, height), strokeWidth)
    }
    for (i in 0..rows) {
        val y = i * gridWidth
        drawLine(color, start = Offset(0f, y), end = Offset(width, y), strokeWidth)
    }
}

private fun DrawScope.drawEcgLine(data: List<Float>, scrollIndex: Int, width: Float, height: Float, maxValue: Float, color: Color, strokeWidth: Float) {
    val path = Path().apply {
        val step = width / data.size
        moveTo(0f, height / 2)
        for (i in data.indices) {
            val x = i * step
            val y = height / 2 - (data[i] / maxValue * height / 2)
            lineTo(x, y)
        }
    }
    drawPath(path, color, style = Stroke(width = strokeWidth))
}
