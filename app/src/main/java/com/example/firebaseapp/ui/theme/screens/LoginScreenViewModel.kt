package com.example.firebaseapp.ui.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun login(
        email: String,
        password: String,
        function: () -> Unit,
        context: Context,
    ) = viewModelScope.launch {
        _loading.value = true
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    _loading.value = false
                    if (task.isSuccessful) {
                        function()
                    }
                }
        } catch (e: Exception) {
            Firebase.crashlytics.log("Credenciales Inválidas en LoginScreen")
            Toast.makeText(context, "Enviando Error a Firebase", Toast.LENGTH_SHORT).show()
            Firebase.crashlytics.recordException(e)
        } finally {
            Toast.makeText(context, "Credenciales Invalidas", Toast.LENGTH_SHORT).show()
        }
    }

    fun createAccount(
        email: String,
        password: String,
        name: String,
        imageUrl: String,
        function: () -> Unit,
        context: Context,
    ) = viewModelScope.launch {
        _loading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val user =
                            hashMapOf(
                                "name" to name,
                                "imageUrl" to imageUrl,
                                "userId" to userId,
                            )
                        Firebase.firestore.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                function()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error al agregar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "No se pudo obtener el userId", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No se pudo crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
