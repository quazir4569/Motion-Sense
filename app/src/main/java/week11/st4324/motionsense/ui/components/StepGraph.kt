package week11.st4324.motionsense.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun StepGraph(stepValues: List<Int>) {

    // Graph container
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            GraphContent(stepValues)
        }
    }
}

@Composable
private fun GraphContent(stepValues: List<Int>) {
    if (stepValues.isEmpty()) return

    val data = stepValues.takeLast(8)
    val maxSteps = (data.maxOrNull() ?: 1).coerceAtLeast(1)
    val sessionNumbers = (stepValues.size - data.size + 1..stepValues.size).toList()

    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.secondary
    val tooltipColor = MaterialTheme.colorScheme.surface
    val tooltipTextColor = MaterialTheme.colorScheme.onSurface

    // Tap detection for bubble tooltip
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var selectedOffset by remember { mutableStateOf<Offset?>(null) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .pointerInput(data) {
                detectTapGestures { tapOffset ->
                    val boxSize = this.size
                    val w = boxSize.width.toFloat()
                    val h = boxSize.height.toFloat()

                    // Compute points
                    val points = data.mapIndexed { i, steps ->
                        val x = if (data.size == 1) 0f else
                            (i.toFloat() / (data.size - 1)) * w
                        val y = h - (steps.toFloat() / maxSteps) * h
                        Offset(x, y)
                    }

                    // Detect tap near a point
                    val hitIndex = points.indexOfFirst { p ->
                        abs(tapOffset.x - p.x) < 40f &&
                                abs(tapOffset.y - p.y) < 40f
                    }

                    if (hitIndex != -1) {
                        selectedIndex = hitIndex
                        selectedOffset = points[hitIndex]
                    } else {
                        selectedIndex = null
                        selectedOffset = null
                    }
                }
            }
    ) {

        val w = size.width
        val h = size.height

        // Graph points
        val points = data.mapIndexed { i, steps ->
            val x = if (data.size == 1) 0f else
                (i.toFloat() / (data.size - 1)) * w
            val y = h - (steps.toFloat() / maxSteps) * h
            Offset(x, y)
        }

        // Line path
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { p -> lineTo(p.x, p.y) }
        }

        // Fill below the line
        val fillPath = Path().apply {
            addPath(path)
            lineTo(points.last().x, h)
            lineTo(points.first().x, h)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                listOf(
                    lineColor.copy(alpha = 0.35f),
                    Color.Transparent
                )
            )
        )

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 6f)
        )

        // Draw points
        points.forEachIndexed { index, p ->
            drawCircle(
                color = pointColor,
                center = p,
                radius = if (selectedIndex == index) 16f else 12f
            )
        }

        // Tooltip bubble
        if (selectedIndex != null && selectedOffset != null) {
            val idx = selectedIndex!!
            val center = selectedOffset!!

            drawIntoCanvas { canvas ->

                // Tooltip shape
                val radiusX = 110f
                val top = (center.y - 130f).coerceAtLeast(20f)
                val bottom = top + 80f

                val bgPaint = android.graphics.Paint().apply {
                    color = tooltipColor.toArgb()
                    setShadowLayer(12f, 0f, 0f, android.graphics.Color.BLACK)
                    isAntiAlias = true
                }

                canvas.nativeCanvas.drawRoundRect(
                    center.x - radiusX,
                    top,
                    center.x + radiusX,
                    bottom,
                    26f,
                    26f,
                    bgPaint
                )

                // Text inside bubble
                val textPaint = android.graphics.Paint().apply {
                    color = tooltipTextColor.toArgb()
                    textSize = 38f
                    isAntiAlias = true
                }

                canvas.nativeCanvas.drawText(
                    "Session ${sessionNumbers[idx]}",
                    center.x - radiusX + 18f,
                    top + 40f,
                    textPaint
                )
                canvas.nativeCanvas.drawText(
                    "${data[idx]} steps",
                    center.x - radiusX + 18f,
                    top + 70f,
                    textPaint
                )
            }
        }
    }
}

private fun Color.toArgb(): Int =
    android.graphics.Color.argb(
        (alpha * 255).roundToInt(),
        (red * 255).roundToInt(),
        (green * 255).roundToInt(),
        (blue * 255).roundToInt()
    )
