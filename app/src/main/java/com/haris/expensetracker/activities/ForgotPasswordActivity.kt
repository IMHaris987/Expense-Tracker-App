package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.databinding.ActivityForgotPasswordBinding
import com.haris.expensetracker.ui.viewmodel.ForgotPassword
import com.haris.expensetracker.ui.viewmodel.ForgotPasswordViewModel
import com.haris.expensetracker.ui.viewmodel.ForgotPasswordViewModelFactory

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository(FirebaseAuth.getInstance())
        val factory = ForgotPasswordViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ForgotPasswordViewModel::class.java)

        observeViewModel()

        with(binding) {
            buttonBack.setOnClickListener {
                onBackPressed()
            }

            buttonReset.setOnClickListener {
                val emailText = binding.edittextEmail.text.toString().trim()

                if (emailText.isEmpty()) {
                    Toast.makeText(this@ForgotPasswordActivity, "Enter your email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.ForgotPassword(emailText)
            }
            textviewLogin.setOnClickListener {
                startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
    private fun observeViewModel() {
        viewModel.forgetPasswordState.observe(this) { state ->
            when (state) {
                is ForgotPassword.Idle -> {

                }
                is ForgotPassword.Loading -> {
                    Toast.makeText(this@ForgotPasswordActivity, "Proceeding Your Request...", Toast.LENGTH_SHORT).show()
                }
                is ForgotPassword.Success -> {
                    Toast.makeText(this@ForgotPasswordActivity, "Please check your email.", Toast.LENGTH_SHORT).show()
                }
                is ForgotPassword.Error -> {
                    Toast.makeText(this@ForgotPasswordActivity, state.message ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}