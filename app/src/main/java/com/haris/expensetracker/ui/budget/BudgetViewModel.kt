package com.haris.expensetracker.ui.budget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Budget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class BudgetViewModel(private val repository: FinanceRepository): ViewModel() {

    private val _budgetsWithProgress = MutableLiveData<List<Budget>>()
    val budgetsWithProgress: LiveData<List<Budget>> = _budgetsWithProgress

    fun getBudgets(uid: String): LiveData<List<Budget>> {
        return repository.getBudgetsForUser(uid)
    }

    fun deleteBudget(budgetId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBudget(budgetId)
        }
    }

    fun calculateBudgetProgress(rawBudgets: List<Budget>) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedBudgets = rawBudgets.map { budget ->
                val (start, end) = getDatesForPeriod(budget.period)

                val actualSpent = repository.getSpentAmount(budget.categoryName, start, end)
                budget.copy(spentAmount = actualSpent)
            }
            withContext(Dispatchers.Main) {
                _budgetsWithProgress.value = updatedBudgets
            }
        }
    }

    private fun getDatesForPeriod(period: String): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        when (period) {
            "Weekly" -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            }
            "Monthly" -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }
            "Yearly" -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val startDate = calendar.timeInMillis
        return Pair(startDate, endDate)
    }

    fun saveBudget(userId: String, name: String, amount: Double, period: String, category: String) {
        val newBudget = Budget(
            userId = userId,
            name = name,
            limitAmount = amount,
            spentAmount = 0.0,
            period = period,
            categoryName = category
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBudget(newBudget)
        }
    }
}