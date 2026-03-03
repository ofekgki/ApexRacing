package com.example.apexracing.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apexracing.utilities.Mapper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class UserViewModel : ViewModel() {
    sealed class UserUiState {
        data object Idle : UserUiState()
        data object Loading : UserUiState()
        data class Ready(val user: User) : UserUiState()
        data class Error(val message: String) : UserUiState()
    }

    private val _state = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val state: StateFlow<UserUiState> = _state

    fun loadUser(uid: String) {
        viewModelScope.launch {
            try {
                _state.value = UserUiState.Loading

                //load ids from RTDB
                val snap = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .get()
                    .await()
                val ids = snap.getValue(UserIds::class.java)
                    ?: throw IllegalStateException("User not found in RTDB")

                // ) map to full domain user
                val user = Mapper.userMapper(ids)

                _state.value = UserUiState.Ready(user)
            } catch (e: Exception) {
                _state.value = UserUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun clear() {
        _state.value = UserUiState.Idle
    }
}
