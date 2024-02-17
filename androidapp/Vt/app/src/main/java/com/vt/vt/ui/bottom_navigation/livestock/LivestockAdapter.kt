package com.vt.vt.ui.bottom_navigation.livestock

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponseItem
import com.vt.vt.databinding.ItemLivestockBinding

class LivestockAdapter(
    private val context: Context,
    private val livestockViewModel: LivestockViewModel
) :
    ListAdapter<LivestockResponseItem, LivestockAdapter.ViewHolder>(DIFF_CALLBACK) {
    var onClickListener: ((LivestockResponseItem) -> Unit)? = null
    private var lastPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLivestockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(private val binding: ItemLivestockBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        @SuppressLint("SetTextI18n")
        fun bind(livestock: LivestockResponseItem) {
            val dynamicPattern = Regex("S-\\d+")
            val newInfo = if (dynamicPattern.containsMatchIn(livestock.info)) {
                livestock.info.replace(dynamicPattern, "")
            } else {
                livestock.info
            }
            binding.tvDateContent.text = livestock.createdAt
            binding.textViewTitle.text = livestock.name
            binding.textViewSubtitle.text = newInfo
            binding.btnOptionsItemLivestock.setOnClickListener(this)
            binding.btnRecord.setOnClickListener(this)
            binding.btnInfo.setOnClickListener(this)
            setAnimation(itemView, adapterPosition)
        }

        override fun onClick(v: View?) {
            val posAdapter = currentList[adapterPosition]
            when (v?.id) {
                R.id.btn_options_item_livestock -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options_livestock)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_edit_livestock -> {
                                val mBundle = Bundle()
                                if (posAdapter.id != null) {
                                    mBundle.putInt("id", posAdapter.id)
                                    v.findNavController()
                                        .navigate(
                                            R.id.action_navigation_livestock_to_editLivestockFragment,
                                            mBundle
                                        )
                                }
                                true
                            }

                            R.id.menu_delete_livestock -> {
                                AlertDialog.Builder(context)
                                    .setTitle(R.string.title_alert_delete_item)
                                    .setIcon(R.drawable.ic_outline_delete_outline_24)
                                    .setMessage(R.string.message_delete_item)
                                    .setPositiveButton(R.string.yes) { dialog, _ ->
                                        val livestockId = currentList[adapterPosition]
                                        livestockViewModel.deleteLivestockById(
                                            livestockId.id.toString()
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton(R.string.no) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .create()
                                    .show()
                                true
                            }

                            R.id.menu_move_livestock -> {
                                val id = currentList[adapterPosition]
                                onClickListener?.invoke(id)
                                true
                            }

                            else -> {
                                false
                            }
                        }
                    }
                    popup.show()
                }

                R.id.btn_record -> {
                    val mBundle = Bundle()
                    if (posAdapter.id != null) {
                        mBundle.putString("livestockId", posAdapter.id.toString())
                        v.findNavController()
                            .navigate(
                                R.id.action_navigation_livestock_to_fatteningFragment,
                                mBundle
                            )
                    }
                }

                R.id.btn_info -> {
                    val id = currentList[adapterPosition].id
                    val mBundle = Bundle()
                    id?.let {
                        mBundle.putInt("id", it)
                        mBundle.putString("livestockTitle", posAdapter.name)
                    }
                    v.findNavController()
                        .navigate(
                            R.id.action_navigation_livestock_to_detailLivestockFragment,
                            mBundle
                        )
                }
            }
        }
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
            anim.duration = 350
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<LivestockResponseItem?> =
            object : DiffUtil.ItemCallback<LivestockResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: LivestockResponseItem,
                    newItem: LivestockResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: LivestockResponseItem,
                    newItem: LivestockResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}