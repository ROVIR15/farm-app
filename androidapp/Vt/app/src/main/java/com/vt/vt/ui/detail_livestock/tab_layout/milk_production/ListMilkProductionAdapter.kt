package com.vt.vt.ui.detail_livestock.tab_layout.milk_production

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.livestock.dto.MilkProductionRecordsItem
import com.vt.vt.databinding.ItemMilkProductionBinding

class ListMilkProductionAdapter :
    ListAdapter<MilkProductionRecordsItem, ListMilkProductionAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemMilkProductionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemMilkProductionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: MilkProductionRecordsItem) {
            with(binding) {
                tvItemDate.text = data.date
                tvItemValueCurrentMilk.text = "${data.score} liter"
                tvItemValuePastMilk.text = "${data.prevScore} liter"
                binding.tvGrow.text = data.growth
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<MilkProductionRecordsItem?> =
            object : DiffUtil.ItemCallback<MilkProductionRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: MilkProductionRecordsItem, newItem: MilkProductionRecordsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: MilkProductionRecordsItem, newItem: MilkProductionRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}