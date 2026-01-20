package com.haris.expensetracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.expensetracker.databinding.ItemAccountSettingBinding
import com.haris.expensetracker.room.Account

class AccountSettingAdapter(
    private var accounts: List<Account>,
    private val onEditClick: (Account) -> Unit,
    private val onDeleteClick: (Account) -> Unit
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
            tvAccountType.text = account.accountType
            tvAccountListBalance.text = "PKR ${String.format("%.2f", account.balance)}"


            btnDelete.setOnLongClickListener {
                onDeleteClick(account)
                true
            }

            btnEdit.setOnClickListener {
                onEditClick(account)
            }
        }
    }

    override fun getItemCount() = accounts.size

    fun submitList(newList: List<Account>) {
        accounts = newList
        notifyDataSetChanged()
    }
}