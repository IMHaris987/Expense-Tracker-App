package com.haris.expensetracker.ui.newcurrency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityNewCurrencyBinding
import com.haris.expensetracker.room.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCurrencyViewModel(private val financeRepository: FinanceRepository): ViewModel() {

    private val _exchangeRate = MutableLiveData<String>()
    val exchangeRate: LiveData<String> = _exchangeRate

    private val _inverseRate = MutableLiveData<String>()
    val inverseRate: LiveData<String> = _inverseRate
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

    fun updateRates(selectedCurrencyCode: String, baseCurrencyCode: String, allRates: Map<String, Double>) {
        val selected = selectedCurrencyCode.uppercase()
        val base = baseCurrencyCode.uppercase()

        val rateToSelected = allRates[selected] ?: 1.0
        val rateToBase = allRates[base] ?: 1.0

        val rate = rateToSelected / rateToBase

        _exchangeRate.postValue(String.format("%.3f", rate))
        _inverseRate.postValue(String.format("%.3f", if (rate != 0.0) 1.0 / rate else 0.0))
    }
}