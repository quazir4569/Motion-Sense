package week11.st4324.motionsense.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthStateViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    private val listener = FirebaseAuth.AuthStateListener {
        _isLoggedIn.value = it.currentUser != null
    }

    init {
        auth.addAuthStateListener(listener)
    }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}
