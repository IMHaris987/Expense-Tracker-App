package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityRegisterBinding
import com.haris.expensetracker.utils.FirebaseHelper

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseHelper = FirebaseHelper()

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
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                firebaseHelper.register(name, email, password) { success, error ->
                    if (success) {
                        startActivity(
                            Intent(this@RegisterActivity, LoginActivity::class.java)
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            error ?: "Registration failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            textviewLoginLink.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}
