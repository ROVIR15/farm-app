package com.vt.vt.ui.bottom_navigation.livestock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.local.livestock.Livestock
import com.vt.vt.databinding.ItemLivestockBinding

class LivestockAdapter(private val livestock: List<Livestock>) :
    RecyclerView.Adapter<LivestockAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLivestockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(livestock[position])
    }

    override fun getItemCount(): Int = livestock.size

    inner class ViewHolder(private val binding: ItemLivestockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(livestock: Livestock) {
            binding.textViewTitle.text = livestock.title
            binding.textViewSubtitle.text = livestock.birth
            binding.btnOptionsItemLivestock.setOnClickListener(this@LivestockAdapter)
            binding.btnRecord.setOnClickListener(this@LivestockAdapter)
            binding.btnInfo.setOnClickListener(this@LivestockAdapter)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_options_item_livestock -> {
                val popup = PopupMenu(v.context, v)
                popup.inflate(R.menu.menu_options_livestock)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_edit_livestock -> {
                            // navigation controller here
                            v.findNavController()
                                .navigate(R.id.action_navigation_livestock_to_editLivestockFragment)
                            true
                        }

                        R.id.menu_delete_livestock -> {
                            // navigation controller here
                            Toast.makeText(v.context, "no action", Toast.LENGTH_SHORT).show()
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
                v.findNavController()
                    .navigate(R.id.action_navigation_livestock_to_detailLivestockFragment)
            }
        }
    }
}