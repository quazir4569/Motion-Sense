package week11.st4324.motionsense.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.components.BottomNavBar

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HistoryScreen(
    senpedvm: SensorsViewModel,
    onHome: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val sessions by senpedvm.sessions.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHome = onHome,
                onHistory = { /* already here */ },
                onProfile = onProfile,
                onLogout = onLogout
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text("History", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            if (sessions.isEmpty()) {
                Text("No sessions yet.")
            } else {
                sessions.forEachIndexed { index, session ->
                    Text("Session ${index + 1}")
                    Text("• Steps: ${session.steps}")
                    Text("• Avg cadence: ${session.avgCadence} SPM")
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}
