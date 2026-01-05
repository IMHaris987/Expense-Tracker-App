package com.haris.expensetracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.data.repository.Result
import kotlinx.coroutines.launch

sealed class ForgotPassword {
    object Idle: ForgotPassword()
    object Loading: ForgotPassword()
    object Success: ForgotPassword()
    data class Error(val message: String?): ForgotPassword()
}

class ForgotPasswordViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _forgetPasswordState = MutableLiveData<ForgotPassword>(ForgotPassword.Idle)

    val forgetPasswordState: LiveData<ForgotPassword> = _forgetPasswordState

    fun ForgotPassword(email: String) {

        _forgetPasswordState.value = ForgotPassword.Loading

        viewModelScope.launch {
            when (val result = repository.passwordForgot(email)) {
                is Result.Success -> {
                    _forgetPasswordState.postValue(ForgotPassword.Success)
                }
                is Result.Failure -> {
                    _forgetPasswordState.postValue(ForgotPassword.Error(result.exception.message))
                }
            }
        }
    }
}