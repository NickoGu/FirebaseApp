import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MessageViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>>
        get() = _messageList

    init {
        loadMessages()
    }

    fun newMessage(messageContent: String) {
        val timestamp = Timestamp.now()
        val userName = currentUser?.email ?: "Anonymous"
        val message = Message(userName, messageContent, timestamp.seconds)

        firestore.collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Handle success if needed
            }
            .addOnFailureListener {
                // Handle failure if needed
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

                    val message = Message(userName, messageContent, timestamp)
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
)
