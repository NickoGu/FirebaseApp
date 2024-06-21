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
    private val currentUser = auth.currentUser
    val storage = Firebase.storage

    fun uploadImage(imageUri: Uri) {
        val userId = auth.currentUser?.uid ?: return

        // Referencia a la imagen en Storage
        val storageRef = storage.reference.child("profileImages/$userId.jpg")

        // Subir imagen a Firebase Storage
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Obtener la URL de descarga de la imagen
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Actualizar el campo de la imagen en Firestore
                    firestore.collection("users").document(userId)
                        .update("imageUrl", uri.toString())
                        .addOnSuccessListener {
                            // Éxito al actualizar la imagen del usuario
                            // Puedes mostrar un mensaje de éxito o actualizar el estado en el ViewModel
                        }
                        .addOnFailureListener { exception ->
                            // Manejar errores de Firestore
                        }
                }
            }
    }

    fun updateName(newName: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .update("name", newName)
            .addOnSuccessListener {
                // Éxito al actualizar la imagen del usuario
                // Puedes mostrar un mensaje de éxito o actualizar el estado en el ViewModel
            }
            .addOnFailureListener { exception ->
                // Manejar errores de Firestore
            }
    }
}

data class User(
    val userName: String,
    val imageUrl: String,
    val userId: String,
)
