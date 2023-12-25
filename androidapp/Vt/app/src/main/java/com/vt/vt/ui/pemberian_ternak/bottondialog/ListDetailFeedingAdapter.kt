package com.vt.vt.ui.pemberian_ternak.bottondialog

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.core.data.source.remote.feeding_record.dto.ConsumptionRecordItem
import com.vt.vt.databinding.ItemDetailFeedingBinding
import com.vt.vt.ui.barang_dan_jasa.ListItemsAndServiceViewModel
import com.vt.vt.ui.pemberian_ternak.FeedingViewModel

class ListDetailFeedingAdapter(
    private val listItemsAndServiceViewModel: ListItemsAndServiceViewModel,
    private val feedingViewModel: FeedingViewModel
) : RecyclerView.Adapter<ListDetailFeedingAdapter.ViewHolder>() {
    private val listConsumptionRecord = ArrayList<ConsumptionRecordItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(consumptionRecordList: List<ConsumptionRecordItem>?) {
        if (consumptionRecordList == null) return
        listConsumptionRecord.clear()
        listConsumptionRecord.addAll(consumptionRecordList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDetailFeedingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: ConsumptionRecordItem) {
            val skuId = data.skuId ?: return
            val productName = listItemsAndServiceViewModel.getProductBySkuId(skuId)
            binding.categoriesDetail.text = when (data.feedCategory) {
                1 -> "Kategori Hijauan"
                2 -> "Kategori Kimia"
                3 -> "Kategori Vitamin"
                4 -> "Kategori Tambahan"
                else -> "Unknown Category"
            }
            Log.d("FEEDING", "name product : ${productName}")
            binding.nameProduct.text = productName?.productName
            binding.stockProduct.text = "${data.score} Satuan"
            binding.btnDelete.setOnClickListener {
                val adapterPos = adapterPosition
                data.blockAreaId?.let { blockId ->
                    feedingViewModel.deleteByItem(
                        blockId,
                        adapterPos
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemDetailFeedingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listConsumptionRecord[position]
        holder.bindTo(data)
    }

    override fun getItemCount(): Int = listConsumptionRecord.size
}