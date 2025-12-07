package week11.st4324.motionsense.sensor

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.math.max
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.Q)
class SensorsRepository(private val app: Application) : SensorEventListener {

    private val sensorManager =
        app.getSystemService(SensorManager::class.java)

    // Hardware step detector
    private val stepDetector: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    // Accelerometer for hybrid step detection
    private val accel: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var onStep: ((Int) -> Unit)? = null
    private var onCadence: ((Int) -> Unit)? = null
    private var onMode: ((String) -> Unit)? = null

    private var sessionSteps = 0
    private val stepTimestamps = mutableListOf<Long>()

    private var sessionStartTime: Long = 0L
    private var sessionEndTime: Long = 0L

    private var lastStepTimeMs: Long = 0L

    // Hybrid detection timing
    private var lastLogicStepTimeMs: Long = 0L
    private val minStepIntervalMs = 250L // about 4 steps/sec max

    // Gravity estimate
    private var gravityX = 0.0
    private var gravityY = 0.0
    private var gravityZ = SensorManager.GRAVITY_EARTH.toDouble()
    private var lastVerticalDynamic = 0.0
    private val stepThreshold = 1.4 // up it to lower sensitivity, lower to up sensitivity

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
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        accel?.let {
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

        lastStepTimeMs = sessionStartTime
        lastLogicStepTimeMs = sessionStartTime - minStepIntervalMs

        lastVerticalDynamic = 0.0
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
            Sensor.TYPE_STEP_DETECTOR -> handleStepDetectorEvent()
            Sensor.TYPE_ACCELEROMETER -> handleAccelEvent(event)
        }
    }

    private fun handleStepDetectorEvent() {
        val now = System.currentTimeMillis()
        if (now - lastLogicStepTimeMs < minStepIntervalMs) return
        registerStep(now)
    }

    private fun handleAccelEvent(event: SensorEvent) {
        val x = event.values[0].toDouble()
        val y = event.values[1].toDouble()
        val z = event.values[2].toDouble()

        gravityX = 0.9 * gravityX + 0.1 * x
        gravityY = 0.9 * gravityY + 0.1 * y
        gravityZ = 0.9 * gravityZ + 0.1 * z

        val gx = gravityX
        val gy = gravityY
        val gz = gravityZ

        val gravityMag = sqrt(gx * gx + gy * gy + gz * gz)
        if (gravityMag < 1e-3) {
            lastVerticalDynamic = 0.0
            return
        }

        // Dynamic acceleration
        val dx = x - gx
        val dy = y - gy
        val dz = z - gz

        val verticalDynamic =
            (dx * gx + dy * gy + dz * gz) / gravityMag

        val now = System.currentTimeMillis()
        val interval = now - lastLogicStepTimeMs

        val isPeak =
            verticalDynamic > stepThreshold &&
                    lastVerticalDynamic <= stepThreshold &&
                    interval >= minStepIntervalMs

        lastVerticalDynamic = verticalDynamic

        if (isPeak) {
            registerStep(now)
        }
    }

    private fun registerStep(timestampMs: Long) {
        lastLogicStepTimeMs = timestampMs
        lastStepTimeMs = timestampMs

        sessionSteps++
        stepTimestamps.add(timestampMs)
        onStep?.invoke(sessionSteps)

        if (stepTimestamps.size == 1) {
            onMode?.invoke("Walking")
            onCadence?.invoke(0)
            return
        }

        val last = stepTimestamps[stepTimestamps.size - 1]
        val prev = stepTimestamps[stepTimestamps.size - 2]
        val dtSec = max(0.1, (last - prev) / 1000.0)
        val instantCadence = (60.0 / dtSec).toInt()

        if (instantCadence > 60) {
            onMode?.invoke("Walking")
        }

        val durationSec =
            max(1.0, (stepTimestamps.last() - stepTimestamps.first()) / 1000.0)
        val avgCadence = ((stepTimestamps.size / durationSec) * 60).toInt()

        onCadence?.invoke(avgCadence)
    }

    fun updateIdleState() {
        val now = System.currentTimeMillis()
        if (now - lastStepTimeMs > 2000) {
            onMode?.invoke("Idle")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
