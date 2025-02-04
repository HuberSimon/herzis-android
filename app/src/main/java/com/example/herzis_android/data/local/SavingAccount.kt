package com.example.herzis_android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saving_account")
data class SavingAccount(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val totalBalance: Double
)