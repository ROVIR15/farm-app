package com.vt.vt.ui.bottom_navigation.keuangan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.keuangan.Pengeluaran
import com.vt.vt.databinding.ItemFinanceSummaryBinding
class KeuanganAdapter(val data: List<Pengeluaran>) :
    RecyclerView.Adapter<KeuanganAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemFinanceSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: ItemFinanceSummaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: Pengeluaran) {
            binding.appCompatTextView4.text = data.title
            binding.itemNilaiRealisasi.text = data.budget.toString()
            itemView.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_keuangan_to_anggaranFragment)
            }
        }
    }
}