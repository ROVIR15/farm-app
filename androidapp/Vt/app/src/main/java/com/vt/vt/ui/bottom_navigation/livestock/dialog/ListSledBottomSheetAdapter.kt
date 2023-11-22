package com.vt.vt.ui.bottom_navigation.livestock.dialog

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.vt.vt.core.data.source.remote.sleds.model.SledOptionResponseItem
import com.vt.vt.databinding.ItemChooseSledBinding

class ListSledBottomSheetAdapter :
    ListAdapter<SledOptionResponseItem, ListSledBottomSheetAdapter.ListSledViewHolder>(DIFF_CALLBACK) {
    var onClickListener: ((SledOptionResponseItem) -> Unit)? = null
    private var lastPosition = -1
    private var selectedItem = -1

    @SuppressLint("NotifyDataSetChanged")
    inner class ListSledViewHolder(private val binding: ItemChooseSledBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: SledOptionResponseItem) {
            val adapterPositionId = currentList[adapterPosition].id
            binding.nameSled.text = data.name
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSledViewHolder {
        val view =
            ItemChooseSledBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ListSledViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListSledViewHolder, position: Int) {
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
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SledOptionResponseItem> =
            object : DiffUtil.ItemCallback<SledOptionResponseItem>() {
                override fun areItemsTheSame(
                    oldItem: SledOptionResponseItem,
                    newItem: SledOptionResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SledOptionResponseItem,
                    newItem: SledOptionResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}