package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityLoginBinding
import com.haris.expensetracker.utils.FirebaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()

        with(binding) {

            buttonBack.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            buttonLogin.setOnClickListener {

                val email = textinputlayoutEmail.editText?.text.toString().trim()
                val password = textinputlayoutPassword.editText?.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                firebaseHelper.login(email, password) { success, error ->
                    if (success) {
                        startActivity(
                            Intent(this@LoginActivity, IntroductionActivity::class.java)
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            error ?: "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            textviewRegisterLink.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                finish()
            }

            textviewForgotPassword.setOnClickListener {
                startActivity(
                    Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                )
            }

        }
    }
}
