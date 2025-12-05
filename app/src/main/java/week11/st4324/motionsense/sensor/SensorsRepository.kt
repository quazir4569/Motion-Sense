package week11.st4324.motionsense.sensor

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.Q)
class SensorsRepository(private val app: Application) : SensorEventListener {

    private val sensorManager =
        app.getSystemService(SensorManager::class.java)

    private val stepDetector: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var onStep: ((Int) -> Unit)? = null
    private var onCadence: ((Int) -> Unit)? = null
    private var onMode: ((String) -> Unit)? = null

    private var sessionSteps = 0
    private val stepTimestamps = mutableListOf<Long>()

    private var sessionStartTime: Long = 0L
    private var sessionEndTime: Long = 0L

    // Used for Idle detection
    private var lastStepTimeMs: Long = 0L

    // Accelerometer smoothing
    private val alpha = 0.8f
    private var smoothed = 0f

    private val minStepIntervalMs = 300

    fun startSensors(
        onStep: (Int) -> Unit,
        onCadence: (Int) -> Unit,
        onMode: (String) -> Unit
    ) {
        this.onStep = onStep
        this.onCadence = onCadence
        this.onMode = onMode

        stepDetector?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }

        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    fun stopSensors() {
        sensorManager.unregisterListener(this)
    }

    fun beginSession() {
        sessionSteps = 0
        stepTimestamps.clear()
        sessionStartTime = System.currentTimeMillis()
    }

    fun finishSession(): StepSession {
        sessionEndTime = System.currentTimeMillis()

        val avgCadence = if (stepTimestamps.size >= 2) {
            val durationSec =
                max(1.0, (stepTimestamps.last() - stepTimestamps.first()) / 1000.0)
            ((stepTimestamps.size / durationSec) * 60).toInt()
        } else 0

        return StepSession(
            steps = sessionSteps,
            avgCadence = avgCadence,
            startedAt = sessionStartTime,
            endedAt = sessionEndTime
        )
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_STEP_DETECTOR -> handleStepDetector()
            Sensor.TYPE_ACCELEROMETER -> handleAccelerometer(event)
        }
    }

    private fun handleStepDetector() {
        val now = System.currentTimeMillis()
        lastStepTimeMs = now

        sessionSteps++
        stepTimestamps.add(now)

        onStep?.invoke(sessionSteps)
        onMode?.invoke("Walking")

        if (stepTimestamps.size >= 2) {
            val durationSec =
                max(1.0, (stepTimestamps.last() - stepTimestamps.first()) / 1000.0)
            val cadence = ((stepTimestamps.size / durationSec) * 60).toInt()
            onCadence?.invoke(cadence)
        }
    }

    private fun handleAccelerometer(event: SensorEvent) {
        val now = System.currentTimeMillis()

        val rawAccel =
            kotlin.math.sqrt(
                event.values[0] * event.values[0] +
                        event.values[1] * event.values[1] +
                        event.values[2] * event.values[2]
            ) - 9.81f

        smoothed = alpha * smoothed + (1 - alpha) * rawAccel

        val stepThreshold = 1.2f

        if (smoothed > stepThreshold &&
            (now - lastStepTimeMs) > minStepIntervalMs
        ) {
            lastStepTimeMs = now

            sessionSteps++
            stepTimestamps.add(now)

            onStep?.invoke(sessionSteps)
            onMode?.invoke("Walking")

            if (stepTimestamps.size >= 2) {
                val durationSec =
                    max(1.0, (stepTimestamps.last() - stepTimestamps.first()) / 1000.0)
                val cadence = ((stepTimestamps.size / durationSec) * 60).toInt()
                onCadence?.invoke(cadence)
            }
        }
    }

    // polled by ViewModel idle timer
    fun updateIdleState() {
        val now = System.currentTimeMillis()
        if (now - lastStepTimeMs > 2000) {
            onMode?.invoke("Idle")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
