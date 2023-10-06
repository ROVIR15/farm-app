package com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.dummy.livestock.Pakan
import com.vt.vt.databinding.ItemPakanBinding

class AreaListPakanAdapter(private val pakan: List<Pakan>) :
    RecyclerView.Adapter<AreaListPakanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPakanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pakan[position])
    }

    override fun getItemCount(): Int = pakan.size

    inner class ViewHolder(private val binding: ItemPakanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Pakan) {
            binding.tvItemDate.text = data.createdAt
            binding.tvScoreHijauan.text = data.scoreHijauan.toString()
            binding.tvScoreVitamin.text = data.scoreHijauan.toString()
            binding.tvScoreKosentrat.text = data.scoreHijauan.toString()
            binding.tvTambahanScore.text = data.scoreHijauan.toString()
        }
    }

}