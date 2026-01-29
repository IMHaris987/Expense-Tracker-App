package com.haris.expensetracker.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityAddAccountBinding
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.ui.addaccount.AddAccountViewModel
import com.haris.expensetracker.ui.addaccount.AddAccountViewModelFactory
import com.haris.expensetracker.ui.transaction.TransactionViewModel
import com.haris.expensetracker.ui.transaction.TransactionViewModelFactory
import kotlinx.coroutines.launch

class AddAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAccountBinding
    private lateinit var addAccountViewModel: AddAccountViewModel
    private var currentAccountId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val dao = database.FinanceDao()
        val repository = FinanceRepository(dao)
        val factory = AddAccountViewModelFactory(repository)
        addAccountViewModel = ViewModelProvider(this, factory)[AddAccountViewModel::class.java]

        val accountId = intent.getLongExtra("account_id", -1L)
        if (accountId != -1L) {
            currentAccountId = accountId
            lifecycleScope.launch {
                val account = addAccountViewModel.getAccountById(accountId)
                account?.let {
                    binding.etAccountName.setText(it.name)
                    binding.etInitialBalance.setText(it.balance.toString())
                    binding.autoCompleteCurrency.setText(it.currency, false)
                    binding.btnSaveAccount.text = "Update Account"
                }
            }
        }

        binding.btnClose.setOnClickListener { finish() }

        binding.btnSaveAccount.setOnClickListener {
            val name = binding.etAccountName.text.toString().trim()
            val balanceStr = binding.etInitialBalance.text.toString().trim()
            val selectedCurrency = binding.autoCompleteCurrency.text.toString()

            if (name.isEmpty() || balanceStr.isEmpty()) return@setOnClickListener

            val balance = balanceStr.toDoubleOrNull() ?: 0.0

            val newAccount = Account(
                id = currentAccountId,
                name = name,
                balance = balance,
                currency = selectedCurrency
            )

            addAccountViewModel.saveAccount(newAccount)

            Toast.makeText(this, "Account Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
        setupCurrencyDropdown()
        setupCurrencyObserver()
    }

    private fun setupCurrencyDropdown() {
        val currencies = mutableListOf("PKR", "USD", "EUR", "+ ADD CURRENCY")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, currencies)
        val autoComplete = binding.autoCompleteCurrency

        autoComplete.setAdapter(adapter)

        autoComplete.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            if (selectedItem == "+ ADD CURRENCY") {
                autoComplete.setText("PKR", false)

                val intent = Intent(this, NewCurrencyActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupCurrencyObserver() {
        addAccountViewModel.availableCurrencies.observe(this) { currencies ->

            val currencyList = currencies.map {"${it.code} (${it.symbol})"} .toMutableList()

            currencyList.add("+ ADD CURRENCY")

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencyList)
            binding.autoCompleteCurrency.setAdapter(adapter)
        }

        binding.autoCompleteCurrency.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            if (selectedItem == "+ ADD CURRENCY") {
                startActivity(Intent(this, NewCurrencyActivity::class.java))
            }
        }
    }
}