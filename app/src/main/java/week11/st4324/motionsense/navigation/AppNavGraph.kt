package week11.st4324.motionsense.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.screens.ForgotPasswordScreen
import week11.st4324.motionsense.ui.screens.HistoryScreen
import week11.st4324.motionsense.ui.screens.HomeScreen
import week11.st4324.motionsense.ui.screens.LoginScreen
import week11.st4324.motionsense.ui.screens.RegisterScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph(
    nav: NavHostController, vm: SensorsViewModel, authenticate: AuthViewModel
) {
    NavHost(navController = nav, startDestination = "login") {

        composable(route = "home") {
            HomeScreen(
                senpedvm = vm,
                onHistory = { nav.navigate("history") },
                onLogout = { nav.navigate("login")
                { launchSingleTop = true }
            })
        }

        composable(route = "history") {
            HistoryScreen(onHome = {
                nav.navigate("home") },
                onLogout = { nav.navigate("login")
                { launchSingleTop = true }
                })
        }

        composable(route = "login") {
            LoginScreen(
                authenticate = authenticate,
                onSuccess = { nav.navigate("home") },
                onRegister = { nav.navigate("register") },
                onForgot = {nav.navigate("forgot")})
        }

        composable(route = "register") {
            RegisterScreen(
                authenticate = authenticate, onSuccess = { nav.navigate("home")},
                    onBack = {nav.navigate("login")
                    { launchSingleTop = true}
                })
        }

        composable(route = "forgot"){
            ForgotPasswordScreen(
                authenticate = authenticate,
                onBack = {nav.navigate("login")
                { launchSingleTop = true}}
            )
        }
    }
}
