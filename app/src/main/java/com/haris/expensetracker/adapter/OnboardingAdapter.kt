package com.haris.expensetracker.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.haris.expensetracker.model.OnboardingItem
import com.haris.expensetracker.ui.OnboardingFragment

class OnboardingAdapter(
    fragmentActivity: FragmentActivity,
    private val items: List<OnboardingItem>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingFragment.newInstance(items[position])
    }
}
