package com.haris.expensetracker.ui.viewmodel

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

    // 2. Private MutableLiveData for internal changes
    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    // 3. Public LiveData for the Activity to observe (read-only)
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        // 4. Set state to Loading immediately
        _loginState.value = LoginState.Loading

        // 5. viewModelScope: A CoroutineScope tied to the ViewModel's lifecycle
        viewModelScope.launch {
            // 6. Call the suspending function on the Repository
            when (val result = repository.login(email, password)) {
                is Result.Success -> {
                    // 7. Post the success result to LiveData
                    _loginState.postValue(LoginState.Success(result.data))
                }
                is Result.Failure -> {
                    // 8. Post the error result to LiveData
                    _loginState.postValue(LoginState.Error(result.exception.message))
                }
            }
        }
    }
}