package com.vt.vt.ui.detail_livestock.tab_layout.bcs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.livestock.dto.BcsRecordsItem
import com.vt.vt.databinding.ItemBcsBinding
import com.vt.vt.utils.formatDate

class BcsRecordAdapter :
    ListAdapter<BcsRecordsItem, BcsRecordAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemBcsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemBcsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: BcsRecordsItem) {
            val createdAt = formatDate(data.createdAt, "dd-MMMM-yyyy")
            binding.tvItemDateBcs.text = createdAt
            binding.tvItemValueBcsSekarang.text = "${data.score} Kg"
            binding.tvItemValueBcsSebelumnya.text = "${data.prevScore} Kg"
            binding.tvGrow.text = data.growth
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BcsRecordsItem?> =
            object : DiffUtil.ItemCallback<BcsRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: BcsRecordsItem,
                    newItem: BcsRecordsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BcsRecordsItem,
                    newItem: BcsRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}