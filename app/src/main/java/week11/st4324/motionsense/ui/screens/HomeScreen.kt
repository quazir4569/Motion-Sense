package week11.st4324.motionsense.ui.screens

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import week11.st4324.motionsense.sensor.SensorPedometerViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(senpedvm: SensorPedometerViewModel, onLogout: () -> Unit) {

    val steps by senpedvm.steps.collectAsState()

    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    val userEmail: String? = currentUser?.email

    DisposableEffect(Unit) {
        senpedvm.start()
        onDispose { senpedvm.stop() }
    }

    val permission = rememberPermissionState(permission = Manifest.permission.ACTIVITY_RECOGNITION)

    LaunchedEffect(Unit) {

        permission.launchPermissionRequest()
    }


    if (permission.status.isGranted) {
        senpedvm.start()
    } else {
        Text("Permission denied. Cannot count steps.")
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Home",
            modifier = Modifier.padding(30.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(52.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Blue),

                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )

                        }

                        Text(text = "$userEmail!")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Steps",
                                modifier = Modifier.padding(30.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        when {
                            permission.status.isGranted -> {
                                Text(text = "Step count: $steps")
                            }

                            permission.status.shouldShowRationale -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Permission needed to count your steps.")
                                    Button(onClick = { permission.launchPermissionRequest() }) {
                                        Text(text = "Give Permission")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Text("Logout")
            }
        }
    }
}
