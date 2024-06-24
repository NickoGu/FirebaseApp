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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
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
        TopAppBar(title = { Text("Editar Perfíl") }, actions = {
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
            verticalArrangement = Arrangement.SpaceEvenly,
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
            OutlinedTextField(
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
                shape = RoundedCornerShape(64.dp),
                placeholder = { Text(text = "Nombre de usuario") },
                modifier = Modifier.padding(vertical = 16.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedLabelColor = Color.White,
                        focusedContainerColor = Color(0xFF3d4354),
                        unfocusedContainerColor = Color(0xFF3d4354),
                    ),
            )
            Column {
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0232c39),
                            contentColor = Color.White,
                        ),
                ) {
                    Text("Seleccionar imágen")
                }

                Button(
                    onClick = {
                        selectedImageUri?.let { uri ->
                            viewModel.uploadImage(uri)
                            viewModel.updateName(userName)
                            navController.navigate("fireStore")
                        }
                    },
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(
                                    0xFF232c39,
                                ),
                            contentColor = Color.White,
                        ),
                    modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
                ) {
                    Text("Guardar usuario")
                }
            }
        }
    }
}
