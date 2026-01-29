package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemAccountSettingBinding
import com.haris.expensetracker.room.Account
import java.util.Collections

class AccountSettingAdapter(
    private var accounts: MutableList<Account>,
    private val onEditClick: (Account) -> Unit,
) : RecyclerView.Adapter<AccountSettingAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAccountSettingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAccountSettingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = accounts[position]

        holder.binding.apply {
            tvAccountListName.text = account.name
            tvAccountListBalance.text = "PKR ${String.format("%.2f", account.balance)}"

            btnEdit.setOnClickListener {
                onEditClick(account)
            }
        }
    }

    override fun getItemCount() = accounts.size

    fun submitList(newList: List<Account>) {
        accounts = newList.toMutableList()
        notifyDataSetChanged()
    }

    fun onItemMove(fromPosition: Int, toPosition: Int) {
        java.util.Collections.swap(accounts, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun getAccountAt(position: Int): Account = accounts[position]
}