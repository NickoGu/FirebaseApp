package com.example.firebaseapp.ui.theme.screens

import MessageViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@Composable
fun FirestoreScreen(navController: NavController) {
    val viewModel: MessageViewModel = viewModel()

    var messageText by remember { mutableStateOf("") }

    val messageList by viewModel.messageList.observeAsState(initial = emptyList())
    val sortedMessages = messageList.sortedBy { it.timestamp }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = true,
        ) {
            itemsIndexed(sortedMessages.reversed()) { index, message ->
                Row(

                ) {
                    AsyncImage(
                        model = message.userImage,
                        contentDescription = null,
                        modifier =
                        Modifier
                            .padding(4.dp)
                            .height(50.dp)
                            .width(50.dp)
                            .clip(shape = RoundedCornerShape(100)),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        Text(
                            text = message.userName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        )

                        Text(
                            text = message.messageContent,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text(text = if (messageText.isEmpty()) "Escribir mensaje..." else "") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        Icon(Icons.Rounded.Send, contentDescription = "", Modifier.clickable { viewModel.newMessage(messageText)
                            messageText = "" })
                    },
                    shape = RoundedCornerShape(64.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            focusedContainerColor = Color(0xFF3d4354),
                            unfocusedContainerColor = Color(0xFF3d4354)
                    )

                )

        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}

