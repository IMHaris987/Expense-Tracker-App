package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityAddGoalBinding
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.room.Goals
import com.haris.expensetracker.ui.goal.GoalViewModel
import com.haris.expensetracker.ui.goal.GoalViewModelFactory
import com.haris.expensetracker.utils.ConfirmationDialogeHelper
import com.haris.expensetracker.utils.DateHelper

class AddGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private lateinit var viewModel: GoalViewModel
    private var selectedDate: String = ""
    private var goalId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = GoalViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[GoalViewModel::class.java]

        goalId = intent.getLongExtra("GOAL_ID", -1L)
        if (goalId != -1L) {
            setupEditMode()
        }

        setupNavigation()
        setupDropDowns()
    }

    private fun setupEditMode() {

        viewModel.getGoalById(goalId) { goal ->
            goal?.let {
                runOnUiThread {
                    binding.inputName.editText?.setText(it.name)
                    binding.inputTarget.setText(it.targetAmount.toString())
                    binding.inputSavedAlready.setText(it.savedAmount.toString())
                    binding.dateAutoComplete.setText(it.desiredDate, false)
                    (binding.inputCategories.editText as? AutoCompleteTextView)?.setText(it.categoryName, false)
                    binding.inputNote.editText?.setText(it.note)
                    selectedDate = it.desiredDate
                }
            }
        }
    }

    private fun setupNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { showExitDialog() }
        })

        binding.btnClose.setOnClickListener { showExitDialog() }

        binding.btnSave.setOnClickListener {
            handleSaveOrUpdate()
        }

        binding.dateAutoComplete.setOnClickListener {
            DateHelper.showDatePicker(this) { date ->
                selectedDate = date
                binding.dateAutoComplete.setText(date, false)
            }
        }
    }

    private fun handleSaveOrUpdate() {
        val name = binding.inputName.editText?.text.toString().trim()
        val targetAmount = binding.inputTarget.text.toString().toDoubleOrNull() ?: 0.0
        val savedAmount = binding.inputSavedAlready.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.inputCategories.editText?.text.toString()
        val note = binding.inputNote.editText?.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        if (validate(name, targetAmount, savedAmount)) {
            if (goalId != -1L) {
                val updatedGoal = Goals(
                    id = goalId,
                    userId = userId,
                    name = name,
                    targetAmount = targetAmount,
                    savedAmount = savedAmount,
                    desiredDate = selectedDate,
                    categoryName = category,
                    note = note
                )
                viewModel.updateGoal(updatedGoal)
            } else {
                viewModel.saveGoals(
                    userId = userId,
                    name = name,
                    targetAmount = targetAmount,
                    savedAmount = savedAmount,
                    desiredDate = selectedDate,
                    categoryName = category,
                    note = note
                )
            }
            finish()
        }
    }

    private fun setupDropDowns() {
        val categories = listOf("All", "Food", "Transport", "Utilities", "Entertainment")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        (binding.inputCategories.editText as? AutoCompleteTextView)?.setAdapter(categoryAdapter)
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) { finish() }
    }

    private fun validate(name: String, targetAmount: Double, savedAmount: Double): Boolean {
        var isValid = true

        if (name.isBlank()) {
            binding.inputName.error = "Name is required"
            isValid = false
        } else binding.inputName.error = null

        // Note: targetAmount and savedAmount errors must be applied to the TextInputLayout, not the EditText
        if (targetAmount <= 0) {
            binding.inputTargetLayout.error = "Target must be > 0"
            isValid = false
        } else binding.inputTargetLayout.error = null

        if (savedAmount > targetAmount) {
            binding.inputSavedAlreadyLayout.error = "Saved cannot exceed target"
            isValid = false
        } else binding.inputSavedAlreadyLayout.error = null

        return isValid
    }
}