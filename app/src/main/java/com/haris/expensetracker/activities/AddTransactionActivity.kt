package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haris.expensetracker.databinding.ActivityAddTransactionBinding
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.room.FinanceDao
import com.haris.expensetracker.room.TransactionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTransactionBinding
    private var accountList = listOf<Account>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val dao = database.FinanceDao()

        // 1. Setup Categories
        val categories = listOf("Food", "Fuel", "Rent", "Shopping", "Salary", "Transport", "Entertainment")
        binding.spinnerCategory.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        // 2. Setup Accounts from Room
        lifecycleScope.launch {
            accountList = dao.getAllAccounts().first()
            if (accountList.isNotEmpty()) {
                val accountNames = accountList.map { it.name }
                binding.spinnerAccount.adapter = ArrayAdapter(
                    this@AddTransactionActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    accountNames
                )
            } else {
                Toast.makeText(this@AddTransactionActivity, "Please add an account first!", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSaveTransaction.setOnClickListener {
            saveTransaction(dao)
        }
    }

    private fun saveTransaction(dao: FinanceDao) {
        val amountStr = binding.etAmount.text.toString()
        val amount = amountStr.toDoubleOrNull()
        val note = binding.etNote.text.toString()

        // Safety check for empty selections
        if (binding.spinnerAccount.selectedItem == null) {
            Toast.makeText(this, "No account selected", Toast.LENGTH_SHORT).show()
            return
        }

        val category = binding.spinnerCategory.selectedItem.toString()
        val selectedAccount = accountList[binding.spinnerAccount.selectedItemPosition]

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = TransactionEntity(
            amount = amount,
            note = note,
            date = Date(),
            type = "Expense",
            accountId = selectedAccount.id,
            categoryName = category
        )

        lifecycleScope.launch {
            // This period must match the format used when creating the Budget
            val currentPeriod = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())

            // This triggers: Insert Transaction -> Update Account Balance -> Update Budget Spent
            dao.recordExpense(transaction, currentPeriod)

            Toast.makeText(this@AddTransactionActivity, "Transaction Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}