package com.iomt.android.compose.view.bt


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay





@Composable
fun EcgGraph(
    ecgValues: List<Float>,
    modifier: Modifier = Modifier
) {
    val minValue = ecgValues.minOrNull() ?: 0f
    val maxValue = ecgValues.maxOrNull() ?: 1f
    val timeStep = 1L // Время между каждым значением ЭКГ в миллисекундах

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        items(ecgValues.size) { index ->
            val value = ecgValues[index]
            val x = index * timeStep.toFloat()
            val y = (value - minValue) / (maxValue - minValue) * 200f

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(y.dp)
                    .background(color = Color.Blue)
                    //.align(LineHeightStyle.Alignment.BottomCenter)
            )
        }
    }
}



@Composable
fun EcgScreen(
    ecgValues: List<Float>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        EcgGraph(
            ecgValues = ecgValues,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Preview
@Composable
fun EcgScreenPreview() {
    val ecgValues = listOf(
        1.2f, 0.9f, 1.5f, 1.1f, 0.8f, 1.3f, 1.0f, 1.4f, 0.7f, 1.6f, 1.2f, 0.9f, 1.5f, 1.1f, 0.8f, 1.3f, 1.0f, 1.4f, 0.7f, 1.6f
    )
    EcgScreen(ecgValues = ecgValues)
}



/*
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
*/
@Composable
fun EcgShowView(dataStr: String) {
    val data = remember(dataStr) { processData(dataStr) }
    val scrollIndex = remember { mutableStateOf(0) }

    // Prevent animation and drawing if data is empty
    if (data.isNotEmpty()) {
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
            if (data.size > 0) {  // Additional check to prevent divide by zero
                drawEcgLine(data, scrollIndex.value, width, height, maxValue, ecgColor, strokeWidth)
            }
        }
    } else {
        Text("No data available", color = Color.Red)
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

@Composable
fun EcgScreen(dataStr: String) {
    Box(
        modifier = Modifier
            .size(406.dp, 180.dp)
            .background(Color.LightGray)
    ) {
        EcgShowView(dataStr = dataStr)
    }
}