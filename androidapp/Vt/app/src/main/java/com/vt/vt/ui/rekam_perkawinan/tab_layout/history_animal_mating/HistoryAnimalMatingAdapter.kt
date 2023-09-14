package com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.HistoryPeranakan
import com.vt.vt.databinding.ItemKesehatanBinding

class HistoryAnimalMatingAdapter(private val dataHistoryPeranakan: List<HistoryPeranakan>) :
    RecyclerView.Adapter<HistoryAnimalMatingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemKesehatanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(dataHistoryPeranakan[position])
    }

    override fun getItemCount(): Int = dataHistoryPeranakan.size

    inner class ViewHolder(private val binding: ItemKesehatanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: HistoryPeranakan) {
            binding.tvDescriptionHealthRecord.text = data.description
        }
    }
}