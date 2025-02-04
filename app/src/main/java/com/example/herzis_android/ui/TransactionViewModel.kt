package com.example.herzis_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herzis_android.data.TransactionRepository
import com.example.herzis_android.data.local.Transaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    val transactions: StateFlow<List<Transaction>> = repository.getAllTransactionsByDate()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.insertTransaction(transaction) }
    }
}