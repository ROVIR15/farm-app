package com.vt.vt.ui.detail_livestock.tab_layout.beratbadan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.livestock.dto.WeightRecordsItem
import com.vt.vt.databinding.ItemBeratBadanBinding

class ListWeightRecordAdapter :
    ListAdapter<WeightRecordsItem, ListWeightRecordAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemBeratBadanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemBeratBadanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: WeightRecordsItem) {
            with(binding) {
                tvItemDateBeratBadan.text = data.date
                tvItemValueBeratSekarang.text = "${data.score} Kg"
                tvItemValueBeratSebelumnya.text = "${data.prevScore} Kg"
                binding.tvGrow.text = data.growth
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<WeightRecordsItem?> =
            object : DiffUtil.ItemCallback<WeightRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: WeightRecordsItem,
                    newItem: WeightRecordsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: WeightRecordsItem,
                    newItem: WeightRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
