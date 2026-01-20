package com.haris.expensetracker.ui.addaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository

class AddAccountViewModelFactory(private val repository: FinanceRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddAccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}