package week11.st4324.motionsense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import week11.st4324.motionsense.AuthRepository

data class AuthState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repo: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(loading = true)
            val result = repo.login(email, password)
            _state.value = if (result.isSuccess) {
                AuthState(success = true)
            } else {
                AuthState(error = result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _state.value = AuthState(loading = true)
            val result = repo.register(email, password)
            _state.value = if (result.isSuccess) {
                AuthState(success = true)
            } else {
                AuthState(error = result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _state.value = AuthState(loading = true)
            val result = repo.resetPassword(email)
            _state.value = if (result.isSuccess) {
                AuthState(success = true)
            } else {
                AuthState(error = result.exceptionOrNull()?.localizedMessage)
            }
        }
    }

    fun logout() {
        repo.logout()
    }
}
