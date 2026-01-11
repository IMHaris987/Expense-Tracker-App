package com.haris.expensetracker.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanaceRepository
import com.haris.expensetracker.room.Budget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetViewModel(private val finanaceRepository: FinanaceRepository): ViewModel() {

    val allBudgets = finanaceRepository.allBudgets.asLiveData()

    fun saveBudget(
        name: String,
        amount: Double,
        period: String,
        category: String,
    ) {
        val newBudget = Budget(
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