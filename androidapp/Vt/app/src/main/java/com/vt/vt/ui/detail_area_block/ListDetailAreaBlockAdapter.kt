package com.vt.vt.ui.detail_area_block

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
import com.vt.vt.core.data.source.remote.block_areas.model.Sleds
import com.vt.vt.core.data.source.remote.sleds.model.SledsResponseItem
import com.vt.vt.databinding.ItemDetailAreaBlockBinding
import com.vt.vt.ui.edit_area_block.AreaBlockViewModel

class ListDetailAreaBlockAdapter(
    private val context: Context,
    private val viewModel: AreaBlockViewModel
) :
    ListAdapter<Sleds, ListDetailAreaBlockAdapter.ViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemDetailAreaBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    inner class ViewHolder(private val binding: ItemDetailAreaBlockBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bindTo(sleds: Sleds) {
            with(binding) {
                tvTitleAnimalCage.text = sleds.id.toString()
                btnOptionsDetailArea.setOnClickListener(this@ViewHolder)
            }
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_detail_area -> {
                    val popup = PopupMenu(v.context, v)
                    popup.inflate(R.menu.menu_options_detail_area_block)
                    popup.setOnMenuItemClickListener(this)
                    popup.show()
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu_edit_animal_cage -> {
                    val id = currentList[adapterPosition].id
                    val bundle = Bundle().apply {
                        putInt("id", id)
                    }
                    itemView.findNavController()
                        .navigate(
                            R.id.action_detailAreaBlockFragment_to_editAreaBlockFragment,
                            bundle
                        )
                    return true
                }

                R.id.menu_delete_animal_cage -> {
                    AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val sledItems = currentList[adapterPosition]
                            viewModel.deleteSledById(
                                sledItems.id.toString()
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
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Sleds?> =
            object : DiffUtil.ItemCallback<Sleds?>() {
                override fun areItemsTheSame(
                    oldItem: Sleds,
                    newItem: Sleds
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Sleds,
                    newItem: Sleds
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
