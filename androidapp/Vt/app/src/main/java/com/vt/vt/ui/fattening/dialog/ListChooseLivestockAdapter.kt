package com.vt.vt.ui.fattening.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockOptionResponseItem
import com.vt.vt.databinding.ItemChooseLivestockBinding

class ListChooseLivestockAdapter :
    ListAdapter<LivestockOptionResponseItem, ListChooseLivestockAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onClickListener: ((LivestockOptionResponseItem) -> Unit)? = null
    private var lastPosition = -1
    private var selectedItem = -1

    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(private val binding: ItemChooseLivestockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: LivestockOptionResponseItem) {
            val adapterPositionId = currentList[adapterPosition].id
            binding.nameLivestock.text = data.name
            binding.detail.text = data.info
            if (selectedItem == adapterPositionId) {
                binding.radioButton.isChecked = true
                binding.container.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_blue_shape_home)
            } else {
                binding.radioButton.isChecked = false
                binding.container.setBackgroundColor(Color.WHITE)
            }
            setAnimation(itemView, adapterPosition)
        }

        init {
            itemView.setOnClickListener {
                selectedItem = currentList[adapterPosition].id!!
                onClickListener?.invoke(currentList[adapterPosition])
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemChooseLivestockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 550
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<LivestockOptionResponseItem> =
            object : DiffUtil.ItemCallback<LivestockOptionResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: LivestockOptionResponseItem,
                    newItem: LivestockOptionResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LivestockOptionResponseItem,
                    newItem: LivestockOptionResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }

    }

}