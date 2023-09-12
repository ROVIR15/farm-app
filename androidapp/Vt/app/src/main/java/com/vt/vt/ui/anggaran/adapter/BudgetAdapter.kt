package com.vt.vt.ui.anggaran.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.databinding.ItemAnggaranBinding
import com.vt.vt.ui.anggaran.AnggaranFragment

// belum dipakai
class BudgetAdapter(private val budgetItemList: List<AnggaranFragment.BudgetItem>) :
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
        fun bind(budgetItem: AnggaranFragment.BudgetItem) {
            binding.tvTitleKategoriBudget.text = budgetItem.name
        }
    }
}