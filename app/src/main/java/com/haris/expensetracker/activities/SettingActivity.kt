package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haris.expensetracker.adapter.AccountSettingAdapter
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivitySettingBinding
import com.haris.expensetracker.room.Account
import com.haris.expensetracker.room.AppDatabase
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.ui.accountsetting.AccountSettingViewModel
import com.haris.expensetracker.ui.accountsetting.AccountSettingViewModelFactory

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var accountAdapter: AccountSettingAdapter
    private lateinit var accountSettingViewModel: AccountSettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = AccountSettingViewModelFactory(repository)

        accountSettingViewModel = ViewModelProvider(this, factory)[AccountSettingViewModel::class.java]

        setupRecyclerView()
        observeAccounts()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.floatingActionButtonAdd.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        accountAdapter = AccountSettingAdapter(
            accounts = mutableListOf(),
            onEditClick = { account ->
                val intent = Intent(this, AddAccountActivity::class.java)
                intent.putExtra("account_id", account.id) // Pass ID for prefilling
                startActivity(intent)
            }
        )
        binding.rvAllAccounts.layoutManager = LinearLayoutManager(this)
        binding.rvAllAccounts.adapter = accountAdapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, // Drag directions
            ItemTouchHelper.LEFT // Swipe direction
        ) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val from = vh.adapterPosition
                val to = target.adapterPosition
                accountAdapter.onItemMove(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val account = accountAdapter.getAccountAt(position)
                showDeleteDialog(account)
                accountAdapter.notifyItemChanged(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvAllAccounts)
    }

    private fun observeAccounts() {
        accountSettingViewModel.allAccounts.observe(this) { accounts ->
            accountAdapter.submitList(accounts)
        }
    }

    private fun showDeleteDialog(account: Account) {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete ${account.name}?")
            .setPositiveButton("Delete") { _, _ ->
                accountSettingViewModel.deleteAccount(account.id)
                Toast.makeText(this, "Account Deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}