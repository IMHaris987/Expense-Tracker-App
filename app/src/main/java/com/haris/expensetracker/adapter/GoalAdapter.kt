package com.haris.expensetracker.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemCardBudgetBinding
import com.haris.expensetracker.room.Goals
import java.text.DecimalFormat

class GoalAdapter(
    private val onCreateClick: () -> Unit,
    private val onEditClick: (Goals) -> Unit,
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

        binding.tvItemName.text = goal.name
        binding.tvSectionThisWeek.text = "Target Date: ${goal.desiredDate}"

        val progress = if (goal.targetAmount > 0) {
            ((goal.savedAmount / goal.targetAmount) * 100).toInt()
        } else 0

        val saved = decimalFormat.format(goal.savedAmount)
        val target = decimalFormat.format(goal.targetAmount)
        binding.tvItemAmount.text = "$saved / $target PKR - $progress%"

        binding.pbBudgetProgress.max = 100
        binding.pbBudgetProgress.progress = if (progress > 100) 100 else progress

        if (progress >= 100) {
            binding.viewStatusIndicator.setBackgroundColor(Color.parseColor("#4CAF50")) // Green for Completed
            binding.pbBudgetProgress.progressTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
        } else {
            binding.viewStatusIndicator.setBackgroundColor(Color.parseColor("#2196F3")) // Blue for In Progress
            binding.pbBudgetProgress.progressTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
        }

        binding.ivMenu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add(0, 0, 0, "Edit")
            popup.menu.add(0, 1, 1, "Delete")

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    0 -> onEditClick(goal)
                    1 -> onDeleteClick(goal.id.toInt())
                }
                true
            }
            popup.show()
        }

        binding.btnCreateBudget.text = "Create Goal"
        binding.btnCreateBudget.visibility = if (position == goalList.size - 1) View.VISIBLE else View.GONE
        binding.btnCreateBudget.setOnClickListener {
            onCreateClick()
        }
    }

    override fun getItemCount(): Int = goalList.size

    fun submitList(newList: List<Goals>) {
        goalList = newList
        notifyDataSetChanged()
    }
}