package week11.st4324.motionsense

import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun SplashScreen(
    navController: NavController,
    isLoggedIn: Boolean
) {
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
        }
    }
}
