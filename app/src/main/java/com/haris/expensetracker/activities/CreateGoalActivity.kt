package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.haris.expensetracker.R
import com.haris.expensetracker.adapter.GoalIconsAdapter
import com.haris.expensetracker.databinding.ActivityCreateGoalBinding
import com.haris.expensetracker.model.GoalIconsCategory
import com.haris.expensetracker.utils.ConfirmationDialogeHelper

class CreateGoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGoalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGoalBinding.inflate(layoutInflater)
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

        binding.createGoal.setOnClickListener {
            val nameText = binding.name.text.toString().trim()
            if (nameText.isEmpty()) {
                Toast.makeText(this@CreateGoalActivity , "Name is Required", Toast.LENGTH_SHORT).show()
            } else{
                val intent = Intent(this@CreateGoalActivity, AddGoalActivity::class.java)
                intent.putExtra("Goal_Name", nameText)
                startActivity(intent)
                finish()
            }
        }

        val goalsRecyclerView = binding.rvGoals

        val categories = listOf(
            GoalIconsCategory("New Vehicle", R.drawable.ic_car, "#4DD0E1"),
            GoalIconsCategory("NEW HOME", R.drawable.ic_home, "#FFB74D"),
            GoalIconsCategory("HOLIDAY TRIP", R.drawable.ic_trip, "#66BB6A"),
            GoalIconsCategory("EDUCATION", R.drawable.ic_education, "#42A5F5"),
            GoalIconsCategory("EMERGENCY FUND", R.drawable.ic_safe, "#BA68C8"),
            GoalIconsCategory("HEALTH CARE", R.drawable.ic_health, "#EF5350")
        )

        goalsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        goalsRecyclerView.adapter = GoalIconsAdapter(categories)
    }

    private fun showExitDialog() {
        ConfirmationDialogeHelper.showConfirmationDialog(this) {
            finish()
        }
    }
}