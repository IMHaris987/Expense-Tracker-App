package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.haris.expensetracker.databinding.ActivityAddLabelBinding

class AddLabelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLabelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLabelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, SelectLabelActivity::class.java))
            showDataState()
        }
    }

    private fun showDataState() {
        binding.emptyStateContainer.visibility = android.view.View.GONE
        binding.recyclerView.visibility = android.view.View.VISIBLE
    }
}