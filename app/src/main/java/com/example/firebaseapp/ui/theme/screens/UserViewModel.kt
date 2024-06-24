package com.example.firebaseapp.ui.theme.screens

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage
    private val currentUser = auth.currentUser

    // Modelo de datos del usuario
    var userInfo = User("", "", "")

    init {
        loadUserData()
    }

    fun uploadImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profileImages/$userId.jpg")

        storageRef
            .putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Actualizar la URL de la imagen en Firestore
                    firestore
                        .collection("users")
                        .document(userId)
                        .update("imageUrl", uri.toString())
                        .addOnFailureListener { exception ->
                            // Manejar errores de Firestore
                            println("Error updating image URL: $exception")
                        }
                }
            }.addOnFailureListener { exception ->
                // Manejar errores de Firebase Storage
                println("Error uploading image: $exception")
            }
    }

    fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("name") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    userInfo = User(userName, imageUrl, userId)
                } else {
                    // Documento no encontrado o no existe
                    println("Document not found")
                }
            }.addOnFailureListener { exception ->
                // Manejar errores de Firestore
                println("Error fetching user data: $exception")
            }
    }

    fun updateName(newName: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore
            .collection("users")
            .document(userId)
            .update("name", newName)
            .addOnFailureListener { exception ->
                // Manejar errores de Firestore
                println("Error updating name: $exception")
            }
    }
}

data class User(
    val userName: String,
    val imageUrl: String,
    val userId: String,
)
