package week11.st4324.motionsense.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun AppTextField(value: String, onChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    TextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier
    )
}
