package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.haris.expensetracker.adapter.TransactionAdapter
import com.haris.expensetracker.databinding.ActivityMainBinding
import com.haris.expensetracker.ui.budgetandgoals.BudgetsGoalsFragment
import com.haris.expensetracker.ui.home.HomeViewModel
import com.haris.expensetracker.ui.home.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var transactionAdapter: TransactionAdapter
    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = com.haris.expensetracker.room.AppDatabase.getDatabase(this)
        val repository = com.haris.expensetracker.data.repository.FinanceRepository(database.FinanceDao())

        val factory = HomeViewModelFactory(repository)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        setupNavigationHeader()
        setupTabs()

        initFabMenuListeners()

        transactionAdapter = TransactionAdapter()
        binding.rvRecentTransactions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }

        setupObservers()
    }

    private fun setupObservers() {
        homeViewModel.totalBalance.observe(this) { balance ->
            binding.tvTotalBalance.text = "PKR ${String.format("%.2f", balance)}"
        }

        homeViewModel.monthlyExpense.observe(this) { expense ->
            binding.tvExpenseAmount.text = "PKR ${expense.toInt()}"
        }

        homeViewModel.recentTransactions.observe(this) { transactions ->
            transactionAdapter.submitList(transactions)
        }
    }

    private fun setupNavigationHeader() {
        binding.btnDrawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnNotification.setOnClickListener {
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
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

        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
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
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
            closeFabMenu()
        }

        binding.btnAddAccountCard.setOnClickListener {
            startActivity(Intent(this, AddAccountActivity::class.java))
        }

        binding.btnCreateTemplate.setOnClickListener {
            Toast.makeText(this, "Template Clicked", Toast.LENGTH_SHORT).show()
            closeFabMenu()
        }
    }

    private fun showFabMenu() {
        isFabMenuOpen = true
        binding.fabMenuContainer.visibility = View.VISIBLE
        binding.fabOverlay.visibility = View.VISIBLE
        binding.fabOverlay.alpha = 0f

        binding.fabOverlay.animate().alpha(1f).setDuration(300).start()

        binding.fabMenuContainer.alpha = 0f
        binding.fabMenuContainer.translationY = 100f
        binding.fabMenuContainer.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(300)
            .start()

        binding.floatingActionButtonAdd.animate().rotation(135f).setDuration(300).start()
        binding.floatingActionButtonAdd.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
    }

    private fun closeFabMenu() {
        isFabMenuOpen = false

        binding.fabOverlay.animate().alpha(0f).setDuration(300).withEndAction {
            binding.fabOverlay.visibility = View.GONE
        }.start()

        binding.fabMenuContainer.animate()
            .translationY(100f)
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                binding.fabMenuContainer.visibility = View.GONE
            }.start()

        binding.floatingActionButtonAdd.animate().rotation(0f).setDuration(300).start()
        binding.floatingActionButtonAdd.setImageResource(android.R.drawable.ic_input_add)
    }
}