package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.*

@Composable
fun LoginScreen(
    vm: AuthViewModel,
    onSuccess: () -> Unit,
    onRegister: () -> Unit,
    onForgot: () -> Unit
) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                vm.login(email, password)
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
