package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityAddGoalBinding
import com.haris.expensetracker.utils.ConfirmationDialogeHelper
import com.haris.expensetracker.utils.DateHelper

class AddGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGoalBinding
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val target = binding.inputTarget.text.toString()
            val savedAlready = binding.inputSavedAlready.text.toString()
            val note = binding.inputNote.editText?.text.toString()

            val targetAmount = target.toDoubleOrNull() ?: 0.0
            val savedAmount = savedAlready.toDoubleOrNull() ?: 0.0

            if (name.isEmpty()) {
                binding.inputName.error = "Name is required"
                return@setOnClickListener
            }

            if (targetAmount <= 0) {
                binding.inputTarget.error = "Target amount must be greater than zero"
                return@setOnClickListener
            }

            if (savedAmount > targetAmount) {
                binding.inputTarget.error = null
                binding.inputSavedAlready.error = "Saved amount cannot exceed target"
                Toast.makeText(this, "Check your amounts!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
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
}