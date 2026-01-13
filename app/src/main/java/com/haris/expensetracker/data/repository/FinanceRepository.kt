package com.haris.expensetracker.data.repository

import androidx.lifecycle.LiveData
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.Budget
import com.haris.expensetracker.room.FinanceDao
import com.haris.expensetracker.room.Goals
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val financeDao: FinanceDao) {

    val allBudgets: Flow<List<Budget>> = financeDao.getAllBudget()
    val allAccounts: Flow<List<Account>> = financeDao.getAllAccounts()
    val allTransactions: Flow<List<TransactionEntity>> = financeDao.getAllTransaction()
    val allGoals: Flow<List<Goals>> = financeDao.getAllGoals()

    suspend fun insertBudget(budget: Budget) {
        financeDao.insertBudget(budget)
    }

    suspend fun insertAccount(account: Account) {
        financeDao.insertAccount(account)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        financeDao.addTransactionAndUpdateAccount(transaction)
    }

    suspend fun recordExpenseWithBudget(transaction: TransactionEntity, period: String) {
        financeDao.recordExpense(transaction, period)
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
}