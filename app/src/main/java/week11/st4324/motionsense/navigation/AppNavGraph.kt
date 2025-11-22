package week11.st4324.motionsense.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth
import week11.st4324.motionsense.auth.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import week11.st4324.motionsense.ui.screens.ForgotPasswordScreen
import week11.st4324.motionsense.ui.screens.HomeScreen
import week11.st4324.motionsense.ui.screens.LoginScreen
import week11.st4324.motionsense.ui.screens.RegisterScreen
import week11.st4324.motionsense.ui.screens.*

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val vm: AuthViewModel = viewModel()
    val user = FirebaseAuth.getInstance().currentUser

    NavHost(
        navController = navController,
        startDestination = if (user == null) "login" else "home"
    ) {
        composable("login") {
            LoginScreen(
                vm = vm,
                onSuccess = {
                    vm.clear()
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegister = { navController.navigate("register") },
                onForgot = { navController.navigate("forgot") }
            )
        }
        composable("register") {
            RegisterScreen(
                vm = vm,
                onSuccess = {
                    vm.clear()
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("forgot") {
            ForgotPasswordScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                onLogout = {
                    vm.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
