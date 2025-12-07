package week11.st4324.motionsense.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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

    // Start sensors And load saved sessions
    LaunchedEffect(Unit) {
        senpedvm.startSensors()
        senpedvm.loadSessions()
    }

    DisposableEffect(Unit) {
        onDispose { senpedvm.stopSensors() }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHome = {},
                onHistory = onHistory,
                onProfile = onProfile,
                onLogout = onLogout
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(padding)
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Today", color = Color.White.copy(alpha = 0.8f))
                Text("Motion Dashboard", color = Color.White)

                // Steps And Cadence Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Steps",
                        value = if (sessionActive) steps.toString() else "0",
                        subtitle = "Current session"
                    )
                    StatCard(
                        title = "Cadence",
                        value = if (sessionActive) cadence.toString() else "0",
                        subtitle = "SPM"
                    )
                }

                // Mode card
                StatCard(
                    title = "Mode",
                    value = if (sessionActive) mode else "Idle",
                    modifier = Modifier.fillMaxWidth()
                )

                // Graph card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        // Tooltip state
                        var showInfo by remember { mutableStateOf(false) }

                        // Header And info button
                        Box(modifier = Modifier.fillMaxWidth()) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sessions trend (last 8 Sessions)",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .pointerInput(Unit) {
                                            detectTapGestures {
                                                showInfo = !showInfo
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            // Tooltip
                            if (showInfo) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-10).dp, y = 32.dp)
                                        .shadow(8.dp, RoundedCornerShape(12.dp))
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Tap the dots on the graph for your info!",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Step Trend Graph
                        StepGraph(stepValues = sessions.map { it.steps })
                    }
                }

                // Start/End Session Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!sessionActive) {
                        Button(
                            onClick = { senpedvm.startSession() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Start Session")
                        }
                    } else {
                        Button(
                            onClick = { senpedvm.endSession() },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("End Session (Save)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.headlineMedium)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
