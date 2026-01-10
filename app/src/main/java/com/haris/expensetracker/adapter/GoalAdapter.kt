package com.haris.expensetracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemGoalCategoryBinding
import com.haris.expensetracker.model.GoalCategory

class GoalAdapter(private var list: List<GoalCategory>) :
    RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemGoalCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoalCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.apply {
            txtGoalTitle.text = item.title
            imgIcon.setImageResource(item.iconRes)
            iconContainer.setCardBackgroundColor(Color.parseColor(item.colorHex))
        }
    }

    override fun getItemCount(): Int = list.size
}