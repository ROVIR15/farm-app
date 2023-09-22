package com.vt.vt.ui.penyimpan_ternak.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.block_areas.model.BlockAndAreasResponseItem
import com.vt.vt.databinding.ItemPenyimpanTernakBinding

class PenyimpananTernakAdapter(private val blockAndAreas: List<BlockAndAreasResponseItem>) :
    RecyclerView.Adapter<PenyimpananTernakAdapter.PenyimpananTernakViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenyimpananTernakViewHolder {
        val view =
            ItemPenyimpanTernakBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PenyimpananTernakViewHolder(view)
    }

    override fun onBindViewHolder(holder: PenyimpananTernakViewHolder, position: Int) {
        holder.bindTo(blockAndAreas[position])
    }

    override fun getItemCount(): Int {
        return blockAndAreas.size
    }

    inner class PenyimpananTernakViewHolder(private val binding: ItemPenyimpanTernakBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bindTo(data: BlockAndAreasResponseItem) {
            binding.tvTitlePenyimpanTernak.text = data.name
            binding.tvSubtitlePenyimpanTernak.text = data.description
            binding.btnRekam.setOnClickListener(this)
            binding.btnSeeAllArea.setOnClickListener(this)
            binding.btnOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_rekam -> {
                    v.findNavController()
                        .navigate(R.id.action_penyimpanTernakFragment_to_pemberianTernakFragment)
                }

                R.id.btn_see_all_area -> {
                    v.findNavController()
                        .navigate(R.id.action_penyimpanTernakFragment_to_detailAreaBlockFragment)
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
                    return true
                }

                R.id.menu_delete_penyimpanan_ternak -> {
                    return true
                }
            }
            return false
        }
    }
}