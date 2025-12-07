package week11.st4324.motionsense.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.screens.*

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph(
    nav: NavHostController,
    vm: SensorsViewModel,
    authenticate: AuthViewModel,
    startDestination: String
) {
    NavHost(
        navController = nav,
        startDestination = startDestination
    ) {

        composable("login") {
            LoginScreen(
                authenticate = authenticate,
                onSuccess = {
                    nav.navigate("home") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onRegister = { nav.navigate("register") },
                onForgot = { nav.navigate("forgot") }
            )
        }

        composable("register") {
            RegisterScreen(
                authenticate = authenticate,
                onSuccess = {
                    nav.navigate("home") {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable("forgot") {
            ForgotPasswordScreen(
                authenticate = authenticate,
                onBack = { nav.popBackStack() }
            )
        }

        composable("home") {
            HomeScreen(
                senpedvm = vm,
                onHistory = { nav.navigate("history") },
                onProfile = { nav.navigate("profile") },
                onLogout = {
                    vm.saveSessionAndLogout {
                        authenticate.logout()
                        nav.navigate("login") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable("history") {
            HistoryScreen(
                senpedvm = vm,
                onHome = { nav.navigate("home") },
                onProfile = { nav.navigate("profile") },
                onLogout = {
                    vm.saveSessionAndLogout {
                        authenticate.logout()
                        nav.navigate("login") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable("profile") {
            ProfileScreen(
                onHome = { nav.navigate("home") },
                onHistory = { nav.navigate("history") },
                onLogout = {
                    vm.saveSessionAndLogout {
                        authenticate.logout()
                        nav.navigate("login") {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
