package week11.st4324.motionsense.auth

data class AuthState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
