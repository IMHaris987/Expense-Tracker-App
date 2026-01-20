package com.haris.expensetracker.ui.newcurrency

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.room.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCurrencyViewModel(private val financeRepository: FinanceRepository): ViewModel() {

    fun saveCurrency(currency: Currency) {
        viewModelScope.launch(Dispatchers.IO) {
            financeRepository.saveCurrency(currency)
        }
    }

    fun refreshRates(base: String, target: String, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = financeRepository.fetchRemoteRates(base)
                val rate = response.conversion_rates[target]
                onResult(rate)
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}