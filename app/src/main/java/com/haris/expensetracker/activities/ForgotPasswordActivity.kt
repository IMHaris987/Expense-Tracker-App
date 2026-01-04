package com.haris.expensetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityForgotPasswordBinding
import com.haris.expensetracker.utils.FirebaseHelper

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()

        with(binding) {
            buttonBack.setOnClickListener {
                onBackPressed()
            }

            buttonBack.setOnClickListener {
                val emailText = binding.edittextEmail.text.toString().trim()

                if (emailText.isEmpty()) {
                    Toast.makeText(this@ForgotPasswordActivity, "Enter your email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                firebaseHelper.passwordForgot(emailText) { success, error ->
                    if (success){
                        Toast.makeText(this@ForgotPasswordActivity, "Check your email", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ForgotPasswordActivity, error ?: "Password reset failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}