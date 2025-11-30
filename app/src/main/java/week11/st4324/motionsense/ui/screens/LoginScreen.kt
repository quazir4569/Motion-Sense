package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.AppButton
import week11.st4324.motionsense.ui.components.AppTextField
import week11.st4324.motionsense.ui.components.LoadingOverlay

@Composable
fun LoginScreen(
    authenticate: AuthViewModel,
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
    onForgot: () -> Unit
) {
    val state by authenticate.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val beige = Color(0xFFF5F5DC)

    Column(
        modifier = Modifier
            .background(beige)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Login", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(16.dp))

                AppTextField(email, { email = it }, "Email", Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                AppTextField(password, { password = it }, "Password", Modifier.fillMaxWidth())

                Spacer(Modifier.height(20.dp))

                AppButton("Sign In", {
                    authenticate.login(email, password)
                }, Modifier.fillMaxWidth())

                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onRegister) { Text("Create Account") }
                TextButton(onClick = onForgot) { Text("Forgot Password?") }

                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (state.success) onSuccess()
            }

            LoadingOverlay(state.loading)
        }
    }
}
