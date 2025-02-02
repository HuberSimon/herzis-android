package com.example.herzis_android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    val name = userAdd()
    NavGraph(name)
}

@Composable
fun userAdd(): String {
    var showDialog by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hallo, $userName", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        if (userName == "") {
            showDialog = true
        }

        if (showDialog) {
            NameInputDialog(
                onDismiss = { showDialog = false },
                onConfirm = { enteredName ->
                    userName = enteredName
                    showDialog = false
                }
            )
        }
    }
    return userName
}

@Composable
fun NameInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gib deinen Namen ein") },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(name) }) {
                Text("OK")
            }
        }
    )
}
