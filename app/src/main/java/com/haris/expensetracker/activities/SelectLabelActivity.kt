package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivitySelectLabelBinding
import com.haris.expensetracker.utils.ConfirmationDialogeHelper

class SelectLabelActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectLabelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLabelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        binding.btnSave.setOnClickListener {
            val labelName = binding.etLabelName.text.toString()
            if (labelName.isNotEmpty()) {
                Toast.makeText(this, "Label $labelName saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.inputNameLayout.error = "Please enter a name"
            }
        }
    }
    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }
}