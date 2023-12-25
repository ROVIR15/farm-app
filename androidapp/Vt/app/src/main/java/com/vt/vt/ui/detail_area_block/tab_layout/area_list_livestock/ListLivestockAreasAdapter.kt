package com.vt.vt.ui.detail_area_block.tab_layout.area_list_livestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponseItem
import com.vt.vt.databinding.ItemLivestockByBlockAreaBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel

class ListLivestockAreasAdapter(private val livestockViewModel: LivestockViewModel) :
    ListAdapter<LivestockResponseItem, ListLivestockAreasAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            binding.tvDateContent.text = data.createdAt
            binding.textViewTitle.text = data.name
            binding.textViewSubtitle.text = data.info
            binding.btnOptionsItemLivestock.setOnClickListener(this)
            binding.btnRecord.setOnClickListener(this)
            binding.btnInfo.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_item_livestock -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options)
                    popup.setOnMenuItemClickListener(this)
                    popup.show()
                }

                R.id.btn_record -> {
                    itemView.findNavController()
                        .navigate(R.id.action_detailAreaBlockFragment_to_fatteningFragment)
                }

                R.id.btn_info -> {
                    val id = currentList[adapterPosition].id
                    val mBundle = Bundle()
                    if (id != null) {
                        mBundle.putInt("id", id)
                    }
                    itemView.findNavController()
                        .navigate(
                            R.id.action_detailAreaBlockFragment_to_detailLivestockFragment,
                            mBundle
                        )
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_edit -> {
                    val getId = currentList[adapterPosition].id
                    val mBundle = Bundle()
                    if (getId != null) {
                        mBundle.putInt("id", getId)
                    }
                    itemView.findNavController()
                        .navigate(
                            R.id.action_detailAreaBlockFragment_to_editLivestockFragment,
                            mBundle
                        )
                    return true
                }

                R.id.menu_delete -> {
                    val getId = currentList[adapterPosition].id
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            livestockViewModel.deleteLivestockById(getId.toString())
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