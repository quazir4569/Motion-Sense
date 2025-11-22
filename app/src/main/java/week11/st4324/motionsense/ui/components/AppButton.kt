package week11.st4324.motionsense.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(modifier = modifier, onClick = onClick) { Text(text) }
}
