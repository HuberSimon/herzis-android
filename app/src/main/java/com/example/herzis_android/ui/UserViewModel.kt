package com.example.herzis_android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.herzis_android.data.UserRepository
import com.example.herzis_android.data.local.SavingAccount
import com.example.herzis_android.data.local.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
) : ViewModel() {

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    val allUsers: StateFlow<List<User>> = repository.allUsers
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    val mainUser: StateFlow<User?> = repository.getMainUser().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        null
    )

    val userCount = repository.userCount.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        0
    )


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initializeMainUser()
            _isInitialized.value = true
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}
