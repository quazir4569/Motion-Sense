package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.sensor.StepRepository
import week11.st4324.motionsense.sensor.StepSession
import week11.st4324.motionsense.ui.components.BottomNavBar

@Composable
fun HistoryScreen(
    onHome: () -> Unit,
    onLogout: () -> Unit
) {
    val repo = remember { StepRepository() }
    var sessions by remember { mutableStateOf<List<StepSession>>(emptyList()) }
    val beige = Color(0xFFF5F5DC)

    val formatter = remember {
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
    }

    LaunchedEffect(Unit) {
        repo.loadSessions { sessions = it }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHome = onHome,
                onHistory = {},
                onLogout = onLogout
            )
        }
    ) { padding ->

        Column(modifier = Modifier
            .background(beige)
        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("History", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            if (sessions.isEmpty()) {
                Text("No saved sessions yet.")
            } else {
                sessions.forEach { session ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${session.steps} steps")
                            Text(formatter.format(session.timestamp))
                        }
                    }
                }
            }
        }
    }}
}
