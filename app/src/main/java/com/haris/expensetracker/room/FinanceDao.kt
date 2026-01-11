package com.haris.expensetracker.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

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

    @Insert
    suspend fun insertBudget(budget: Budget)

    @Query("SELECT * FROM budgets")
    fun getAllBudget(): kotlinx.coroutines.flow.Flow<List<Budget>>
}