package com.haris.expensetracker.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemCardBudgetBinding
import com.haris.expensetracker.room.Budget
import java.text.DecimalFormat

class BudgetAdapter(
    private val onCreateClick: () -> Unit,
    private val onEditClick: (Budget) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    private var budgetList = emptyList<Budget>()
    private val decimalFormat = DecimalFormat("#,###")

    class BudgetViewHolder(val binding: ItemCardBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemCardBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgetList[position]
        val binding = holder.binding

        binding.tvItemName.text = budget.name
        binding.tvSectionThisWeek.text = budget.period

        val progress = if (budget.limitAmount > 0) {
            ((budget.spentAmount / budget.limitAmount) * 100).toInt()
        } else 0

        val spent = decimalFormat.format(budget.spentAmount)
        val limit = decimalFormat.format(budget.limitAmount)
        binding.tvItemAmount.text = "$spent / $limit PKR - $progress%"

        binding.pbBudgetProgress.max = 100
        binding.pbBudgetProgress.progress = if (progress > 100) 100 else progress

        if (budget.spentAmount > budget.limitAmount) {
            binding.viewStatusIndicator.setBackgroundColor(Color.RED)
            binding.pbBudgetProgress.progressTintList = ColorStateList.valueOf(Color.RED)
        } else {
            binding.viewStatusIndicator.setBackgroundColor(Color.parseColor("#4CAF50")) // Material Green
            binding.pbBudgetProgress.progressTintList = ColorStateList.valueOf(Color.parseColor("#2196F3"))
        }

        binding.ivMenu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menu.add(0, 0, 0, "Edit")
            popup.menu.add(0, 1, 1, "Delete")

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    0 -> onEditClick(budget)
                    1 -> onDeleteClick(budget.id.toInt())
                }
                true
            }
            popup.show()
        }

        if (position == 0) {
            binding.tvBudgetTitle.visibility = View.VISIBLE
            binding.tvBudgetSubtitle.visibility = View.VISIBLE
        } else {
            binding.tvBudgetTitle.visibility = View.GONE
            binding.tvBudgetSubtitle.visibility = View.GONE
        }

        binding.btnCreateBudget.visibility = if (position == budgetList.size - 1) View.VISIBLE else View.GONE
        binding.btnCreateBudget.setOnClickListener {
            onCreateClick()
        }
    }

    override fun getItemCount(): Int = budgetList.size

    fun submitList(newList: List<Budget>) {
        budgetList = newList
        notifyDataSetChanged()
    }
}