package com.haris.expensetracker.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import java.time.Period

@Dao
interface FinanceDao {

    @Insert
    suspend fun insertAccount(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): kotlinx.coroutines.flow.Flow<List<Account>>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransaction(): kotlinx.coroutines.flow.Flow<List<TransactionEntity>>

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
                // Deduct from Source
                decreaseBalance(transaction.accountId, transaction.amount)
                // Add to Target
                transaction.targetAccountId?.let { targetId ->
                    increaseBalance(targetId, transaction.amount)
                }
            }
        }
    }

    @Insert
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT * FROM budgets")
    fun getAllBudget(): kotlinx.coroutines.flow.Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE userId = :currentUserId")
    fun getBudgetByUserId(currentUserId: String): LiveData<List<Budget>>

    @Query("UPDATE budgets SET spentAmount = spentAmount + :amount WHERE categoryName = :category AND period = :currentPeriod")
    suspend fun updateBudgetSpending(amount: Double, category: String, currentPeriod: String)

    @Transaction
    suspend fun recordExpense(transaction: TransactionEntity, period: String) {
        insertTransaction(transaction)
        reduceAccountBalance(transaction.accountId, transaction.amount)
        updateBudgetSpending(transaction.amount, transaction.categoryName, period)
    }

    @Insert
    suspend fun insertGoals(goal: Goals)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): kotlinx.coroutines.flow.Flow<List<Goals>>

    @Query("SELECT * FROM goals WHERE userId = :currentUserId")
    fun getGoalsByUserId(currentUserId: String): LiveData<List<Goals>>
}