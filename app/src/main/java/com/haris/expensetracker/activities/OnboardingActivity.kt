package com.haris.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.haris.expensetracker.R
import com.haris.expensetracker.adapter.OnboardingAdapter
import com.haris.expensetracker.model.OnboardingItem

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var dotsLayout: LinearLayout
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
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btnNext)
        dotsLayout = findViewById(R.id.dotsLayout)

        adapter = OnboardingAdapter(this, items)
        viewPager.adapter = adapter

        setupDots()
        animateDot(currentDotIndex, true)

        // Update dots when page changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                animateDot(currentDotIndex, false)
                animateDot(position, true)
                currentDotIndex = position
            }
        })

        btnNext.setOnClickListener {
            getSharedPreferences("onboarding", MODE_PRIVATE).edit()
                .putBoolean("completed", true)
                .apply()

            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }

    private fun setupDots() {
        val dots = Array(items.size) { TextView(this) }
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i].text = "•"
            dots[i].textSize = 50f // big dots
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.dot_inactive))
            dotsLayout.addView(dots[i])
        }
    }

    private fun animateDot(index: Int, active: Boolean) {
        if (index < 0 || index >= dotsLayout.childCount) return
        val dot = dotsLayout.getChildAt(index) as TextView

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
