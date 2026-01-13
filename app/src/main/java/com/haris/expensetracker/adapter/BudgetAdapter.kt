package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemCardBudgetBinding
import com.haris.expensetracker.room.Budget
import java.text.DecimalFormat

class BudgetAdapter : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
    private var budgetList = emptyList<Budget>()
    private val decimalFormat = DecimalFormat("#,###.##")
    class BudgetViewHolder(val binding: ItemCardBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemCardBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val currentBudget = budgetList[position]
        val binding = holder.binding

        binding.tvItemName.text = currentBudget.name

        val progress = if (currentBudget.limitAmount > 0) {
            ((currentBudget.spentAmount / currentBudget.limitAmount) * 100).toInt()
        } else 0

        val spent = decimalFormat.format(currentBudget.spentAmount)
        val limit = decimalFormat.format(currentBudget.limitAmount)
        binding.tvItemAmount.text = "$spent / $limit PKR"

        binding.pbBudgetProgress.progress = progress

        if (currentBudget.spentAmount > currentBudget.limitAmount) {
            binding.viewStatusIndicator.setBackgroundResource(android.R.color.holo_red_dark)
        } else {
            binding.viewStatusIndicator.setBackgroundResource(android.R.color.holo_green_dark)
        }

        val amountText = "${currentBudget.limitAmount} PKR"
        binding.tvItemAmount.text = amountText

        binding.pbBudgetProgress.max = 100
        binding.pbBudgetProgress.progress = 100

        binding.ivMenu.setOnClickListener {}
        binding.btnCreateBudget.visibility = View.GONE
    }

    override fun getItemCount(): Int = budgetList.size

    fun submitList(newList: List<Budget>) {
        budgetList = newList
        notifyDataSetChanged()
    }
}