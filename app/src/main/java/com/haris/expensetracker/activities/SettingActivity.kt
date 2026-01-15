package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haris.expensetracker.adapter.AccountSettingAdapter
import com.haris.expensetracker.databinding.ActivitySettingBinding
import com.haris.expensetracker.room.AppDatabase
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var accountAdapter: AccountSettingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadAccounts()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.floatingActionButtonAdd.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        accountAdapter = AccountSettingAdapter(emptyList()) { account ->
            Toast.makeText(this, "Account clicked: ${account.name}", Toast.LENGTH_SHORT).show()
        }

        binding.rvAllAccounts.apply {
            layoutManager = LinearLayoutManager(this@SettingActivity)
            adapter = accountAdapter
        }
    }

    private fun loadAccounts() {
        val database = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            database.FinanceDao().getAllAccounts().collect { accounts ->
                accountAdapter.submitList(accounts)
            }
        }
    }
}