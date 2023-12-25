package com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.breeding.dto.BreedingHistoryItem
import com.vt.vt.databinding.ItemHistoryBinding

class HistoryAnimalMatingAdapter :
    ListAdapter<BreedingHistoryItem, HistoryAnimalMatingAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: BreedingHistoryItem) {
            data.let {
                binding.tvDateRecord.text = it.date
                binding.tvDescriptionRecord.text = it.remarks
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BreedingHistoryItem> =
            object : DiffUtil.ItemCallback<BreedingHistoryItem>() {
                override fun areItemsTheSame(
                    oldItem: BreedingHistoryItem, newItem: BreedingHistoryItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BreedingHistoryItem, newItem: BreedingHistoryItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

}