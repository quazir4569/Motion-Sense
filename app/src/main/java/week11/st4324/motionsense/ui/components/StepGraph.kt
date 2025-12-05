package week11.st4324.motionsense.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun StepGraph(
    stepValues: List<Int>  // list of steps per session
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurface
    val pointColor = MaterialTheme.colorScheme.secondary

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {

        val maxSteps = (stepValues.maxOrNull() ?: 1).toFloat()
        val spacingX = if (stepValues.isNotEmpty())
            size.width / (stepValues.size + 1)
        else
            size.width / 4f

        val paddingBottom = 40f
        val paddingLeft = 40f
        val graphHeight = size.height - paddingBottom

        // Axis
        val origin = Offset(paddingLeft, graphHeight)
        val yEnd = Offset(paddingLeft, 0f)
        val xEnd = Offset(size.width, graphHeight)

        drawLine(
            color = axisColor,
            start = origin,
            end = yEnd,
            strokeWidth = 3f
        )

        drawLine(
            color = axisColor,
            start = origin,
            end = xEnd,
            strokeWidth = 3f
        )

        // Y-axis labels: 0 and maxSteps
        val native = drawContext.canvas.nativeCanvas
        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 28f
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        native.drawText(
            "0",
            paddingLeft - 8f,
            graphHeight,
            textPaint
        )

        native.drawText(
            maxSteps.toInt().toString(),
            paddingLeft - 8f,
            20f,
            textPaint
        )

        // X-axis labels: session numbers
        val xTextPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 26f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        stepValues.forEachIndexed { index, _ ->
            val x = paddingLeft + spacingX * (index + 1)
            native.drawText(
                (index + 1).toString(),
                x,
                size.height,
                xTextPaint
            )
        }

        // No sessions yet -> axes only
        if (stepValues.isEmpty()) return@Canvas

        // Graph line
        val path = Path()
        stepValues.forEachIndexed { index, value ->
            val x = paddingLeft + spacingX * (index + 1)
            val ratio = (value / maxSteps)
            val y = graphHeight - (ratio * graphHeight)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }

            drawCircle(
                color = pointColor,
                radius = 6f,
                center = Offset(x, y)
            )
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4f)
        )
    }
}
