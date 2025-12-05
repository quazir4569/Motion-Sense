package week11.st4324.motionsense.sensor

data class StepSession(
    val id: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val steps: Int = 0,
    val avgCadence: Int = 0,
    val startedAt: Long = 0L,
    val endedAt: Long = 0L
)
