package com.haris.expensetracker.activities

import android.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewCurrencyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = NewCurrencyViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[NewCurrencyViewModel::class.java]

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }

        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.btnClose.setOnClickListener {
            showExitDialog()
        }

        binding.btnSave.setOnClickListener {
            val code = binding.inputCode.editText?.text.toString()
            val symbol = binding.inputSymbol.editText?.text.toString()

            val rate = binding.inputRate.editText?.text.toString().toDoubleOrNull() ?: 1.0
            val inverseRate = binding.inputInverseRate.editText?.text.toString().toDoubleOrNull() ?: 1.0

            if (code.isNotEmpty() && symbol.isNotEmpty()) {
                val newCurrency = Currency(
                    code = code,
                    name = "",
                    symbol = symbol,
                    rate = rate,
                    inverseRate = inverseRate
                )

                viewModel.saveCurrency(newCurrency)
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
                Toast.makeText(this, "Please select a base currency", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (targetCode.isEmpty()) {
                binding.inputCode.error = "Enter target code (e.g. PKR)"
                return@setOnClickListener
            }

            Toast.makeText(this, "Fetching latest rates...", Toast.LENGTH_SHORT).show()

            viewModel.refreshRates(baseCurrency, targetCode) { fetchedRate ->
                runOnUiThread {
                    if (fetchedRate != null) {
                        binding.inputRate.editText?.setText(String.format("%.4f", fetchedRate))

                        val inverse = 1.0 / fetchedRate
                        binding.inputInverseRate.editText?.setText(String.format("%.4f", inverse))

                        Toast.makeText(this, "Rates updated!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to fetch rates. Check code or internet.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        setupDropDowns()
    }

    private fun setupDropDowns() {
        val currencyData = mapOf(
            "PKR" to Pair("Rs", "PKR"),
            "USD" to Pair("$", "USD"),
            "EUR" to Pair("€", "EUR"),
            "GBP" to Pair("£", "GBP")
        )

        val currencies = currencyData.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencies)
        binding.autoCompleteCurrency.setAdapter(adapter)

        binding.autoCompleteCurrency.setOnItemClickListener { parent, _, position, _ ->
            val selected = parent.getItemAtPosition(position).toString()

            val info = currencyData[selected]
            if (info != null) {
                binding.inputSymbol.editText?.setText(info.first)
                binding.inputCode.editText?.setText(info.second)

                fetchAutomaticRates(base = "USD", target = info.second)
            }
        }
    }

    private fun fetchAutomaticRates(base: String, target: String) {
        viewModel.refreshRates(base, target) { fetchedRate ->
            runOnUiThread {
                if (fetchedRate != null) {
                    binding.inputRate.editText?.setText(String.format("%.4f", fetchedRate))
                    binding.inputInverseRate.editText?.setText(String.format("%.4f", 1.0 / fetchedRate))
                }
            }
        }
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }
}