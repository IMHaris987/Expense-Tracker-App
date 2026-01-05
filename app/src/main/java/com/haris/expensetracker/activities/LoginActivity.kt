package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.haris.expensetracker.data.repository.AuthRepository
import com.haris.expensetracker.databinding.ActivityLoginBinding
import com.haris.expensetracker.ui.viewmodel.LoginState
import com.haris.expensetracker.ui.viewmodel.LoginViewModel
import com.haris.expensetracker.ui.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Initialize ViewModel using the Factory
        val repository = AuthRepository(FirebaseAuth.getInstance())
        val factory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // 2. Observe the loginState LiveData
        observeViewModel()

        with(binding) {

            buttonBack.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            buttonLogin.setOnClickListener {
                val email = textinputlayoutEmail.editText?.text.toString().trim()
                val password = textinputlayoutPassword.editText?.text.toString().trim()

                // 3. Input validation remains in the View (UI logic)
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // 4. Call the ViewModel function (Business logic is now in ViewModel/Repository)
                viewModel.login(email, password)
            }

            textviewRegisterLink.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            textviewForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        }
    }

    // 5. Separate function to handle all LiveData observation
    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Idle -> {
                    // Hide progress bar (if visible)
                }
                is LoginState.Loading -> {
                    // Show progress bar (e.g., binding.progressBar.visibility = View.VISIBLE)
                    Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
                }
                is LoginState.Success -> {
                    // Hide progress bar
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, IntroductionActivity::class.java))
                    finish()
                }
                is LoginState.Error -> {
                    // Hide progress bar
                    Toast.makeText(this, state.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
