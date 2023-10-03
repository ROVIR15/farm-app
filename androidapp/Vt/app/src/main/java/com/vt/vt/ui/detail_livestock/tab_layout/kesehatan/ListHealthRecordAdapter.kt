package com.vt.vt.ui.detail_livestock.tab_layout.kesehatan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponseItem
import com.vt.vt.core.data.source.remote.livestock.model.HealthRecordsItem
import com.vt.vt.databinding.ItemKesehatanBinding

class ListHealthRecordAdapter :
    ListAdapter<HealthRecordsItem, ListHealthRecordAdapter.ViewHolder>(DIFF_CALLBACK) {
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
        fun bindTo(data: HealthRecordsItem) {
            with(binding) {
                tvDateHealthRecord.text = data.date
                tvDescriptionHealthRecord.text = data.remarks
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<HealthRecordsItem?> =
            object : DiffUtil.ItemCallback<HealthRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: HealthRecordsItem,
                    newItem: HealthRecordsItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: HealthRecordsItem,
                    newItem: HealthRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
