package com.haris.expensetracker.data.repository

import androidx.lifecycle.LiveData
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.Budget
import com.haris.expensetracker.room.FinanceDao
import com.haris.expensetracker.room.Goals
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val financeDao: FinanceDao) {

    val allAccounts: Flow<List<Account>> = financeDao.getAllAccounts()
    val allTransactions: Flow<List<TransactionEntity>> = financeDao.getAllTransaction()

    suspend fun insertBudget(budget: Budget) {
        financeDao.insertBudget(budget)
    }

    suspend fun processTransaction(transaction: TransactionEntity) {
        financeDao.processTransaction(transaction)
    }

    suspend fun deleteBudget(budgetId: Int) {
        financeDao.deleteBudgetById(budgetId)
    }

    suspend fun insertGoals(goals: Goals) {
        financeDao.insertGoals(goals)
    }

    fun getBudgetsForUser(uid: String): LiveData<List<Budget>> {
        return financeDao.getBudgetByUserId(uid)
    }

    fun getGoalsForUser(uid: String): LiveData<List<Goals>> {
        return financeDao.getGoalsByUserId(uid)
    }

    fun getSpentAmount(category: String, start: Long, end: Long): Double {
        return financeDao.getSpentAmountForCategory(category, start, end) ?: 0.0
    }
}