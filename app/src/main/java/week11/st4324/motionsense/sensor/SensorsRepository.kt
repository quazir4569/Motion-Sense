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
import kotlin.math.sqrt

class SensorsRepository(context: Context) : SensorEventListener {
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var stepDetectorSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null

    //Values for step counter
    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private var _targetSteps = MutableStateFlow(0)
    var targetSteps: StateFlow<Int> = _targetSteps

    //Value for FireStore Database
    private val db = FirebaseFirestore.getInstance()

    //Values for x-y-z (Added StateFlow for future reference)
    private val _xValue = MutableStateFlow(0.0f)
    val xValue: StateFlow<Float> = _xValue
    private val _yValue = MutableStateFlow(0.0f)
    val yValue: StateFlow<Float> = _xValue
    private val _zValue = MutableStateFlow(0.0f)
    val zValue: StateFlow<Float> = _xValue

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    //This will register the help register or start the sensor
    fun registerStepSensor() {

        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        accelerometerSensor = sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER))

        stepDetectorSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
        accelerometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    //This will unregister or stop the motion sensor.
    fun unregisterStepSensor() {
        sensorManager.unregisterListener(this)
    }

    fun completeSteps(){

        targetSteps
    }

    //This will open a logic as follows:
    //If steps sensor (TYPE_STEP_DETECTOR) is detected, it will add 1 counter to UI and firebase.
    //If accelerometer sensor (TYPE_ACCELEROMETER) is detected, it will add a text
    //base on the magnitude (x-y-z) such as idle, walk, and run.
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            _steps.value += 1

            addSteps(_steps.value)
        }
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            _xValue.value = x
            _yValue.value = y
            _zValue.value = z

            val magnitude = sqrt(x * x + y * y + z * z)

            if (magnitude > 0.0f && magnitude < 10.0f) {

                _status.value = "Idle"
            }

            if (magnitude > 10.0f && magnitude < 15.0f) {

                _status.value = "Walk"
            }

            if (magnitude > 15.0f) {

                _status.value = "Run"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //This will add a step counter to the Firebase (sensorSteps->uid->steps)
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

    private fun addTargetSteps(targetSteps: Int){

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val info = hashMapOf("targetSteps" to targetSteps)

            db.collection("sensorSteps").document(user.uid).set(info).addOnSuccessListener {
                Log.d("FireStore", "Target steps were saved successfully")
            }.addOnFailureListener { e ->
                Log.w("FireStore", "Error, target steps were not saved", e)

            }
        }


    }
}