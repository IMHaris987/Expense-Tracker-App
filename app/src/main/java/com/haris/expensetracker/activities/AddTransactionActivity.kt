package com.haris.expensetracker.activities

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haris.expensetracker.R
import com.haris.expensetracker.databinding.ActivityAddTransactionBinding
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.room.TransactionEntity
import com.haris.expensetracker.utils.ConfirmationDialogeHelper
import com.haris.expensetracker.utils.DateHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private var accountList = listOf<Account>()

    // Default to today's date formatted correctly
    private var selectedDateString: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    private var selectedType = "Expense"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val dao = database.FinanceDao()

        setupTypeToggles()

        // Set initial date text
        binding.tvDateDisplay.text = selectedDateString

        // 1. Setup Categories
        val categories = listOf("Food", "Fuel", "Rent", "Shopping", "Salary", "Transport", "Entertainment")
        binding.spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        // 2. Setup Accounts from Room
        lifecycleScope.launch {
            dao.getAllAccounts().collect { accounts ->
                accountList = accounts
                if (accounts.isNotEmpty()) {
                    val names = accounts.map { it.name }
                    val adapter = ArrayAdapter(
                        this@AddTransactionActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        names
                    )
                    binding.spinnerAccount.adapter = adapter
                    binding.spinnerTargetAccount.adapter = adapter
                }
            }
        }

        // 3. Close Button with Confirmation Dialog
        binding.btnClose.setOnClickListener {
            ConfirmationDialogeHelper.showConfirmationDialog(this) {
                finish()
            }
        }

        binding.btnSaveTransaction.setOnClickListener {
            saveTransaction(dao)
        }

        // 4. Date Picker Implementation
        binding.btnPickDate.setOnClickListener {
            DateHelper.showDatePicker(this) { date ->
                selectedDateString = date
                // Update the TextView inside the LinearLayout
                binding.tvDateDisplay.text = date
            }
        }
    }

    private fun setupTypeToggles() {
        binding.toggleGroupType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnTypeExpense -> {
                        selectedType = "Expense"
                        binding.layoutToAccount.visibility = View.GONE
                        binding.layoutCategory.visibility = View.VISIBLE
                        binding.tvFromAccountLabel.text = "Account"
                    }
                    R.id.btnTypeIncome -> {
                        selectedType = "Income"
                        binding.layoutToAccount.visibility = View.GONE
                        binding.layoutCategory.visibility = View.VISIBLE
                        binding.tvFromAccountLabel.text = "Deposit To"
                    }
                    R.id.btnTypeTransfer -> {
                        selectedType = "Transfer"
                        binding.layoutToAccount.visibility = View.VISIBLE
                        binding.layoutCategory.visibility = View.GONE
                        binding.tvFromAccountLabel.text = "From Account"
                    }
                }
            }
        }
    }

    private fun saveTransaction(dao: com.haris.expensetracker.room.FinanceDao) {
        val amount = binding.etAmount.text.toString().toDoubleOrNull()
        val note = binding.etNote.text.toString()

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Enter valid amount", Toast.LENGTH_SHORT).show()
            return
        }
        if (accountList.isEmpty()) {
            Toast.makeText(this, "No accounts found", Toast.LENGTH_SHORT).show()
            return
        }

        val sourceAccount = accountList[binding.spinnerAccount.selectedItemPosition]
        var targetAccountId: Long? = null
        var categoryName = binding.spinnerCategory.selectedItem.toString()

        if (selectedType == "Transfer") {
            val targetAccount = accountList[binding.spinnerTargetAccount.selectedItemPosition]
            if (sourceAccount.id == targetAccount.id) {
                Toast.makeText(this, "Cannot transfer to same account", Toast.LENGTH_SHORT).show()
                return
            }
            targetAccountId = targetAccount.id
            categoryName = "Transfer"
        }

        // Convert the String date back to a Date object for Room
        val dateObject = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDateString) ?: Date()

        lifecycleScope.launch {
            val transaction = TransactionEntity(
                amount = amount,
                note = note,
                date = dateObject, // Passed as Date object
                type = selectedType,
                accountId = sourceAccount.id,
                targetAccountId = targetAccountId,
                categoryName = categoryName
            )

            dao.processTransaction(transaction)
            Toast.makeText(this@AddTransactionActivity, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}