package com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.block_areas.dto.FeedingRecordsItem
import com.vt.vt.databinding.ItemFoodBinding

class ListFoodAreasAdapter() :
    ListAdapter<FeedingRecordsItem, ListFoodAreasAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FeedingRecordsItem) {
            binding.tvItemDate.text = data.day
            val scoreTextViewMap = mapOf(
                "Hijauan" to binding.tvScoreHijauan,
                "Konsentrat" to binding.tvScoreKosentrat,
                "Vitamin" to binding.tvScoreVitamin,
                "Tambahan" to binding.tvTambahanScore
            )

            for (feedItem in data.feedList) {
                val feedCategory = feedItem.feedCategory
                val totalScore = feedItem.totalScore

                scoreTextViewMap[feedCategory]?.text = totalScore.toString()
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<FeedingRecordsItem?> =
            object : DiffUtil.ItemCallback<FeedingRecordsItem?>() {
                override fun areItemsTheSame(
                    oldItem: FeedingRecordsItem, newItem: FeedingRecordsItem
                ): Boolean {
                    return oldItem.blockAreaId == newItem.blockAreaId
                }

                override fun areContentsTheSame(
                    oldItem: FeedingRecordsItem, newItem: FeedingRecordsItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}