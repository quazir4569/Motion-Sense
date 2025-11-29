package week11.st4324.motionsense.sensor

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class StepSession(
    val steps: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

class StepRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun userId(): String =
        auth.currentUser?.uid ?: "anonymous"

    fun saveSession(steps: Int, onDone: () -> Unit = {}) {
        val session = StepSession(steps = steps)
        db.collection("users")
            .document(userId())
            .collection("sessions")
            .add(session)
            .addOnSuccessListener { onDone() }
    }

    // Load all sessions once
    fun loadSessions(onResult: (List<StepSession>) -> Unit) {
        db.collection("users")
            .document(userId())
            .collection("sessions")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { snap ->
                val list = snap.toObjects(StepSession::class.java)
                onResult(list)
            }
    }
}
