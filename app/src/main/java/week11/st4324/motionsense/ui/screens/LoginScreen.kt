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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Motion Sense",
                fontSize = 40.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    value = email,
                    onChange = { email = it },
                    label = "Email",
                    modifier = Modifier.fillMaxWidth()
                )

                AppTextField(
                    value = password,
                    onChange = { password = it },
                    label = "Password",
                    modifier = Modifier.fillMaxWidth()
                )

                AppButton(
                    text = "Sign In",
                    onClick = { authenticate.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                    Text("Create Account")
                }
                TextButton(onClick = onForgot, modifier = Modifier.fillMaxWidth()) {
                    Text("Forgot Password?")
                }

                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (state.success) onSuccess()
            }
        }

        LoadingOverlay(state.loading)
    }
}
