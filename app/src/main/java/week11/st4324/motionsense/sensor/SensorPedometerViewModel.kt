package week11.st4324.motionsense.sensor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.StateFlow

class SensorPedometerViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SensorPedometerRepository(application)

    val steps: StateFlow<Int> = repo.steps

    fun start() = repo.registerStepSensor()

    fun stop() = repo.unregisterStepSensor()

}