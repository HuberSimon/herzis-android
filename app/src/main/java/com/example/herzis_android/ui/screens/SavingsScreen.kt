package com.example.herzis_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.herzis_android.data.local.SavingAccount
import com.example.herzis_android.data.local.Transaction
import com.example.herzis_android.data.local.User
import com.example.herzis_android.ui.SavingAccountViewModel
import com.example.herzis_android.ui.TransactionViewModel
import com.example.herzis_android.ui.UserViewModel
import java.util.Date

@Composable
fun SavingsScreen(userViewModel: UserViewModel, savingAccountViewModel: SavingAccountViewModel, transactionViewModel: TransactionViewModel) {
    val allSavingAccounts by savingAccountViewModel.allSavingAccounts.collectAsState(initial = emptyList())
    val allUsers by userViewModel.allUsers.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedAccount by remember { mutableStateOf<SavingAccount?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(allSavingAccounts) { account ->
                    SavingAccountItem(
                        account,
                        onEdit = { selectedAccount = account; showEditDialog = true }
                    )
                }

            }
        }
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Sparkonto hinzufügen", tint = Color.White)
        }
    }

    if (showDialog) {
        AddAccountDialog(
            onDismiss = { showDialog = false },
            onAddAccount = { name, startBalance ->
                val totBalance = startBalance.toDoubleOrNull() ?: 0.0
                savingAccountViewModel.addAccount(SavingAccount(name = name, totalBalance = totBalance))
                if(totBalance > 0.0) {
                    val newTransaction = Transaction(
                        positiv = true,
                        amount = totBalance,
                        date = Date(),
                        description = "$name Sparkonto mit $totBalance Euro hinzugefügt"
                    )
                    transactionViewModel.addTransaction(newTransaction)
                }
                if(totBalance < 0.0) {
                    val newTransaction = Transaction(
                        positiv = false,
                        amount = totBalance,
                        date = Date(),
                        description = "$name Sparkonto mit $totBalance Euro hinzugefügt"
                    )
                    transactionViewModel.addTransaction(newTransaction)
                }
                showDialog = false
            }
        )
    }

    if (showEditDialog && selectedAccount != null) {
        EditAccountDialog(
            savingAccount = selectedAccount!!,
            transactionViewModel = transactionViewModel,
            onDismiss = { showEditDialog = false },
            onUpdateSavingAccount = { updatedSavingAccount ->
                savingAccountViewModel.updateAccount(updatedSavingAccount)
                showEditDialog = false
            },
            onDelete = { savingAccount ->
                val totalBalance = savingAccount.totalBalance
                val countUsers = allUsers.size
                val splitBalance = totalBalance / countUsers
                if (countUsers > 1) {
                    allUsers.forEach { otherUser ->
                        val updatedBalance = otherUser.balance - splitBalance
                        userViewModel.updateUser(otherUser.copy(balance = updatedBalance))
                    }
                }
                if(totalBalance > 0.0) {
                    val newTransaction = Transaction(
                        positiv = false,
                        amount = totalBalance,
                        date = Date(),
                        description = "${savingAccount.name} Sparkonto gelöscht, Gesamte Summe $totalBalance auf $countUsers mit jeweils $splitBalance Euro aufgeteilt"
                    )
                    transactionViewModel.addTransaction(newTransaction)
                }
                savingAccountViewModel.deleteAccount(savingAccount)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditAccountDialog(
    savingAccount: SavingAccount,
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit,
    onUpdateSavingAccount: (SavingAccount) -> Unit,
    onDelete: (SavingAccount) -> Unit
) {
    val name by remember { mutableStateOf(TextFieldValue(savingAccount.name)) }
    var totalBalance by remember { mutableStateOf(TextFieldValue(savingAccount.totalBalance.toString())) }
    var textValueAdd by remember { mutableStateOf("") }
    var textValueSub by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = savingAccount.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    TextButton(onClick = { onDelete(savingAccount) }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Löschen")
                    }
                }

                BasicTextField(
                    value = textValueAdd,
                    onValueChange = { textValueAdd = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (textValueAdd.isEmpty()) Text("Guthaben eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                BasicTextField(
                    value = textValueSub,
                    onValueChange = { textValueSub = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (textValueSub.isEmpty()) Text("Ausgabe eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Abbrechen")
                        Text(text = "Abbrechen")
                    }
                    TextButton(onClick = {
                        val updatedBalanceAdd = textValueAdd.toDoubleOrNull() ?: 0.0
                        val updatedBalanceSub = textValueSub.toDoubleOrNull() ?: 0.0
                        val total = savingAccount.totalBalance + updatedBalanceAdd - updatedBalanceSub
                        onUpdateSavingAccount(savingAccount.copy(totalBalance = total))
                        if (textValueAdd.isNotEmpty()) {
                            if (updatedBalanceAdd > 0.0) {
                                val newTransaction = Transaction(
                                    positiv = true,
                                    amount = updatedBalanceAdd,
                                    date = Date(),
                                    description = "Auf ${name.text} Sparkonto wurden $updatedBalanceAdd Euro hinzugefügt"
                                )
                                transactionViewModel.addTransaction(newTransaction)
                            }
                        }
                        if (textValueSub.isNotEmpty()) {
                            if(updatedBalanceSub > 0.0) {
                                val newTransaction = Transaction(
                                    positiv = false,
                                    amount = updatedBalanceSub,
                                    date = Date(),
                                    description = "Aus ${name.text} Sparkonto wurden $updatedBalanceSub Euro ausgegeben"
                                )
                                transactionViewModel.addTransaction(newTransaction)
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Done, contentDescription = "Hinzufügen")
                        Text(text = "Hinzufügen")
                    }
                }
            }
        }
    }
}

@Composable
fun AddAccountDialog(onDismiss: () -> Unit, onAddAccount: (String, String) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var startBalance by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Sparkonto hinzufügen", style = MaterialTheme.typography.titleMedium)

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (name.text.isEmpty()) Text("Name eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = startBalance,
                    onValueChange = { startBalance = it },
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (startBalance.text.isEmpty()) Text("Start Kontostand eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Abbrechen")
                        Text("Abbrechen")
                    }
                    TextButton(onClick = {
                        if (name.text.isNotEmpty()) {
                            onAddAccount(name.text, startBalance.text)
                        }
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Hinzufügen")
                        Text("Hinzufügen")
                    }
                }
            }
        }
    }
}

@Composable
fun SavingAccountItem(
    savingAccount: SavingAccount,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onEdit() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = savingAccount.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = savingAccount.totalBalance.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}


