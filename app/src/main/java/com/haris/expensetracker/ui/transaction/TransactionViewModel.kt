package com.haris.expensetracker.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionViewModel(private val repository: FinanceRepository) : ViewModel() {

    val allAccounts = repository.allAccounts

    fun processNewTransaction(transaction: TransactionEntity, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val success = repository.processTransaction(transaction)
            withContext(Dispatchers.Main) {
                onResult(success)
            }
        }
    }
}