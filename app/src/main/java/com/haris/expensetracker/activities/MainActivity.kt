package com.haris.expensetracker.activities

import BudgetsGoalsFragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.tabs.TabLayout
import com.haris.expensetracker.R
import com.haris.expensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isFabMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationHeader()
        setupTabs()

        initFabMenuListeners()
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
        binding.accountsContent.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE

        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        }
    }

    private fun showBudgetsGoalsTab() {
        binding.accountsContent.visibility = View.GONE
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
            Toast.makeText(this, "New Record Clicked", Toast.LENGTH_SHORT).show()
            closeFabMenu()
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