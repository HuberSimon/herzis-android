package com.example.herzis_android.data

import com.example.herzis_android.data.local.SavingAccount
import com.example.herzis_android.data.local.SavingAccountDao
import kotlinx.coroutines.flow.Flow


class SavingAccountRepository(private val savingAccountDao: SavingAccountDao) {

    val allSavingAccounts: Flow<List<SavingAccount>> = savingAccountDao.getAllSavingAccounts()
    suspend fun insertAccount(account: SavingAccount) {
        savingAccountDao.insertAccount(account)
    }

    suspend fun updateAccount(account: SavingAccount) {
        savingAccountDao.updateAccount(account)
    }

    suspend fun deleteAccount(account: SavingAccount) {
        savingAccountDao.deleteAccount(account)
    }
}