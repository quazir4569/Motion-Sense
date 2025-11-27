package week11.st4324.motionsense.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.screens.ForgotPasswordScreen
import week11.st4324.motionsense.ui.screens.HomeScreen
import week11.st4324.motionsense.ui.screens.LoginScreen
import week11.st4324.motionsense.ui.screens.RegisterScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val vm: AuthViewModel = viewModel()
    val senpedvm: SensorsViewModel = viewModel()
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
                senpedvm = senpedvm,
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
