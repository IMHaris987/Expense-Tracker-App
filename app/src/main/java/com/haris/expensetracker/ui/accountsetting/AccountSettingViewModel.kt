package com.haris.expensetracker.ui.accountsetting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountSettingViewModel(private var repository: FinanceRepository) : ViewModel() {

    val allAccounts : LiveData<List<Account>> = repository.allAccounts

    fun deleteAccount(accountId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAccount(accountId)
        }
    }
}