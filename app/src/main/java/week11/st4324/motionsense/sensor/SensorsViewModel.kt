package week11.st4324.motionsense.sensor

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
class SensorsViewModel(
    private val app: Application
) : ViewModel() {

    private val repo = SensorsRepository(app)
    private val stepRepo = StepRepository()

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private val _cadence = MutableStateFlow(0)
    val cadence: StateFlow<Int> = _cadence

    private val _mode = MutableStateFlow("Idle")
    val mode: StateFlow<String> = _mode

    private val _sessionActive = MutableStateFlow(false)
    val sessionActive: StateFlow<Boolean> = _sessionActive

    private val _sessions = MutableStateFlow<List<StepSession>>(emptyList())
    val sessions: StateFlow<List<StepSession>> = _sessions

    init {
        // Load existing sessions when ViewModel is created
        loadSessions()

        // Idle detection loop
        viewModelScope.launch {
            while (true) {
                delay(1500)
                repo.updateIdleState()
            }
        }
    }

    fun startSensors() {
        repo.startSensors(
            onStep = { _steps.value = it },
            onCadence = { _cadence.value = it },
            onMode = { _mode.value = it }
        )
    }

    fun stopSensors() {
        repo.stopSensors()
        _mode.value = "Idle"
    }

    fun startSession() {
        _sessionActive.value = true
        _steps.value = 0
        repo.beginSession()
    }

    fun endSession() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_sessionActive.value) {
                val session = repo.finishSession()
                stepRepo.saveSession(session)
                _sessions.value = stepRepo.loadSessions()
                _sessionActive.value = false
            }
        }
    }

    fun loadSessions() {
        viewModelScope.launch(Dispatchers.IO) {
            _sessions.value = stepRepo.loadSessions()
        }
    }


    //Used by logout to ensure active session is saved
    //Before signing out and navigating away.

    fun saveSessionAndLogout(onDone: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_sessionActive.value) {
                val session = repo.finishSession()
                stepRepo.saveSession(session)
                _sessionActive.value = false
            }
            _sessions.value = stepRepo.loadSessions()
            onDone()
        }
    }

    companion object {
        fun Factory(app: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return SensorsViewModel(app) as T
                }
            }
    }
}