package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityNewCurrencyBinding
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.room.Currency
import com.haris.expensetracker.ui.newcurrency.NewCurrencyViewModel
import com.haris.expensetracker.ui.newcurrency.NewCurrencyViewModelFactory
import com.haris.expensetracker.utils.ConfirmationDialogeHelper

class NewCurrencyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewCurrencyBinding
    private lateinit var viewModel: NewCurrencyViewModel
    // Holds the rates retrieved from your API service
    private var currentRatesMap: Map<String, Double> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = NewCurrencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NewCurrencyViewModel::class.java]

        setupObservers()
        setupListeners()
        setupDropDowns()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { showExitDialog() }
        })
    }

    private fun setupObservers() {
        // Observe ViewModel updates and apply them to the EditText fields
        viewModel.exchangeRate.observe(this) { rate ->
            binding.inputRate.editText?.setText(rate)
        }

        viewModel.inverseRate.observe(this) { invRate ->
            binding.inputInverseRate.editText?.setText(invRate)
        }
    }

    private fun setupListeners() {
        binding.btnClose.setOnClickListener { showExitDialog() }

        binding.btnSave.setOnClickListener {
            val code = binding.inputCode.editText?.text.toString()
            val symbol = binding.inputSymbol.editText?.text.toString()
            val rate = binding.inputRate.editText?.text.toString().toDoubleOrNull() ?: 1.0
            val inverseRate = binding.inputInverseRate.editText?.text.toString().toDoubleOrNull() ?: 1.0

            if (code.isNotEmpty() && symbol.isNotEmpty()) {
                viewModel.saveCurrency(Currency(code, "", symbol, rate, inverseRate))
                Toast.makeText(this, "Currency Added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please fill Code and Symbol", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRefresh.setOnClickListener {
            val baseCurrency = binding.autoCompleteCurrency.text.toString()
            val targetCode = binding.inputCode.editText?.text.toString().uppercase().trim()

            if (baseCurrency == "None" || baseCurrency.isEmpty()) {
                Toast.makeText(this, "Select a base currency", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fetchAutomaticRates(baseCurrency, targetCode)
        }
    }

    private fun setupDropDowns() {
        val currencyData = mapOf(
            "PKR" to Pair("Rs", "PKR"),
            "USD" to Pair("$", "USD"),
            "EUR" to Pair("€", "EUR")
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencyData.keys.toList())
        binding.autoCompleteCurrency.setAdapter(adapter)

        binding.autoCompleteCurrency.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()
            currencyData[selected]?.let { info ->
                binding.inputSymbol.editText?.setText(info.first)
                binding.inputCode.editText?.setText(info.second)

                // Use local map if available, otherwise fetch from internet
                if (currentRatesMap.isNotEmpty()) {
                    viewModel.updateRates(info.second, "USD", currentRatesMap)
                } else {
                    fetchAutomaticRates("USD", info.second)
                }
            }
        }
    }

    private fun fetchAutomaticRates(base: String, target: String) {
        viewModel.refreshRates(base, target) { fetchedRate ->
            runOnUiThread {
                if (fetchedRate != null) {
                    binding.inputRate.editText?.setText(String.format("%.4f", fetchedRate))
                    binding.inputInverseRate.editText?.setText(String.format("%.4f", 1.0 / fetchedRate))
                } else {
                    Toast.makeText(this, "Fetch failed. Check connection.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) { finish() }
    }
}