package com.vt.vt.ui.bottom_navigation.keuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.budget.BudgetBreakdownItem
import com.vt.vt.databinding.ItemFinanceSummaryBinding
import com.vt.vt.utils.convertRupiah

class BudgetAdapter() :
    ListAdapter<BudgetBreakdownItem, BudgetAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemFinanceSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemFinanceSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: BudgetBreakdownItem) {
            val budgetValue = "${data.budgetAmount}"
            val expensesValue = "${data.left}"
            val decimalBudgetValue = budgetValue.replace(".", "").toBigDecimal()
            val decimalExpensesValue = expensesValue.replace(".", "").toBigDecimal()
            val id = currentList[adapterPosition].id
            binding.tvBudgetValue.text = decimalBudgetValue.convertRupiah()
            binding.tvExpensesValue.text = decimalExpensesValue.convertRupiah()
            itemView.setOnClickListener {
                val mBundle = Bundle()
                if (id != null) {
                    mBundle.putInt("id", id)
                }
                it.findNavController()
                    .navigate(R.id.action_navigation_keuangan_to_anggaranFragment, mBundle)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BudgetBreakdownItem> =
            object : DiffUtil.ItemCallback<BudgetBreakdownItem>() {
                override fun areItemsTheSame(
                    oldItem: BudgetBreakdownItem,
                    newItem: BudgetBreakdownItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BudgetBreakdownItem,
                    newItem: BudgetBreakdownItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}