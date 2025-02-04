package com.example.herzis_android.data

import com.example.herzis_android.data.local.Transaction
import com.example.herzis_android.data.local.TransactionDao
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    fun getAllTransactionsByDate(): Flow<List<Transaction>> =
        transactionDao.getAllTransactionsByDate()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }
}