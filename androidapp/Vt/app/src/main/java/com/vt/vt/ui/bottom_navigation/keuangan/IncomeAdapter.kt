package com.vt.vt.ui.bottom_navigation.keuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.income.IncomesItem
import com.vt.vt.databinding.ItemIncomeBinding
import com.vt.vt.utils.convertRupiah

class IncomeAdapter : ListAdapter<IncomesItem, IncomeAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemIncomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: IncomesItem) {
            val amount = data.amount?.convertRupiah()
            binding.tvTitleIncome.text = data.categoryLabel.toString()
            binding.tvAmount.text = amount
            val incomeId = currentList[adapterPosition].id
            itemView.setOnClickListener {
                val mBundle = Bundle()
                if (incomeId != null) {
                    mBundle.putInt("incomeId", incomeId)
                }
                it.findNavController()
                    .navigate(R.id.action_navigation_keuangan_to_editIncomeFragment, mBundle)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<IncomesItem> =
            object : DiffUtil.ItemCallback<IncomesItem>() {
                override fun areItemsTheSame(
                    oldItem: IncomesItem, newItem: IncomesItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: IncomesItem, newItem: IncomesItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}
