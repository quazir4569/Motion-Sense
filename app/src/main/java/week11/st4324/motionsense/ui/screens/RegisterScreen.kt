package week11.st4324.motionsense.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.*

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val state by vm.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.Center) {
            Text("Register", style = MaterialTheme.typography.headlineMedium)

            Spacer(Modifier.height(16.dp))
            AppTextField(email, { email = it }, "Email", Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            AppTextField(pass, { pass = it }, "Password", Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            AppTextField(confirm, { confirm = it }, "Confirm Password", Modifier.fillMaxWidth())

            Spacer(Modifier.height(20.dp))

            AppButton("Create Account", {
                vm.register(email, pass, confirm)
            }, Modifier.fillMaxWidth())

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("Back") }

            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (state.success) onSuccess()
        }

        LoadingOverlay(state.loading)
    }
}
