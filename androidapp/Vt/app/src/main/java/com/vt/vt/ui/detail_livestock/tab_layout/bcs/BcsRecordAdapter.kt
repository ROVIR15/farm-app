package com.vt.vt.ui.detail_livestock.tab_layout.bcs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.bcs_record.model.BcsRecordResponseItem
import com.vt.vt.databinding.ItemBcsBinding

class BcsRecordAdapter :
    ListAdapter<BcsRecordResponseItem, BcsRecordAdapter.ViewHolder>(DIFF_CALLBACK) {

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
        fun bindTo(data: BcsRecordResponseItem) {
            binding.tvItemDateBcs.text = data.date
            binding.tvItemValueBcsSekarang.text = "${data.score} Kg"
            binding.tvItemValueBcsSebelumnya.text = "${data.prevScore} Kg"
            binding.tvGrow.text = data.grow
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BcsRecordResponseItem?> =
            object : DiffUtil.ItemCallback<BcsRecordResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: BcsRecordResponseItem,
                    newItem: BcsRecordResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BcsRecordResponseItem,
                    newItem: BcsRecordResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}