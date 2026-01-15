package com.haris.expensetracker.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(private val repository: FinanceRepository) : ViewModel() {

    fun processNewTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.processTransaction(transaction)
        }
    }
}