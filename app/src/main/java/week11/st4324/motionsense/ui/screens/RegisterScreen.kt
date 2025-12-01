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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.ui.components.AppButton
import week11.st4324.motionsense.ui.components.AppTextField
import week11.st4324.motionsense.ui.components.LoadingOverlay

@Composable
fun RegisterScreen(
    authenticate: AuthViewModel,
    onSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val state by authenticate.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val dodgerBlue = Color(0xFF1E90FF)

    Column(
        modifier = Modifier
            .background(dodgerBlue)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.padding(50.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Register", fontSize = 40.sp, style = MaterialTheme.typography.headlineMedium)

                Spacer(Modifier.height(16.dp))
                AppTextField(email, { email = it }, "Email", Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                AppTextField(pass, { pass = it }, "Password", Modifier.fillMaxWidth())
                Spacer(Modifier.height(12.dp))
                AppTextField(confirm, { confirm = it }, "Confirm Password", Modifier.fillMaxWidth())

                Spacer(Modifier.height(20.dp))

                AppButton("Create Account", {
                    authenticate.register(email, pass, confirm)
                }, Modifier.fillMaxWidth())

                Spacer(Modifier.height(8.dp))
                TextButton(onClick = onBack) { Text("Back") }

                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (state.success) onSuccess()
            }

            LoadingOverlay(state.loading)
        }
    }
}
