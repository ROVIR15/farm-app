package com.vt.vt.ui.rekam_perkawinan.tab_layout.lambing_breeding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.breeding.LambingItem
import com.vt.vt.databinding.ItemLembingBinding
import com.vt.vt.utils.formatDate

class LambingBreedingAdapter :
    ListAdapter<LambingItem, LambingBreedingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLembingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemLembingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: LambingItem) {
            val createdAt = formatDate(data.createdAt, "dd MMM yyyy")
            with(binding) {
                tvDate.text = createdAt
                tvTitle.text = data.livestock.name
                infoLivestock.text = data.livestock.info
                description.text = data.livestock.description
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<LambingItem> =
            object : DiffUtil.ItemCallback<LambingItem>() {
                override fun areItemsTheSame(
                    oldItem: LambingItem,
                    newItem: LambingItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LambingItem,
                    newItem: LambingItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}