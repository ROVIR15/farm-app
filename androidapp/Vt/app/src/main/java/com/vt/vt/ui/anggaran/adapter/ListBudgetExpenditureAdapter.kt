package com.vt.vt.ui.anggaran.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.budget.ExpendituresItem
import com.vt.vt.databinding.ItemAnggaranBinding
import com.vt.vt.utils.convertRupiah

class ListBudgetExpenditureAdapter() :
    ListAdapter<ExpendituresItem, ListBudgetExpenditureAdapter.BudgetViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = ItemAnggaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budgetItem = getItem(position)
        holder.bind(budgetItem)
    }

    inner class BudgetViewHolder(private val binding: ItemAnggaranBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(expendituresItem: ExpendituresItem) {
            val amountExpenditure = expendituresItem.amount.toString()
            val decimalAmountExpenditureValue = amountExpenditure.replace(".", "").toBigDecimal()
            binding.tvBudget.text = decimalAmountExpenditureValue.convertRupiah()
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ExpendituresItem> =
            object : DiffUtil.ItemCallback<ExpendituresItem>() {
                override fun areItemsTheSame(
                    oldItem: ExpendituresItem,
                    newItem: ExpendituresItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ExpendituresItem,
                    newItem: ExpendituresItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}