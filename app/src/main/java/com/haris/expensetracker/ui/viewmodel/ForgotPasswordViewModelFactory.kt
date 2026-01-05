package com.haris.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.AuthRepository

class ForgotPasswordViewModelFactory (
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
            @Suppress ("UNCHECKED_CAST")
            return ForgotPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}