package com.haris.expensetracker.ui.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Goals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoalViewModel(private val finanaceRepository: FinanceRepository): ViewModel() {

    private val _goalsWithProgress = MutableLiveData<List<Goals>>()
    val goalsWithProgress: LiveData<List<Goals>> = _goalsWithProgress

    fun calculateGoalsProgress(rawGoals: List<Goals>, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val updatedGoals = rawGoals.map { goal ->
                val transactionTotal = finanaceRepository.getSavedAmount(goal.categoryName, uid) ?: 0.0

                println("DEBUG: Goal: ${goal.name}, Category: ${goal.categoryName}, Found in Trans: $transactionTotal")

                val totalProgress = goal.savedAmount + transactionTotal
                goal.copy(savedAmount = totalProgress)
            }
            withContext(Dispatchers.Main) {
                _goalsWithProgress.value = updatedGoals
            }
        }
    }

    fun getGoals(uid: String): LiveData<List<Goals>> {
        return finanaceRepository.getGoalsForUser(uid)
    }

    fun deleteGoal(goalId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            finanaceRepository.deleteGoal(goalId)
        }
    }

    fun saveGoals(
        userId: String,
        name: String,
        targetAmount: Double,
        savedAmount: Double,
        desiredDate: String,
        categoryName: String,
        note: String
    ) {
        val newGoal = Goals(
            userId = userId,
            name = name,
            targetAmount = targetAmount,
            savedAmount = savedAmount,
            desiredDate = desiredDate,
            categoryName = categoryName,
            note = note
        )

        viewModelScope.launch {
            finanaceRepository.insertGoals(newGoal)
        }
    }
}