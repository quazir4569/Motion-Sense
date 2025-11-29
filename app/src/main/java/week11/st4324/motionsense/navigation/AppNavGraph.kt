package week11.st4324.motionsense.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.screens.HistoryScreen
import week11.st4324.motionsense.ui.screens.HomeScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph(
    nav: NavHostController,
    vm: SensorsViewModel
) {
    NavHost(navController = nav, startDestination = "home") {

        composable(route = "home") {
            HomeScreen(
                senpedvm = vm,
                onHistory = { nav.navigate("history") },
                onLogout = { nav.navigate("login") }
            )
        }

        composable(route = "history") {
            HistoryScreen(
                onHome = { nav.navigate("home") },
                onLogout = { nav.navigate("login") }
            )
        }

    }
}
