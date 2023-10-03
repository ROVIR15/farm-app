package com.vt.vt.ui.bottom_navigation.livestock

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.databinding.ItemLivestockBinding

class LivestockAdapter(
    private val context: Context,
    private val livestockViewModel: LivestockViewModel
) :
    ListAdapter<LivestockResponseItem, LivestockAdapter.ViewHolder>(DIFF_CALLBACK) {

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
            binding.textViewTitle.text = livestock.name
            binding.textViewSubtitle.text = "1 tahun 6 bulan, ${livestock.bangsa}"
            binding.btnOptionsItemLivestock.setOnClickListener(this)
            binding.btnRecord.setOnClickListener(this)
            binding.btnInfo.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_item_livestock -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options_livestock)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_edit_livestock -> {
                                val id = currentList[adapterPosition].id
                                val mBundle = Bundle()
                                mBundle.putInt("id", id)
                                v.findNavController()
                                    .navigate(
                                        R.id.action_navigation_livestock_to_editLivestockFragment,
                                        mBundle
                                    )
                                true
                            }

                            R.id.menu_delete_livestock -> {
                                AlertDialog.Builder(context)
                                    .setTitle("Delete")
                                    .setIcon(R.drawable.ic_outline_delete_outline_24)
                                    .setMessage("Are you sure delete this Information")
                                    .setPositiveButton("Yes") { dialog, _ ->
                                        val livestockId = currentList[adapterPosition]
                                        livestockViewModel.deleteLivestockById(
                                            livestockId.id.toString()
                                        )
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("No") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .create()
                                    .show()
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
                    v.findNavController()
                        .navigate(R.id.action_navigation_livestock_to_fatteningFragment)
                }

                R.id.btn_info -> {
                    val id = currentList[adapterPosition].id
                    val mBundle = Bundle()
                    mBundle.putInt("id", id)
                    v.findNavController()
                        .navigate(R.id.action_navigation_livestock_to_detailLivestockFragment, mBundle)
                }
            }
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