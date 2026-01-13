package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haris.expensetracker.databinding.ActivityAddAccountBinding
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.AppDatabase
import kotlinx.coroutines.launch

class AddAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val dao = database.FinanceDao()

        binding.btnClose.setOnClickListener { finish() }

        binding.btnSaveAccount.setOnClickListener {
            val name = binding.etAccountName.text.toString().trim()
            val balance = binding.etInitialBalance.text.toString().toDoubleOrNull() ?: 0.0

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter an account name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val newAccount = Account(
                    id = 0,
                    name = name,
                    balance = balance,
                    accountType = "Regular",
                    currency = "PKR"
                )
                dao.insertAccount(newAccount)
                Toast.makeText(this@AddAccountActivity, "Account added!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}