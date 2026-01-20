package com.haris.expensetracker.ui.accountsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository

class AccountSettingViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountSettingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountSettingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}