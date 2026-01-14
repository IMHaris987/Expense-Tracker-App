package com.haris.expensetracker.room

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val balance: Double,
    val accountType: String,
    val currency: String
)

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = Account::class, parentColumns = ["id"], childColumns = ["accountId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val note: String,
    val date: java.util.Date,
    val type: String,
    val accountId: Long,
    val targetAccountId: Long? = null,
    val categoryName: String
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val name: String,
    val limitAmount: Double,
    val spentAmount: Double = 0.0,
    val period: String,
    val categoryName: String
)

@Entity(tableName = "goals")
data class Goals(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val desiredDate: String,
    val note: String
)
