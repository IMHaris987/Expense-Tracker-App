package com.haris.expensetracker.ui.register

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.data.repository.Result
import kotlinx.coroutines.launch

sealed class RegisterState {
    object Idle: RegisterState()
    object Loading: RegisterState()
    data class Success(val user: FirebaseUser): RegisterState()
    data class Failure(val message: String?): RegisterState()
}

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>(RegisterState.Idle)

    val registerState: LiveData<RegisterState> = _registerState

    fun register(name: String, email: String, password: String) {

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            // 6. Call the suspending function on the Repository
            when (val result = repository.register(name, email, password)) {
                is Result.Success -> {
                    // 7. Post the success result to LiveData
                    _registerState.value = RegisterState.Success(result.data)
                }
                is Result.Failure -> {
                    // 8. Post the error result to LiveData
                    _registerState.value = RegisterState.Failure(result.exception.message)
                }
            }
        }
    }
}