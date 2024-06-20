package com.example.firebaseapp.ui.theme.screens

import MessageViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FirestoreScreen() {
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
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Rounded.AccountCircle, contentDescription = "Icon")
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
            modifier =
                Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text(text = "Escribir mensaje") },
                singleLine = true,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.newMessage(messageText)
                    messageText = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF3483FA)),
            ) {
                Icon(Icons.Rounded.Send, contentDescription = "")
            }
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
    }
}
