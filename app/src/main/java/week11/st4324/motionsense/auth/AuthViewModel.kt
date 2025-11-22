package week11.st4324.motionsense.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank()) {
                _state.value = AuthState(error = "Fields cannot be empty")
                return@launch
            }
            _state.value = AuthState(loading = true)
            val result = repo.login(email, password)
            _state.value = if (result.isSuccess) AuthState(success = true)
            else AuthState(error = result.exceptionOrNull()?.localizedMessage)
        }
    }

    fun register(email: String, password: String, confirm: String) {
        viewModelScope.launch {
            if (email.isBlank() || password.isBlank() || confirm.isBlank()) {
                _state.value = AuthState(error = "All fields must be filled")
                return@launch
            }
            if (password != confirm) {
                _state.value = AuthState(error = "Passwords do not match")
                return@launch
            }
            _state.value = AuthState(loading = true)
            val result = repo.register(email, password)
            _state.value = if (result.isSuccess) AuthState(success = true)
            else AuthState(error = result.exceptionOrNull()?.localizedMessage)
        }
    }

    fun reset(email: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                _state.value = AuthState(error = "Email required")
                return@launch
            }
            _state.value = AuthState(loading = true)
            val result = repo.resetPassword(email)
            _state.value = if (result.isSuccess) AuthState(success = true)
            else AuthState(error = result.exceptionOrNull()?.localizedMessage)
        }
    }

    fun logout() { repo.logout() }

    fun clear() { _state.value = AuthState() }
}