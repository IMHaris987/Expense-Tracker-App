package com.haris.expensetracker.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.data.repository.Result
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle: LoginState()
    object Loading: LoginState()
    data class Success(val user: FirebaseUser): LoginState()
    data class Error(val message: String?): LoginState()
}

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            when (val result = repository.login(email, password)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Success(result.data)
                }
                is Result.Failure -> {
                    _loginState.value = LoginState.Error(result.exception.message)
                }
            }
        }
    }
}