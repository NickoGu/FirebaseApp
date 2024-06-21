import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore

class MessageViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>>
        get() = _messageList
    val userMail = currentUser?.email

    init {
        loadMessages()
    }

    fun newMessage(messageContent: String) {
        val timestamp = Timestamp.now()

        // Verificar que el usuario esté autenticado
        val userId = currentUser?.uid
        if (userId != null) {
            val userRef = firestore.collection("users").document(userId)

            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userName = documentSnapshot.getString("name") ?: "Unknown"
                        val userImage =
                            documentSnapshot.getString(
                                "imageUrl",
                            ) ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTugu0kegXOT1Gh1sgDVHvYjkGW29w19Hl9gQ&s"

                        val message = Message(userName, messageContent, timestamp.seconds, userImage)

                        firestore.collection("messages")
                            .add(message)
                            .addOnSuccessListener {
                                // Éxito al agregar el mensaje
                            }
                            .addOnFailureListener { e ->
                                // Error al agregar el mensaje
                            }
                    } else {
                        // El documento del usuario no existe
                        // Manejar la situación según tu caso
                    }
                }
                .addOnFailureListener { e ->
                    // Error al obtener el documento del usuario
                }
        } else {
            // El usuario no está autenticado
            // Manejar la situación según tu caso
        }
    }

    fun loadMessages() {
        firestore.collection("messages")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val messages = mutableListOf<Message>()
                snapshot?.forEach { document ->
                    val userName = document.getString("userName") ?: "Unknown"
                    val messageContent = document.getString("messageContent") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0
                    val userImage = document.getString("userImage") ?: ""

                    val message = Message(userName, messageContent, timestamp, userImage)
                    messages.add(message)
                }
                _messageList.value = messages
            }
    }
}

data class Message(
    val userName: String,
    val messageContent: String,
    val timestamp: Long,
    val userImage: String,
)
