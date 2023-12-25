package com.vt.vt.ui.penyimpan_ternak.adapter

import android.content.Context
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
import com.vt.vt.core.data.source.remote.block_areas.dto.BlockAndAreasResponseItem
import com.vt.vt.databinding.ItemPenyimpanTernakBinding
import com.vt.vt.ui.penyimpan_ternak.LivestockStorageViewModel

class LivestockStorageAdapter(
    private val context: Context,
    private val viewModel: LivestockStorageViewModel,
) : ListAdapter<BlockAndAreasResponseItem, LivestockStorageAdapter.ViewHolder>(
    TASK_DIFF_CALLBACK
) {
    private lateinit var mBundle: Bundle
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemPenyimpanTernakBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemPenyimpanTernakBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bindTo(data: BlockAndAreasResponseItem) {
            binding.tvDateContentLivestockStorage.text = data.createdAt
            binding.tvTitlePenyimpanTernak.text = data.name
            binding.tvSubtitlePenyimpanTernak.text = data.description
            binding.btnRekam.setOnClickListener(this)
            binding.btnSeeAllArea.setOnClickListener(this)
            binding.btnOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_rekam -> {
                    isUpdate = false
                    val id = currentList[adapterPosition].id
                    mBundle = Bundle().apply {
                        putInt("id", id)
                    }
                    v.findNavController()
                        .navigate(
                            R.id.action_penyimpanTernakFragment_to_pemberianTernakFragment,
                            mBundle
                        )
                }

                R.id.btn_see_all_area -> {
                    isUpdate = false
                    val id = currentList[adapterPosition].id
                    mBundle = Bundle().apply {
                        putInt("id", id)
                    }
                    v.findNavController()
                        .navigate(
                            R.id.action_penyimpanTernakFragment_to_detailAreaBlockFragment,
                            mBundle
                        )
                }

                R.id.btn_options -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options_penyimpanan_ternak)
                    popup.setOnMenuItemClickListener(this)
                    popup.show()
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_edit_penyimpanan_ternak -> {
                    isUpdate = true
                    val id = currentList[adapterPosition].id
                    mBundle = Bundle().apply {
                        putInt("id", id)
                    }
                    itemView.findNavController()
                        .navigate(R.id.action_penyimpanTernakFragment_to_dataAreaFragment, mBundle)
                    return true
                }

                R.id.menu_delete_penyimpanan_ternak -> {
                    isUpdate = false
                    AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val blockArea = currentList[adapterPosition]
                            viewModel.deleteBlockAndArea(
                                blockArea.id.toString()
                            )
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .create()
                        .show()

                    return true
                }
            }
            return false
        }
    }

    companion object {
        var isUpdate: Boolean = false
        val TASK_DIFF_CALLBACK: DiffUtil.ItemCallback<BlockAndAreasResponseItem?> =
            object : DiffUtil.ItemCallback<BlockAndAreasResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: BlockAndAreasResponseItem,
                    newItem: BlockAndAreasResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BlockAndAreasResponseItem,
                    newItem: BlockAndAreasResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}