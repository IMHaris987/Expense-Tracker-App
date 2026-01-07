package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.haris.expensetracker.R
import com.haris.expensetracker.adapter.OnboardingAdapter
import com.haris.expensetracker.databinding.ActivityOnboardingBinding
import com.haris.expensetracker.model.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    private val items = listOf(
        OnboardingItem(
            "Track Your Spending",
            "Track and analyze your spending immediately and automatically through our bank connection.",
            R.drawable.ic_track_expenses
        ),
        OnboardingItem(
            "Save Money",
            "Analyze spending habits and save more money",
            R.drawable.ic_save_money
        ),
        OnboardingItem(
            "Budget your money",
            "Build healthy financial habits. Control unnecessary expenses",
            R.drawable.budget
        ),
        OnboardingItem(
            "Set Up Your Goals",
            "Track and follow what matters to you. Save for important things.",
            R.drawable.goal
        )
    )

    private var currentDotIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OnboardingAdapter(this, items)
        binding.viewPager.adapter = adapter

        setupDots()
        animateDot(currentDotIndex, true)

        // Page change listener
        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                animateDot(currentDotIndex, false)
                animateDot(position, true)
                currentDotIndex = position
            }
        })

        binding.btnNext.setOnClickListener {
            getSharedPreferences("onboarding", MODE_PRIVATE)
                .edit()
                .putBoolean("completed", true)
                .apply()

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setupDots() {
        binding.dotsLayout.removeAllViews()

        repeat(items.size) {
            val dot = TextView(this).apply {
                text = "•"
                textSize = 50f
                setTextColor(
                    ContextCompat.getColor(
                        this@OnboardingActivity,
                        R.color.dot_inactive
                    )
                )
            }
            binding.dotsLayout.addView(dot)
        }
    }

    private fun animateDot(index: Int, active: Boolean) {
        if (index !in 0 until binding.dotsLayout.childCount) return

        val dot = binding.dotsLayout.getChildAt(index) as TextView

        val scale = if (active) 1.2f else 1f
        val alpha = if (active) 1f else 0.6f
        val translationY = if (active) -5f else 0f

        dot.animate()
            .scaleX(scale)
            .scaleY(scale)
            .alpha(alpha)
            .translationY(translationY)
            .setDuration(250)
            .start()

        dot.setTextColor(
            ContextCompat.getColor(
                this,
                if (active) R.color.dot_active else R.color.dot_inactive
            )
        )
    }
}
