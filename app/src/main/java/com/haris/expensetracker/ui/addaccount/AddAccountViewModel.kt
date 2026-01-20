package com.haris.expensetracker.ui.addaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAccountViewModel(private var repository: FinanceRepository) : ViewModel() {

    val availableCurrencies: LiveData<List<Currency>> = repository.allCurrencies

    fun saveAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAccount(account)
        }
    }
}