package com.example.firebaseapp.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel = UserViewModel(),
    navController: NavController,
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var userName by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        selectedImageUri = viewModel.userInfo.imageUrl.toUri()
        userName = viewModel.userInfo.userName
    }
    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent(),
        ) { uri: Uri? ->
            selectedImageUri = uri
        }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Editar Perfil") }, actions = {
            Icon(
                Icons.Rounded.Refresh,
                contentDescription = "",
                Modifier.clickable {
                    selectedImageUri = viewModel.userInfo.imageUrl.toUri()
                },
            )
        })
    }) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model =
                    if (selectedImageUri == null) {
                        selectedImageUri = viewModel.userInfo.imageUrl.toUri()
                        selectedImageUri
                    } else {
                        selectedImageUri
                    },
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
                value =
                    if (userName == "") {
                        userName = viewModel.userInfo.userName
                        userName
                    } else {
                        userName
                    },
                onValueChange = { newName ->
                    userName = newName
                },
                placeholder = { Text(text = viewModel.userInfo.userName) },
                modifier = Modifier.padding(top = 16.dp),
            )
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
                        navController.navigate("fireStore")
                    }
                },
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text("Guardar Usuario")
            }
        }
    }
}
