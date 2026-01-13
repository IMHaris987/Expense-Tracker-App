package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.FinanceRepository
import com.haris.expensetracker.databinding.ActivityAddGoalBinding
import com.haris.expensetracker.room.AppDatabase
import com.haris.expensetracker.ui.goal.GoalViewModel
import com.haris.expensetracker.ui.goal.GoalViewModelFactory
import com.haris.expensetracker.utils.ConfirmationDialogeHelper
import com.haris.expensetracker.utils.DateHelper

class AddGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private var selectedDate: String = ""
    private lateinit var viewModel: GoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase.getDatabase(this)
        val repository = FinanceRepository(database.FinanceDao())
        val factory = GoalViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[GoalViewModel::class.java]

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.btnClose.setOnClickListener {
            showExitDialog()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.inputName.editText?.text.toString()
            val targetAmount = binding.inputTarget.text.toString().toDoubleOrNull() ?: 0.0
            val savedAmount = binding.inputSavedAlready.text.toString().toDoubleOrNull() ?: 0.0
            val note = binding.inputNote.editText?.text.toString()

            val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (validate(name, targetAmount, savedAmount)) {
                viewModel.saveGoals(
                    userId = userId,
                    name = name,
                    targetAmount = targetAmount,
                    savedAmount = savedAmount,
                    desiredDate = selectedDate,
                    note = note
                )
                Toast.makeText(this, "Goals Saved Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.dateAutoComplete.setOnClickListener {
            DateHelper.showDatePicker(this) { date ->
                selectedDate = date
                binding.dateAutoComplete.setText(date, false)
            }
        }
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }

    private fun validate(name: String, targetAmount: Double, savedAmount: Double): Boolean {
        var isValid = true

        if (name.isBlank()) {
            binding.inputName.error = "Name is required"
            isValid = false
        } else {
            binding.inputName.error = null
        }

        if (targetAmount <= 0) {
            binding.inputTarget.error = "Target amount must be greater than zero"
            isValid = false
        } else {
            binding.inputTarget.error = null
        }

        if (savedAmount > targetAmount) {
            binding.inputSavedAlready.error = "Saved amount cannot exceed target"
            isValid = false
        } else {
            binding.inputSavedAlready.error = null
        }

        return isValid
    }
}