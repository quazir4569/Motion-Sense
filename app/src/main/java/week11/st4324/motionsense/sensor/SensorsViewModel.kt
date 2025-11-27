package week11.st4324.motionsense.sensor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow

class SensorsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SensorsRepository(application)

    //Use to reference the steps or status from Repository
    val steps: StateFlow<Int> = repo.steps
    val status: StateFlow<String> = repo.status

    //Use for DisposableEffect upon starting the UI
    fun start() = repo.registerStepSensor()
    fun stop() = repo.unregisterStepSensor()

}