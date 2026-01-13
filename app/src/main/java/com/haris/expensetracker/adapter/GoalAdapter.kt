package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemCardBudgetBinding
import com.haris.expensetracker.room.Goals
import java.text.DecimalFormat

class GoalAdapter : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var goalList = emptyList<Goals>()
    class GoalViewHolder(val binding: ItemCardBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemCardBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val currentGoal = goalList[position]
        val binding = holder.binding
        val decimalFormat = DecimalFormat("#,###.##")

        val saved = decimalFormat.format(currentGoal.savedAmount)
        val target = decimalFormat.format(currentGoal.targetAmount)

        binding.tvItemAmount.text = "$saved / $target PKR"

        binding.tvBudgetSubtitle.text = "Target Date: ${currentGoal.desiredDate}"

        binding.tvBudgetTitle.text = "Goals"
        binding.tvBudgetSubtitle.text = "Track your savings progress"
        binding.tvItemName.text = currentGoal.name

        val progress = if (currentGoal.targetAmount > 0) {
            ((currentGoal.savedAmount / currentGoal.targetAmount) * 100).toInt()
        } else 0

        binding.tvItemAmount.text = "${currentGoal.savedAmount} / ${currentGoal.targetAmount} PKR"
        binding.pbBudgetProgress.progress = progress

        binding.viewStatusIndicator.setBackgroundResource(
            if (progress >= 100) android.R.color.holo_green_dark else android.R.color.holo_blue_dark
        )

        binding.btnCreateBudget.visibility = View.GONE
    }

    override fun getItemCount(): Int = goalList.size

    fun submitList(newList: List<Goals>) {
        goalList = newList
        notifyDataSetChanged()
    }
}