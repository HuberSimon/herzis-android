package com.example.herzis_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.herzis_android.data.local.User
import com.example.herzis_android.ui.App
import com.example.herzis_android.ui.UserViewModel

@Composable
fun UserScreen(userViewModel: UserViewModel) {
    val mainUser by userViewModel.mainUser.collectAsState(initial = null)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "User Settings",
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp),
            style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Hauptnutzer ändern",
            modifier = Modifier
                .clickable {
                    if (mainUser != null && mainUser!!.name.isNotEmpty()) {
                        userViewModel.updateUser(User(id = mainUser!!.id, name = "", balance = 0.0, mainUser = true))
                    }
                }
                .padding(end = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
