package com.haris.expensetracker.data.repository

import androidx.lifecycle.LiveData
import com.haris.expensetracker.network.ExchangeRateResponse
import com.haris.expensetracker.network.RetrofitInstance
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.Budget
import com.haris.expensetracker.room.Currency
import com.haris.expensetracker.room.FinanceDao
import com.haris.expensetracker.room.Goals
import com.haris.expensetracker.room.TransactionEntity

class FinanceRepository(private val financeDao: FinanceDao) {

    val allAccounts: LiveData<List<Account>> = financeDao.getAllAccounts()
    val allTransactions: LiveData<List<TransactionEntity>> = financeDao.getAllTransaction()
    val allCurrencies: LiveData<List<Currency>> = financeDao.getAllCurrencies()

    suspend fun insertBudget(budget: Budget) {
        financeDao.insertBudget(budget)
    }

    suspend fun insertAccount(account: Account) {
        financeDao.insertAccount(account)
    }

    suspend fun processTransaction(transaction: TransactionEntity) {
        financeDao.processTransaction(transaction)
    }

    suspend fun deleteBudget(budgetId: Int) {
        financeDao.deleteBudgetById(budgetId)
    }

    suspend fun saveCurrency(currency: Currency) {
        financeDao.insertCurrency(currency)
    }

    suspend fun deleteGoal(goalId: Int) {
        financeDao.deleteGoalById(goalId)
    }

    suspend fun deleteAccount(accountId: Long) {
        financeDao.deleteAccountById(accountId)
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

    fun getSavedAmount(goalCategory: String, uid: String): Double {
        return financeDao.getSavedAmountForGoal(goalCategory, uid) ?: 0.0
    }

    suspend fun getBudgetById(id: Long): Budget? {
        return financeDao.getBudgetById(id)
    }

    suspend fun updateBudget(budget: Budget) {
        financeDao.updateBudget(budget)
    }

    suspend fun getGoalById(id: Long): Goals? {
        return financeDao.getGoalById(id)
    }

    suspend fun updateGoal(goal: Goals) {
        financeDao.updateGoal(goal)
    }

    suspend fun fetchRemoteRates(base: String): ExchangeRateResponse {
        return RetrofitInstance.api.getExchangeRates(base)
    }
}