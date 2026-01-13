package com.haris.expensetracker.ui.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Budget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(private val finanaceRepository: FinanceRepository): ViewModel() {

    fun getBudgets(uid: String): LiveData<List<Budget>> {
        return finanaceRepository.getBudgetsForUser(uid)
    }

    fun saveBudget(
        userId: String,
        name: String,
        amount: Double,
        period: String,
        category: String,
    ) {
        val newBudget = Budget(
            userId = userId,
            name = name,
            limitAmount = amount,
            period = period,
            categoryName = category
        )

        viewModelScope.launch(Dispatchers.IO) {
            finanaceRepository.insertBudget(newBudget)
        }
    }
}