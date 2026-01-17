package com.haris.expensetracker.ui.home

import androidx.lifecycle.*
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.TransactionEntity

class HomeViewModel(private val repository: FinanceRepository) : ViewModel() {

    val allAccounts: LiveData<List<Account>> = repository.allAccounts
    val recentTransaction: LiveData<List<TransactionEntity>> = repository.allTransactions
    val totalBalance: LiveData<Double> = repository.allAccounts.map { accounts ->
        accounts.sumOf { it.balance }
    }
    val monthlyExpense: LiveData<Double> = repository.allTransactions.map { transactions ->
        transactions.filter { it.type == "Expense" }.sumOf { it.amount }
    }
}