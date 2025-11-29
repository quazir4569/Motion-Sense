package week11.st4324.motionsense.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepGraph(stepValues: List<Int>, label: String) {

    Text(label, fontSize = 18.sp)
    Spacer(Modifier.height(12.dp))

    if(stepValues.isEmpty()) {
        Text("No data yet — save a session first.")
        return
    }

    Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) {

        val max = stepValues.max()
        val step = size.width / (stepValues.size + 1)

        // Y axis numbers
        for(i in 1..5){
            drawContext.canvas.nativeCanvas.drawText(
                "${(max/5) * i}",
                10f,
                size.height - (size.height/5*i),
                android.graphics.Paint().apply { textSize = 30f }
            )
        }

        // Plot points
        stepValues.forEachIndexed { i, v ->
            val x = step * (i+1)
            val y = size.height - (v/max.toFloat() * size.height)
            drawCircle(Color.Red, 10f, Offset(x, y))
        }
    }
}
