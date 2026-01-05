package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.haris.expensetracker.ui.viewmodel.RegisterState
import com.haris.expensetracker.ui.viewmodel.RegisterViewModel
import com.haris.expensetracker.ui.viewmodel.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository(FirebaseAuth.getInstance())
        val factory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(RegisterViewModel::class.java)

        observeViewModel()

        with(binding) {
            buttonBack.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, WelcomeActivity::class.java))
                finish()
            }

            buttonRegister.setOnClickListener {
                val name = textinputlayoutName.editText?.text.toString().trim()
                val email = textinputlayoutEmail.editText?.text.toString().trim()
                val password = textinputlayoutPassword.editText?.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.register(name, email, password)
            }

            textviewLoginLink.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is RegisterState.Idle -> {
                    // Hide progress bar (if visible)
                }
                is RegisterState.Loading -> {
                    Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show()
                }
                is RegisterState.Success -> {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, IntroductionActivity::class.java))
                    finish()
                }
                is RegisterState.Failure -> {
                    Toast.makeText(this, state.message ?: "Register failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
