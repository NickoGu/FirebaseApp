package com.example.firebaseapp.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun ProfileScreen(
    viewModel: UserViewModel = UserViewModel(),
    navController: NavController,
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var userName by remember { mutableStateOf("") }
    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            selectedImageUri = uri
        }
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth().fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            modifier =
                Modifier
                    .padding(4.dp)
                    .height(300.dp)
                    .width(300.dp)
                    .clip(shape = RoundedCornerShape(100)),
            contentScale = ContentScale.Crop,
        )
        TextField(
            value = userName,
            onValueChange = { newName ->
                userName = newName
            },
            label = { Text("Nombre del Usuario") },
            modifier = Modifier.padding(top = 16.dp),
        )
        // Botón para seleccionar una imagen
        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Seleccionar Imagen")
        }

        Button(
            onClick = {
                selectedImageUri?.let { uri ->
                    viewModel.uploadImage(uri)
                    viewModel.updateName(userName)
                    navController.navigate(route = "fireStore")
                }
            },
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text("Guardar Usuario")
        }
    }
}
