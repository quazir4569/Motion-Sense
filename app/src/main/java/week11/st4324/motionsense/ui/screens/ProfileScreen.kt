package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import week11.st4324.motionsense.ui.components.BottomNavBar

@Composable
fun ProfileScreen(
    onHome: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "Not signed in"

    Scaffold(
        bottomBar = {
            BottomNavBar(
                onHome = onHome,
                onHistory = onHistory,
                onProfile = { /* already here */ },
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
            horizontalAlignment = Alignment.Start
        ) {
            Text("Profile", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Text("Email: $email", style = MaterialTheme.typography.titleMedium)
        }
    }
}
