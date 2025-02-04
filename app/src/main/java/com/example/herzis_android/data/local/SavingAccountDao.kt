package com.example.herzis_android.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: SavingAccount)

    @Update
    suspend fun updateAccount(account: SavingAccount)

    @Delete
    suspend fun deleteAccount(account: SavingAccount)

    @Query("SELECT * FROM saving_account WHERE name = :accountName LIMIT 1")
    fun getAccountByName(accountName: String): Flow<SavingAccount>

    @Query("SELECT * FROM saving_account ORDER BY id DESC")
    fun getAllSavingAccounts(): Flow<List<SavingAccount>>
}