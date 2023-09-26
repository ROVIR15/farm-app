package com.vt.vt.ui.detail_livestock.tab_layout.kesehatan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponseItem
import com.vt.vt.databinding.ItemKesehatanBinding

class ListHealthRecordAdapter :
    ListAdapter<HealthRecordResponseItem, ListHealthRecordAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemKesehatanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val getItem = getItem(position)
        holder.bindTo(getItem)
    }

    inner class ViewHolder(private val binding: ItemKesehatanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: HealthRecordResponseItem) {
            with(binding) {
                tvDateHealthRecord.text = data.date
                tvDescriptionHealthRecord.text = data.remarks
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<HealthRecordResponseItem?> =
            object : DiffUtil.ItemCallback<HealthRecordResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: HealthRecordResponseItem,
                    newItem: HealthRecordResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: HealthRecordResponseItem,
                    newItem: HealthRecordResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
