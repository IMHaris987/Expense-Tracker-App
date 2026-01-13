package com.haris.expensetracker.ui.home

import androidx.lifecycle.*
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.flow.map

class HomeViewModel(private val repository: FinanceRepository) : ViewModel() {

    val recentTransactions: LiveData<List<TransactionEntity>> =
        repository.allTransactions.asLiveData()

    val totalBalance: LiveData<Double> = repository.allAccounts.map { accounts ->
        accounts.sumOf { it.balance }
    }.asLiveData()

    val monthlyExpense: LiveData<Double> = repository.allTransactions.map { transactions ->
        transactions.filter { it.type == "Expense" }.sumOf { it.amount }
    }.asLiveData()
}