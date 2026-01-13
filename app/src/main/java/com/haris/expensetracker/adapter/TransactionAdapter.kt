package com.haris.expensetracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.room.TransactionEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import com.haris.expensetracker.databinding.ItemTransactionBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var transactionList = emptyList<TransactionEntity>()
    private val decimalFormat = DecimalFormat("#,###.##")
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    class TransactionViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val transaction = transactionList[position]
        val binding = holder.binding

        binding.tvCategoryName.text = transaction.categoryName
        binding.tvDate.text = dateFormat.format(transaction.date)
        binding.tvNote.text = transaction.note

        if (transaction.type == "Expense") {
            binding.tvAmount.text = "- ${decimalFormat.format(transaction.amount)} PKR"
            binding.tvAmount.setTextColor(Color.RED)
        } else {
            binding.tvAmount.text = "+ ${decimalFormat.format(transaction.amount)} PKR"
            binding.tvAmount.setTextColor(Color.parseColor("#4CAF50"))
        }
    }

    override fun getItemCount(): Int = transactionList.size

    fun submitList(newList: List<TransactionEntity>) {
        transactionList = newList
        notifyDataSetChanged()
    }
}