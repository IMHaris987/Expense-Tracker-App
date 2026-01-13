package com.haris.expensetracker.ui.goal

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Goals
import kotlinx.coroutines.launch

class GoalViewModel(private val finanaceRepository: FinanceRepository): ViewModel() {

    fun getGoals(uid: String): LiveData<List<Goals>> {
        return finanaceRepository.getGoalsForUser(uid)
    }

    fun saveGoals(
        userId: String,
        name: String,
        targetAmount: Double,
        savedAmount: Double,
        desiredDate: String,
        note: String
    ) {
        val newGoal = Goals(
            userId = userId,
            name = name,
            targetAmount = targetAmount,
            savedAmount = savedAmount,
            desiredDate = desiredDate,
            note = note
        )

        viewModelScope.launch {
            finanaceRepository.insertGoals(newGoal)
        }
    }
}