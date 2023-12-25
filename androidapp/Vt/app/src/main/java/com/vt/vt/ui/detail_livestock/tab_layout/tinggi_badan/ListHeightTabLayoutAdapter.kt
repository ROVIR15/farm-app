package com.vt.vt.ui.detail_livestock.tab_layout.tinggi_badan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.livestock.dto.HeightRecordsItem
import com.vt.vt.databinding.ItemHeightBinding

class ListHeightTabLayoutAdapter :
    ListAdapter<HeightRecordsItem, ListHeightTabLayoutAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemHeightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemHeightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: HeightRecordsItem) {
            with(binding) {
                tvItemDate.text = data.date
                tvItemValueCurrentHeight.text = "${data.score} Kg"
                tvItemValuePastHeight.text = "${data.prevScore} Kg"
                binding.tvGrow.text = data.growth
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<HeightRecordsItem?> =
            object : DiffUtil.ItemCallback<HeightRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: HeightRecordsItem, newItem: HeightRecordsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: HeightRecordsItem, newItem: HeightRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}