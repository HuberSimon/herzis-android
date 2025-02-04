package com.example.herzis_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.herzis_android.data.SavingAccountRepository
import com.example.herzis_android.data.TransactionRepository
import com.example.herzis_android.data.UserRepository
import com.example.herzis_android.data.local.AppDatabase
import com.example.herzis_android.ui.App
import com.example.herzis_android.ui.SavingAccountViewModel
import com.example.herzis_android.ui.TransactionViewModel
import com.example.herzis_android.ui.UserViewModel
import com.example.herzis_android.ui.theme.HerzisandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDatabase = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(appDatabase.userDao())
        val savingAccountRepository = SavingAccountRepository(appDatabase.savingAccountsDao())
        val transactionRepository = TransactionRepository(appDatabase.transactionDao())
        val userViewModel = UserViewModel(userRepository)
        val savingAccountViewModel = SavingAccountViewModel(savingAccountRepository)
        val transactionViewModel = TransactionViewModel(transactionRepository)

        setContent {
            HerzisandroidTheme {
                // Wrapping the NavGraph inside MaterialTheme for styling
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App(userViewModel, savingAccountViewModel, transactionViewModel)
                }
            }
        }
    }
}