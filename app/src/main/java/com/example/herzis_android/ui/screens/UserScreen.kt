package com.example.herzis_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.herzis_android.ui.App

@Composable
fun UserScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "User Settings",
            modifier = Modifier
                .padding(end = 16.dp),
            style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Meinen Namen ändern",
            modifier = Modifier
                .clickable {  }
                .padding(end = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}