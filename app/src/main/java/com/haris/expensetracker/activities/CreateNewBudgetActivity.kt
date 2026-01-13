package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityCreateNewBudgetBinding
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.ui.budget.BudgetViewModel
import com.haris.expensetracker.ui.budget.BudgetViewModelFactory
import com.haris.expensetracker.utils.ConfirmationDialogeHelper

class CreateNewBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewBudgetBinding
    private lateinit var viewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = BudgetViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BudgetViewModel::class.java]

        setupNavigation()
        setupButtons()
        setupDropdowns()
    }

    private fun setupNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        })

        binding.btnClose.setOnClickListener { showExitDialog() }

        binding.btnSave.setOnClickListener {
            val name = binding.inputName.editText?.text.toString()
            val amount = binding.inputAmount.editText?.text.toString()
            val period = binding.inputPeriod.editText?.text.toString()
            val category = binding.inputCategories.editText?.text.toString()

            val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (validate(name, amount)) {
                viewModel.saveBudget(uid, name, amount.toDouble(), period, category)
                Toast.makeText(this, "Budget Saved Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupDropdowns() {

        val periods = listOf("None", "Daily", "Weekly", "Monthly", "Yearly")
        val periodAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, periods)
        (binding.inputPeriod.editText as? android.widget.AutoCompleteTextView)?.setAdapter(periodAdapter)

        val currencies = listOf("PKR", "USD", "EUR", "GBP", "INR")
        val currencyAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, currencies)
        (binding.inputCurrency.editText as? android.widget.AutoCompleteTextView)?.setAdapter(currencyAdapter)

        val category = listOf("All", "Food", "Transport", "Utilities", "Entertainment")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, category)
        (binding.inputCategories.editText as? android.widget.AutoCompleteTextView)?.setAdapter(categoryAdapter)

        val accounts = listOf("All", "Cash", "Bank Account", "Credit Card")
        val accountAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, accounts)
        (binding.inputAccount.editText as? android.widget.AutoCompleteTextView)?.setAdapter(accountAdapter)
    }

    private fun setupButtons() {
        binding.btnAddLabel.setOnClickListener {
            startActivity(Intent(this, AddLabelActivity::class.java))
        }
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }

    private fun validate(name: String, amount: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            binding.inputName.error = "Name is required"
            isValid = false
        } else {
            binding.inputName.error = null
        }

        if (amount.isBlank()) {
            binding.inputAmount.error = "Amount is required"
            isValid = false
        } else {
            binding.inputAmount.error = null
        }

        return isValid
    }
}