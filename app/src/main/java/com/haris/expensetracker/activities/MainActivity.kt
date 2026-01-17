package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.haris.expensetracker.R
import com.haris.expensetracker.adapter.AccountAdapter
import com.haris.expensetracker.adapter.TransactionAdapter
import com.haris.expensetracker.databinding.ActivityMainBinding
import com.haris.expensetracker.ui.budgetandgoals.BudgetsGoalsFragment
import com.haris.expensetracker.ui.home.HomeViewModel
import com.haris.expensetracker.ui.home.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var accountAdapter: AccountAdapter
    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = com.haris.expensetracker.room.AppDatabase.getDatabase(this)
        val repository = com.haris.expensetracker.data.repository.FinanceRepository(database.FinanceDao())
        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        transactionAdapter = TransactionAdapter()
        binding.rvRecentTransactions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }

        accountAdapter = AccountAdapter(emptyList())
        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = accountAdapter
        }

        setupAccountObservers()
        setupObservers()
        setupNavigationHeader()
        setupTabs()
        initFabMenuListeners()
        setupNavigationDrawer()
    }

    private fun setupAccountObservers() {
        homeViewModel.allAccounts.observe(this) { accounts ->
            if (!accounts.isNullOrEmpty()) {
                accountAdapter.submitList(accounts)
            }
        }

        // Observer for Balance
        homeViewModel.totalBalance.observe(this) { balance ->
            binding.tvTotalBalance.text = "Total: PKR ${String.format("%.2f", balance)}"
        }

        // Observer for Monthly Expense
        homeViewModel.monthlyExpense.observe(this) { expense ->
            binding.tvExpenseAmount.text = "PKR ${expense.toInt()}"
        }

        // Observer for Recent Transactions
        // Note: Ensure your ViewModel variable is named 'recentTransactions' (plural)
        homeViewModel.recentTransaction.observe(this) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    private fun setupObservers() {
        homeViewModel.totalBalance.observe(this) { balance ->
            binding.tvTotalBalance.text = "Total: PKR ${String.format("%.2f", balance)}"
        }

        homeViewModel.monthlyExpense.observe(this) { expense ->
            binding.tvExpenseAmount.text = "PKR ${expense.toInt()}"
        }

        homeViewModel.recentTransaction.observe(this) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> showLogoutConfirmationDialog()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun showLogoutConfirmationDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Logout")
            .setMessage("Are you sure you want to log out?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .show()
    }

    private fun performLogout() {
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupNavigationHeader() {
        binding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnNotification.setOnClickListener {
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
        }

        binding.btnAccountSettings.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showAccountsTab()
                    1 -> showBudgetsGoalsTab()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showAccountsTab() {
        binding.accounts.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }

    private fun showBudgetsGoalsTab() {
        binding.accounts.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, BudgetsGoalsFragment())
            .commit()
    }

    private fun initFabMenuListeners() {
        binding.floatingActionButtonAdd.setOnClickListener {
            if (!isFabMenuOpen) showFabMenu() else closeFabMenu()
        }

        binding.fabOverlay.setOnClickListener { closeFabMenu() }

        binding.btnNewRecord.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
            closeFabMenu()
        }

        binding.btnAddAccountCard.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
            closeFabMenu()
        }
    }

    private fun showFabMenu() {
        isFabMenuOpen = true
        binding.fabMenuContainer.visibility = View.VISIBLE
        binding.fabOverlay.visibility = View.VISIBLE
        binding.fabOverlay.animate().alpha(1f).setDuration(300).start()
        binding.fabMenuContainer.animate().translationY(0f).alpha(1f).setDuration(300).start()
        binding.floatingActionButtonAdd.animate().rotation(135f).setDuration(300).start()
    }

    private fun closeFabMenu() {
        isFabMenuOpen = false
        binding.fabOverlay.animate().alpha(0f).setDuration(300).withEndAction {
            binding.fabOverlay.visibility = View.GONE
        }.start()
        binding.fabMenuContainer.animate().translationY(100f).alpha(0f).setDuration(300).withEndAction {
            binding.fabMenuContainer.visibility = View.GONE
        }.start()
        binding.floatingActionButtonAdd.animate().rotation(0f).setDuration(300).start()
    }
}