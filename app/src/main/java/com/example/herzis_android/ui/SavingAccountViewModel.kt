package com.example.herzis_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herzis_android.data.SavingAccountRepository
import com.example.herzis_android.data.local.SavingAccount
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SavingAccountViewModel(private val repository: SavingAccountRepository) : ViewModel() {
    val allSavingAccounts: StateFlow<List<SavingAccount>> = repository.allSavingAccounts
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun addAccount(account: SavingAccount) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun updateAccount(account: SavingAccount) {
        viewModelScope.launch {
            repository.updateAccount(account)
        }
    }

    fun deleteAccount(account: SavingAccount) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }
}