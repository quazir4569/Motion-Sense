package week11.st4324.motionsense

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.navigation.AppNavGraph
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.theme.MotionSenseTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MotionSenseTheme {

                val nav = rememberNavController()

                val authVM: AuthViewModel = viewModel()
                val sensorVM: SensorsViewModel = viewModel(
                    factory = SensorsViewModel.Factory(application)
                )

                // Determine where the app starts
                val user = FirebaseAuth.getInstance().currentUser
                val startDest = if (user != null) "home" else "login"

                AppNavGraph(
                    nav = nav,
                    vm = sensorVM,
                    authenticate = authVM,
                    startDestination = startDest
                )
            }
        }
    }
}
