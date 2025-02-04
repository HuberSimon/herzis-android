package com.example.herzis_android.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE mainUser = 1 LIMIT 1")
    fun getMainUser(): Flow<User?>

    @Query("SELECT name FROM users WHERE mainUser = 1 LIMIT 1")
    fun getMainUserName(): Flow<String?>

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Flow<Int>

}

