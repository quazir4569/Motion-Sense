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
import androidx.compose.ui.unit.sp
import week11.st4324.motionsense.sensor.StepRepository
import week11.st4324.motionsense.sensor.StepSession
import week11.st4324.motionsense.ui.components.BottomNavBar

@Composable
fun HistoryScreen(
    onHome: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val repo = remember { StepRepository() }
    var sessions by remember { mutableStateOf<List<StepSession>>(emptyList()) }
    val dodgerBlue = Color(0xFF1E90FF)

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
                onProfile = onProfile,
                onHistory = {},
                onLogout = onLogout
            )
        }
    ) { padding ->

        Column(modifier = Modifier
            .background(dodgerBlue)
        ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("History", fontSize = 40.sp, style = MaterialTheme.typography.headlineLarge)
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
