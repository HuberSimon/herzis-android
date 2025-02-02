package com.example.herzis_android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.herzis_android.ui.screens.HomeScreen
import com.example.herzis_android.ui.screens.DetailScreen
import com.example.herzis_android.ui.screens.SettingsScreen
import com.example.herzis_android.ui.screens.UserScreen

@Composable
fun NavGraph(userName: String) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Meine App", navController, userName)
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen()
                }
                composable("detail") {
                    DetailScreen()
                }
                composable("settings") {
                    SettingsScreen()
                }
                composable("user") {
                    UserScreen()
                }
            }
        }
    )
}


