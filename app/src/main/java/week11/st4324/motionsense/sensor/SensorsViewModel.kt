package week11.st4324.motionsense.sensor

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sqrt
import kotlin.math.abs

// Handles step counting + cadence calculation + motion detection
class SensorsViewModel(app: Application) : AndroidViewModel(app), SensorEventListener {

    // android sensor system
    private val sensorManager = app.getSystemService(SensorManager::class.java)

    // live step counter
    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()

    // cadence (steps per minute)
    private val _cadence = MutableStateFlow(0)
    val cadence = _cadence.asStateFlow()

    // current movement state
    private val _mode = MutableStateFlow("Idle")
    val mode = _mode.asStateFlow()

    // store timestamps of recent steps for SPM math
    private val lastSteps = ArrayDeque<Long>()   // holds last 10 step timestamps

    // sensitivity threshold (higher = less sensitive)
    private val sensitivity = 2.5f                // you requested lower sensitivity

    private var lastStepTime = 0L                 // last detected step time

    // when accelerometer data comes in
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val now = System.currentTimeMillis()      // used for timing + cadence

        // read x,y,z acceleration values
        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]

        // calculate magnitude of movement vector
        val magnitude = sqrt((ax*ax + ay*ay + az*az).toDouble()).toFloat()

        // difference from gravity baseline (9.8m/s²)
        val delta = abs(magnitude - 9.8f)

        // if motion spike high enough = count step
        if (delta > sensitivity && now - lastStepTime > 250) {
            lastStepTime = now
            _steps.value += 1                      // add one real step
            _mode.value = "Walking"               // update movement mode
            updateCadence(now)                    // 👈 new SPM calculation
        }

        // if no steps for 2 sec → user idle
        if (now - lastStepTime > 2000) _mode.value = "Idle"
    }

    // Calculate Steps per Minute
    private fun updateCadence(time: Long) {

        // store time of this step (keep last 10 only)
        lastSteps.addLast(time)
        if (lastSteps.size > 10) lastSteps.removeFirst()

        // require 2+ steps to measure cadence
        if (lastSteps.size < 2) return

        // average interval between recent steps
        val intervals = lastSteps.zipWithNext { a, b -> b - a }
        val avgInterval = intervals.average()     // milliseconds per step

        // SPM = 60,000 / avg_interval_per_step
        _cadence.value = (60000 / avgInterval).toInt()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    // start listening
    fun start() {
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME)
    }

    // stop listening
    fun stop() {
        sensorManager.unregisterListener(this)
    }
}
