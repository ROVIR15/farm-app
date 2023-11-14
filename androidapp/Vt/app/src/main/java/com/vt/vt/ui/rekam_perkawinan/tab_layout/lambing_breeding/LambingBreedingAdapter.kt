package com.vt.vt.ui.rekam_perkawinan.tab_layout.lambing_breeding

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
import com.vt.vt.core.data.source.remote.breeding.LambingItem
import com.vt.vt.databinding.ItemLembingBinding
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.utils.formatDate

class LambingBreedingAdapter(private val viewModel: RecordBreedingViewModel) :
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
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bindTo(data: LambingItem) {
            val createdAt = formatDate(data.createdAt, "dd MMM yyyy")
            with(binding) {
                tvDate.text = createdAt
                tvTitle.text = data.livestock.name
                infoLivestock.text = data.livestock.info
                description.text = data.livestock.description
                btnOptions.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options -> {
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
                    val id = currentList[adapterPosition].livestock.id
                    val mBundle = Bundle()
                    if (id != null) {
                        mBundle.putInt("id", id)
                    }
                    itemView.findNavController().navigate(
                        R.id.action_breedingRecordFragment_to_editLivestockFragment,
                        mBundle
                    )
                    return true
                }

                R.id.menu_delete -> {
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val id = currentList[adapterPosition].id
                            viewModel.deleteLambing(id.toString())
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