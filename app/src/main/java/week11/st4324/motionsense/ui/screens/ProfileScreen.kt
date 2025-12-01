package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.components.BottomNavBar

@Composable
fun ProfileScreen(
    senpedvm: SensorsViewModel,
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {

    val dodgerBlue = Color(0xFF1E90FF)


    Scaffold(
        containerColor = Color.White, bottomBar = {
            BottomNavBar(
                onHome = onHome, onProfile = {}, onHistory = onHistory, onLogout = onLogout
            )
        }) { padding ->

        Column(
            modifier = Modifier
                .background(dodgerBlue)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Profile", fontSize = 40.sp, style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(16.dp))

            }
        }
    }
}