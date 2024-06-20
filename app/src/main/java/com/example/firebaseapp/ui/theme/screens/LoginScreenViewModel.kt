package com.example.firebaseapp.ui.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    function()
                } else {
                    Toast.makeText(context, "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    println("Email o contraseña incorrectos")
                }
            }
    }

    fun createAccount(
        email: String,
        password: String,
        function: () -> Unit,
        context: Context,
    ) = viewModelScope.launch {
        _loading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    function()
                } else {
                    Toast.makeText(context, "No se pudo crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
    }
}