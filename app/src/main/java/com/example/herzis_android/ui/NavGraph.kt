package com.example.herzis_android.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.herzis_android.ui.screens.HerzisScreen
import com.example.herzis_android.ui.screens.SavingsScreen
import com.example.herzis_android.ui.screens.TransactionsScreen
import com.example.herzis_android.ui.screens.UserScreen

@Composable
fun NavGraph(userViewModel: UserViewModel, savingAccountViewModel: SavingAccountViewModel, transactionViewModel: TransactionViewModel) {
    val navController = rememberNavController()
    val mainUser by userViewModel.mainUser.collectAsState(initial = null)
    val mainUserName = mainUser?.name ?: ""

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Herzis", navController, mainUserName)
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "transactions",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable("herzis") {
                    HerzisScreen(userViewModel, savingAccountViewModel, transactionViewModel)
                }
                composable("savings") {
                    SavingsScreen(userViewModel, savingAccountViewModel, transactionViewModel)
                }
                composable("transactions") {
                    TransactionsScreen(transactionViewModel)
                }
                composable("user") {
                    UserScreen(userViewModel)
                }
            }
        }
    )
}


