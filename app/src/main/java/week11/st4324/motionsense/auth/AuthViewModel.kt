package week11.st4324.motionsense.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class AuthState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val auth = FirebaseAuth.getInstance()

    fun login(email: String, password: String) {
        _state.update { it.copy(loading = true, error = null, success = false) }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update { it.copy(loading = false, success = true) }
                } else {
                    _state.update {
                        it.copy(
                            loading = false,
                            error = task.exception?.message
                        )
                    }
                }
            }
    }

    fun register(email: String, password: String, confirm: String) {
        if (password != confirm) {
            _state.update { it.copy(error = "Passwords do not match") }
            return
        }

        _state.update { it.copy(loading = true, error = null, success = false) }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update { it.copy(loading = false, success = true) }
                } else {
                    _state.update {
                        it.copy(
                            loading = false,
                            error = task.exception?.message
                        )
                    }
                }
            }
    }

    fun reset(email: String) {
        _state.update { it.copy(loading = true, error = null, success = false) }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.update { it.copy(loading = false, success = true) }
                } else {
                    _state.update {
                        it.copy(
                            loading = false,
                            error = task.exception?.message
                        )
                    }
                }
            }
    }

    fun resetState() {
        _state.value = AuthState()
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        resetState()
    }
}
