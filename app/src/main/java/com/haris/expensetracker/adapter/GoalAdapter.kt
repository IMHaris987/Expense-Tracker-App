package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemCardBudgetBinding
import com.haris.expensetracker.room.Goals
import java.text.DecimalFormat

class GoalAdapter(
    private val onCreateClick: () -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<GoalAdapter.GoalViewHolder>() {

    private var goalList = emptyList<Goals>()
    private val decimalFormat = DecimalFormat("#,###")

    class GoalViewHolder(val binding: ItemCardBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemCardBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val goal = goalList[position]
        val binding = holder.binding

        if (position == 0) {
            binding.tvBudgetTitle.visibility = View.VISIBLE
            binding.tvBudgetSubtitle.visibility = View.VISIBLE
            binding.tvBudgetTitle.text = "Goals"
            binding.tvBudgetSubtitle.text = "Track your savings progress"
        } else {
            binding.tvBudgetTitle.visibility = View.GONE
            binding.tvBudgetSubtitle.visibility = View.GONE
        }

        binding.btnCreateBudget.text = "Create Goal"
        binding.btnCreateBudget.visibility = if (position == goalList.size - 1) View.VISIBLE else View.GONE
        binding.btnCreateBudget.setOnClickListener { onCreateClick() }

        binding.tvItemName.text = goal.name
        binding.tvSectionThisWeek.text = "Target: ${goal.desiredDate}"

        val progress = if (goal.targetAmount > 0) {
            ((goal.savedAmount / goal.targetAmount) * 100).toInt()
        } else 0

        val saved = decimalFormat.format(goal.savedAmount)
        val target = decimalFormat.format(goal.targetAmount)
        binding.tvItemAmount.text = "$saved / $target PKR -$progress%"

        binding.pbBudgetProgress.max = 100
        binding.pbBudgetProgress.progress = if (progress > 100) 100 else progress

        if (progress >= 100) {
            binding.viewStatusIndicator.setBackgroundResource(android.R.color.holo_green_dark)
            binding.pbBudgetProgress.progressTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#4CAF50"))
        } else {
            binding.viewStatusIndicator.setBackgroundResource(android.R.color.holo_blue_dark)
            binding.pbBudgetProgress.progressTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2196F3"))
        }

        binding.ivMenu.setOnClickListener { view ->
            val popup = android.widget.PopupMenu(view.context, view)
            popup.menu.add("Delete")
            popup.setOnMenuItemClickListener { item ->
                if (item.title == "Delete") {
                    onDeleteClick(goal.id)
                }
                true
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = goalList.size

    fun submitList(newList: List<Goals>) {
        goalList = newList
        notifyDataSetChanged()
    }
}