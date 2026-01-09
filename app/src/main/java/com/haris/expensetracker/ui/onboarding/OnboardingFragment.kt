package com.haris.expensetracker.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.haris.expensetracker.R
import com.haris.expensetracker.model.OnboardingItem

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val image = view.findViewById<ImageView>(R.id.imageView)
        val title = view.findViewById<TextView>(R.id.textview_title)
        val desc = view.findViewById<TextView>(R.id.textview_description)

        arguments?.let {
            title.text = it.getString("title")
            desc.text = it.getString("desc")
            image.setImageResource(it.getInt("image"))
        }
    }

    companion object {
        fun newInstance(item: OnboardingItem): OnboardingFragment {
            val fragment = OnboardingFragment()
            fragment.arguments = Bundle().apply {
                putString("title", item.title)
                putString("desc", item.description)
                putInt("image", item.imageRes)
            }
            return fragment
        }
    }
}