package com.vt.vt.ui.anggaran.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.dummy.keuangan.Pengeluaran
import com.vt.vt.databinding.ItemAnggaranBinding

class BudgetAdapter(private val budgetItemList: List<Pengeluaran>) :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = ItemAnggaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budgetItem = budgetItemList[position]
        holder.bind(budgetItem)
    }

    override fun getItemCount(): Int {
        return budgetItemList.size
    }

    inner class BudgetViewHolder(private val binding: ItemAnggaranBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(budgetItem: Pengeluaran) {
            binding.tvTitleKategoriBudget.text = budgetItem.title
            binding.tvBudget.text = "Rp.${budgetItem.budget}"
        }
    }
}