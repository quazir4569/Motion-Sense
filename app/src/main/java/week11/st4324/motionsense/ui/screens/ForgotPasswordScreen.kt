package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.*

@Composable
fun ForgotPasswordScreen(
    authenticate: AuthViewModel,
    onBack: () -> Unit
) {
    val state by authenticate.state.collectAsState()
    var email by remember { mutableStateOf("") }
    val dodgerBlue = Color(0xFF1E90FF)


    Column(modifier = Modifier
        .background(dodgerBlue)
    ) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Reset Password", fontSize = 39.sp,style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            AppTextField(email, { email = it }, "Email", Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))

            AppButton("Send Reset Email", {
                authenticate.reset(email)
            }, Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("Back") }

            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (state.success) onBack()
        }

        LoadingOverlay(state.loading)
    }
}}
