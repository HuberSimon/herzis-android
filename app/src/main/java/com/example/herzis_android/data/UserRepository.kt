package com.example.herzis_android.data

import com.example.herzis_android.data.local.User
import com.example.herzis_android.data.local.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    val userCount: Flow<Int> = userDao.getUserCount()

    suspend fun initializeMainUser() {
        val mainUser = userDao.getMainUser().first()
        if (mainUser == null) {
            val newUser = User(name = "", balance = 0.0, mainUser = true)
            userDao.insertUser(newUser)
        }
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    fun getMainUser(): Flow<User?> = userDao.getMainUser()
}
