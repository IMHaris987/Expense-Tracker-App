package com.haris.expensetracker.data.repository

import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.Budget
import com.haris.expensetracker.room.FinanceDao
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.flow.Flow

class FinanaceRepository(private val financeDao: FinanceDao) {

    val allBudgets: Flow<List<Budget>> = financeDao.getAllBudget()
    val allAccounts: Flow<List<Account>> = financeDao.getAllAccounts()
    val allTransactions: Flow<List<TransactionEntity>> = financeDao.getAllTransaction()

    suspend fun insertBudget(budget: Budget) {
        financeDao.insertBudget(budget)
    }

    suspend fun insertAccount(account: Account) {
        financeDao.insertAccount(account)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        financeDao.insertTransaction(transaction)
    }
}