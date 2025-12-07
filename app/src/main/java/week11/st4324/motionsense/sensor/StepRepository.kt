package week11.st4324.motionsense.sensor

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class StepRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Firebase Storing goes as: users/{uid}/sessions/{sessionId}
    private fun userSessionsCollection(): CollectionReference? {
        val user = auth.currentUser ?: return null
        return db.collection("users")
            .document(user.uid)
            .collection("sessions")
    }

    suspend fun saveSession(session: StepSession) {
        val col = userSessionsCollection() ?: return

        val docRef = if (session.id.isNotBlank()) {
            col.document(session.id)
        } else {
            col.document()
        }

        val user = auth.currentUser

        val sessionWithUser = session.copy(
            id = docRef.id,
            userId = user?.uid ?: "",
            userEmail = user?.email ?: "",
        )

        docRef.set(sessionWithUser).await()
    }

    suspend fun loadSessions(): List<StepSession> {
        val col = userSessionsCollection() ?: return emptyList()

        // order by start time so graphs & history are stable
        val snapshot = col.orderBy("startedAt").get().await()
        return snapshot.toObjects(StepSession::class.java)
    }
}
