package com.haris.expensetracker.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FinanceDao {

    @Insert
    suspend fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): LiveData<List<Account>>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransaction(): LiveData<List<TransactionEntity>>

    @Transaction
    suspend fun addTransactionAndUpdateAccount(transaction: TransactionEntity) {
        insertTransaction(transaction)

        if (transaction.type == "Expense") {
            reduceAccountBalance(transaction.accountId, transaction.amount)
        } else if (transaction.type == "Income") {
            increaseAccountBalance(transaction.accountId, transaction.amount)
        }
    }

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :accountId")
    suspend fun reduceAccountBalance(accountId: Long, amount: Double)

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :accountId")
    suspend fun increaseAccountBalance(accountId: Long, amount: Double)

    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :id")
    suspend fun increaseBalance(id: Long, amount: Double)

    @Query("UPDATE accounts SET balance = balance - :amount WHERE id = :id")
    suspend fun decreaseBalance(id: Long, amount: Double)

    @Transaction
    suspend fun processTransaction(transaction: TransactionEntity) {
        insertTransaction(transaction)

        when (transaction.type) {
            "Expense" -> decreaseBalance(transaction.accountId, transaction.amount)
            "Income" -> increaseBalance(transaction.accountId, transaction.amount)
            "Transfer" -> {
                decreaseBalance(transaction.accountId, transaction.amount)
                transaction.targetAccountId?.let { targetId ->
                    increaseBalance(targetId, transaction.amount)
                }
            }
        }
    }

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudgetById(budgetId: Int)

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: Int)

    @Insert
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT * FROM budgets")
    fun getAllBudget(): LiveData<List<Budget>>

    @Query("SELECT * FROM budgets WHERE userId = :currentUserId")
    fun getBudgetByUserId(currentUserId: String): LiveData<List<Budget>>

    @Insert
    suspend fun insertGoals(goal: Goals)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): LiveData<List<Goals>>

    @Query("SELECT * FROM goals WHERE userId = :currentUserId")
    fun getGoalsByUserId(currentUserId: String): LiveData<List<Goals>>

    @Query("SELECT SUM(amount) FROM transactions WHERE categoryName = :category AND type = 'Expense' AND date BETWEEN :startDate AND :endDate")
    fun getSpentAmountForCategory(category: String, startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE categoryName = :goalCategory AND userId = :uid AND (type = 'Income' OR type = 'Transfer')")
    fun getSavedAmountForGoal(goalCategory: String, uid: String): Double?
}