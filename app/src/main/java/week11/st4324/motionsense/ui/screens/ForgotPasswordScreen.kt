package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.*

@Composable
fun ForgotPasswordScreen(
    vm: AuthViewModel,
    onBack: () -> Unit
) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("Reset Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            AppTextField(email, { email = it }, "Email", Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))

            AppButton("Send Reset Email", {
                vm.reset(email)
            }, Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("Back") }

            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (state.success) onBack()
        }

        LoadingOverlay(state.loading)
    }
}
