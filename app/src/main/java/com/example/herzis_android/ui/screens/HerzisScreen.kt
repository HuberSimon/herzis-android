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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Dialog
import com.example.herzis_android.data.local.SavingAccount
import com.example.herzis_android.data.local.Transaction
import com.example.herzis_android.data.local.User
import com.example.herzis_android.ui.SavingAccountViewModel
import com.example.herzis_android.ui.TransactionViewModel
import com.example.herzis_android.ui.UserViewModel
import java.util.Date

@Composable
fun HerzisScreen(userViewModel: UserViewModel, savingAccountViewModel: SavingAccountViewModel, transactionViewModel: TransactionViewModel) {
    val allUsers by userViewModel.allUsers.collectAsState(initial = emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User?>(null) }
    val allSavingAccounts by savingAccountViewModel.allSavingAccounts.collectAsState(initial = emptyList())

    val totalBalance = allSavingAccounts.sumOf { it.totalBalance }
    val userCount = allUsers.size
    val userSavingsBalance = totalBalance / userCount

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            items(allUsers.filter { it.mainUser }) { user ->
                UserItem(
                    user,
                    userSavingsBalance,
                    onEdit = { selectedUser = user; showEditDialog = true }
                )
            }

            val herzalUsers = allUsers.filter { !it.mainUser }
            if (herzalUsers.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        text = "Herzal",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(allUsers.filter { !it.mainUser }) { user ->
                    UserItem(
                        user,
                        userSavingsBalance,
                        onEdit = { selectedUser = user; showEditDialog = true }
                    )
                }
            }
            item{
                Spacer(modifier = Modifier.height(100.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    "Sparkonten",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                allSavingAccounts.forEach { account ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = account.name, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = account.totalBalance.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (account.totalBalance >= 0) Color.Black else Color.Red
                        )

                    }
                }
                Spacer(modifier = Modifier.height(150.dp))
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Herzal hinzufügen", tint = Color.White)
        }
    }

    if (showDialog) {
        AddUserDialog(
            onDismiss = { showDialog = false },
            onAddUser = { name ->
                userViewModel.addUser(User(name = name, balance = 0.0, mainUser = false))
                showDialog = false
            }
        )
    }

    if (showEditDialog && selectedUser != null) {
        EditUserDialog(
            user = selectedUser!!,
            transactionViewModel = transactionViewModel,
            onDismiss = { showEditDialog = false },
            onUpdateUser = { updatedUser ->
                userViewModel.updateUser(updatedUser)
                showEditDialog = false
            },
            onDelete = { user ->
                if (allUsers.size > 1) {
                    val remainingUsers = allUsers.filter { it != user }
                    val totalBalance = user.balance
                    val splitBalance = totalBalance / remainingUsers.size

                    remainingUsers.forEach { otherUser ->
                        val updatedBalance = otherUser.balance + splitBalance
                        userViewModel.updateUser(otherUser.copy(balance = updatedBalance))
                    }
                }

                userViewModel.deleteUser(user)
                showEditDialog = false
            }
        )
    }
}


@Composable
fun AddUserDialog(onDismiss: () -> Unit, onAddUser: (String) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties()) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Herzal hinzufügen", style = MaterialTheme.typography.titleMedium)

                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.Enter) {
                                if (name.text.isNotEmpty()) {
                                    onAddUser(name.text.removeSuffix("\n"))
                                    onDismiss()
                                }
                                true
                            } else {
                                false
                            }
                        },
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.padding(8.dp)) {
                            if (name.text.isEmpty()) Text("Name eingeben", color = Color.Gray)
                            innerTextField()
                        }
                    }
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
                            onAddUser(name.text)
                            onDismiss()
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
fun EditUserDialog(
    user: User,
    transactionViewModel: TransactionViewModel,
    onDismiss: () -> Unit,
    onUpdateUser: (User) -> Unit,
    onDelete: (User) -> Unit
) {
    val name by remember { mutableStateOf(TextFieldValue(user.name)) }
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
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (!user.mainUser && (user.balance == 0.0)) {
                        TextButton(onClick = { onDelete(user) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Löschen")
                        }
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
                            if (textValueSub.isEmpty()) Text("Ausgaben eingeben", color = Color.Gray)
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
                        val totalBalance = user.balance + updatedBalanceAdd - updatedBalanceSub
                        if (name.text.isNotEmpty()) {
                            onUpdateUser(user.copy(balance = totalBalance))
                        }
                        if(updatedBalanceAdd > 0.0) {
                            val newTransaction = Transaction(
                                positiv = true,
                                amount = updatedBalanceAdd,
                                date = Date(),
                                description = "$updatedBalanceAdd Euro auf ${user.name}'s Konto gebucht"
                            )
                            transactionViewModel.addTransaction(newTransaction)
                        }
                        if(updatedBalanceSub > 0.0) {
                            val newTransaction = Transaction(
                                positiv = false,
                                amount = updatedBalanceSub,
                                date = Date(),
                                description = "$updatedBalanceSub Euro von ${user.name}'s Konto ausgegeben"
                            )
                            transactionViewModel.addTransaction(newTransaction)
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
fun UserItem(
    user: User,
    userSavingsBalance: Double,
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = (user.balance+userSavingsBalance).toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if ((user.balance+userSavingsBalance) >= 0) Color.Black else Color.Red
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = user.balance.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if ((user.balance) >= 0) Color.Black else Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
