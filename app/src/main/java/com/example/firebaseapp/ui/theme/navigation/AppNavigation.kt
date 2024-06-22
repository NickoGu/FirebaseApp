package com.example.firebaseapp.ui.theme.navigation

import LoginScreen
import SignupScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebaseapp.ui.theme.screens.FirestoreScreen
import com.example.firebaseapp.ui.theme.screens.HomeScreen
import com.example.firebaseapp.ui.theme.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LoginScreen(padding = PaddingValues(16.dp), navController = navController)
        }
        composable(route = "signUp") {
            SignupScreen(padding = PaddingValues(16.dp), navController = navController)
        }
        composable(route = "homeScreen") {
            HomeScreen()
        }
        composable(route = "fireStore") {
            FirestoreScreen(navController = navController)
        }
        composable(route = "editUser") {
            ProfileScreen(navController = navController)
        }
    }
}
