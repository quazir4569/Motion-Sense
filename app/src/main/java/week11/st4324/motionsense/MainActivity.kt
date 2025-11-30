package week11.st4324.motionsense

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import week11.st4324.motionsense.auth.AuthViewModel
import week11.st4324.motionsense.navigation.AppNavGraph
import week11.st4324.motionsense.sensor.SensorsViewModel
import week11.st4324.motionsense.ui.theme.MotionSenseTheme

class MainActivity : ComponentActivity() {

    private val vm: SensorsViewModel by viewModels()

    private val authenticate: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MotionSenseTheme {
                val nav = rememberNavController()
                AppNavGraph(nav = nav, vm = vm, authenticate = authenticate )
            }
        }
    }
}
