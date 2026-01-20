package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemAccountBinding
import com.haris.expensetracker.room.Account

class AccountAdapter(
    private var accountList: List<Account>,
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    class AccountViewHolder(val binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accountList[position]
        val binding= holder.binding

        binding.tvAccountName.text = account.name
        binding.tvAccountBalance.text = "PKR ${String.format("%.2f", account.balance)}"
    }

    override fun getItemCount() = accountList.size

    fun submitList(newList: List<Account>) {
        accountList = newList
        notifyDataSetChanged()
    }
}