package com.vt.vt.ui.detail_area_block.tab_layout.area_list_livestock

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.databinding.ItemLivestockByBlockAreaBinding

class AreaListLivestockAdapter :
    ListAdapter<LivestockResponseItem, AreaListLivestockAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemLivestockByBlockAreaBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemLivestockByBlockAreaBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bindTo(data: LivestockResponseItem) {
            binding.textViewTitle.text = data.name
            binding.textViewSubtitle.text = data.description
            binding.btnOptionsItemLivestock.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_item_livestock -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options)
                    popup.setOnMenuItemClickListener(this)
                    popup.show()
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val getId = currentList[adapterPosition].id
                    return true
                }

                R.id.menu_delete -> {
                    val getId = currentList[adapterPosition].id
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            dialog.dismiss()
                        }.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()
                    return true
                }
            }
            return false
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<LivestockResponseItem> =
            object : DiffUtil.ItemCallback<LivestockResponseItem>() {
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