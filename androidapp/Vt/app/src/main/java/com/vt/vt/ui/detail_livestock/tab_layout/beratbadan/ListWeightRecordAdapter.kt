package com.vt.vt.ui.detail_livestock.tab_layout.beratbadan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.weight_record.model.WeightRecordResponseItem
import com.vt.vt.databinding.ItemBeratBadanBinding

class ListWeightRecordAdapter :
    ListAdapter<WeightRecordResponseItem, ListWeightRecordAdapter.ViewHolder>(DIFF_CALLBACK) {

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
        fun bindTo(data: WeightRecordResponseItem) {
            with(binding) {
                tvItemDateBeratBadan.text = data.date
                tvItemValueBeratSekarang.text = "${data.score} Kg"
                tvItemValueBeratSebelumnya.text = "${data.score} Kg"
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<WeightRecordResponseItem?> =
            object : DiffUtil.ItemCallback<WeightRecordResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: WeightRecordResponseItem,
                    newItem: WeightRecordResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: WeightRecordResponseItem,
                    newItem: WeightRecordResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
