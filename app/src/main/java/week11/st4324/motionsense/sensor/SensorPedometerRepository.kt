package week11.st4324.motionsense.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SensorPedometerRepository(context: Context) : SensorEventListener {
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepDetectorSensor: Sensor? = null
    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private val db = FirebaseFirestore.getInstance()

    fun registerStepSensor() {
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        stepDetectorSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    fun unregisterStepSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            _steps.value += 1

            addSteps(_steps.value)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun addSteps(steps: Int) {

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val info = hashMapOf("steps" to steps)

            db.collection("sensorSteps").document(user.uid).set(info).addOnSuccessListener {
                Log.d("FireStore", "Steps were saved successfully")
            }.addOnFailureListener { e ->
                Log.w("FireStore", "Error, steps were not saved", e)

            }
        }
    }
}