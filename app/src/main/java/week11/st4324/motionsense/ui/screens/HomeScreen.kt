package week11.st4324.motionsense.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.sensor.StepRepository
import week11.st4324.motionsense.sensor.StepSession
import week11.st4324.motionsense.ui.components.BottomNavBar
import week11.st4324.motionsense.ui.components.StepGraph

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    senpedvm: SensorsViewModel, onHistory: () -> Unit, onLogout: () -> Unit
) {
    val steps by senpedvm.steps.collectAsState()
    val mode by senpedvm.mode.collectAsState()
    val cadence by senpedvm.cadence.collectAsState()

    val repo = remember { StepRepository() }
    var sessions by remember { mutableStateOf<List<StepSession>>(emptyList()) }
    val scope = rememberCoroutineScope()

    val beige = Color(0xFFF5F5DC)

    // initial load
    LaunchedEffect(Unit) {
        repo.loadSessions { sessions = it }
        senpedvm.start()
    }

    Scaffold(
        containerColor = Color.White, bottomBar = {
            BottomNavBar(
                onHome = {}, onHistory = onHistory, onLogout = onLogout
            )
        }) { padding ->

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

                Text("Home", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(16.dp))

                Text("Steps: $steps", style = MaterialTheme.typography.headlineMedium)
                Text("Cadence: $cadence SPM")
                Text("Mode: $mode")

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = {
                        scope.launch {
                            repo.saveSession(steps) {
                                repo.loadSessions { sessions = it }
                            }
                        }
                    }) {
                    Text("Save Session")
                }

                Spacer(Modifier.height(30.dp))

                StepGraph(
                    stepValues = sessions.map { it.steps }, label = "Steps"
                )
            }
        }
    }
}
