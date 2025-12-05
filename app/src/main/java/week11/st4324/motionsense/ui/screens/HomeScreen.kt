package week11.st4324.motionsense.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.components.BottomNavBar
import week11.st4324.motionsense.ui.components.StepGraph

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    senpedvm: SensorsViewModel,
    onHistory: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val steps by senpedvm.steps.collectAsState()
    val mode by senpedvm.mode.collectAsState()
    val cadence by senpedvm.cadence.collectAsState()
    val sessions by senpedvm.sessions.collectAsState()
    val sessionActive by senpedvm.sessionActive.collectAsState()

    // Start sensors when entering the screen
    LaunchedEffect(Unit) {
        senpedvm.startSensors()
        senpedvm.loadSessions()
    }

    // Stop sensors when leaving
    DisposableEffect(Unit) {
        onDispose { senpedvm.stopSensors() }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHome = { /* already here */ },
                onHistory = onHistory,
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Dashboard", style = MaterialTheme.typography.headlineLarge)

            Spacer(Modifier.height(16.dp))

            StepGraph(stepValues = sessions.map { it.steps })

            Spacer(Modifier.height(16.dp))

            Text("Steps (current session): $steps")
            Text("Cadence: $cadence SPM")
            Text("Mode: $mode")

            Spacer(Modifier.height(20.dp))

            if (!sessionActive) {
                Button(onClick = { senpedvm.startSession() }) {
                    Text("Start Session")
                }
            } else {
                Button(onClick = { senpedvm.endSession() }) {
                    Text("End Session (Save)")
                }
            }
        }
    }
}
