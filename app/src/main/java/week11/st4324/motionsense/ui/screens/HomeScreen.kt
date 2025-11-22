package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}
