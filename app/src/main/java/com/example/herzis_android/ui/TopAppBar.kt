package com.example.herzis_android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun CustomTopAppBar(title: String, navController: NavController, userName: String) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Herzis",
                    modifier = Modifier
                        .clickable { navController.navigate("herzis") }
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (currentRoute == "herzis") FontWeight.Bold else FontWeight.Normal
                    )
                )
                Text(
                    text = "Sparkonten",
                    modifier = Modifier
                        .clickable { navController.navigate("savings") }
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (currentRoute == "savings") FontWeight.Bold else FontWeight.Normal
                    )
                )
                Text(
                    text = "Transaktionen",
                    modifier = Modifier
                        .clickable { navController.navigate("transactions") }
                        .padding(end = 16.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (currentRoute == "transactions") FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
            IconButton(onClick = { navController.navigate("user") }) {
                CircleWithLetter(letter = userName.firstOrNull()?.uppercaseChar()?.toString() ?: "?")
            }

        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = Color.Gray
        )
    }
}

@Composable
fun CircleWithLetter(letter: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = letter,
            color = Color.White,
        )
    }
}