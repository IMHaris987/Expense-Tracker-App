package com.haris.expensetracker.ui.newcurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository

class NewCurrencyViewModelFactory(private val financeRepository: FinanceRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewCurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewCurrencyViewModel(financeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}