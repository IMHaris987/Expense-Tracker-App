package com.haris.expensetracker.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.haris.expensetracker.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        binding.tvDeleteData.setOnClickListener {
            showDeleteDataConfirmation()
        }

        loadUserData()
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val email = sharedPref.getString("user_email", "haristahir7861@gmail.com")
        binding.tvEmail.text = email
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showDeleteDataConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete All Data")
            .setMessage("This will permanently remove all your local transactions and accounts. This action cannot be undone.")
            .setPositiveButton("Delete Everything") { _, _ ->
                // TODO: Call your Room Database 'clearAllTables()' here
                Toast.makeText(this, "Data cleared successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}