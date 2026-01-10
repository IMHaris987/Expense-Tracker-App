package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityCreateNewBudgetBinding
import com.haris.expensetracker.utils.ConfirmationDialogeHelper


class CreateNewBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNewBudgetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupButtons()
        setupDropdowns()
    }

    private fun setupToolbar() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.btnSave.setOnClickListener {
            saveBudget()
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

    private fun saveBudget() {
        val name = binding.inputName.editText?.text.toString()
        val period = binding.inputPeriod.editText?.text.toString()
        val amount = binding.inputAmount.editText?.text.toString()
        val currency = binding.inputCurrency.editText?.text.toString()

        if (name.isBlank()) {
            binding.inputName.error = "Name is required"
            return
        }

        Toast.makeText(this, "Budget '$name' saved!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }
}