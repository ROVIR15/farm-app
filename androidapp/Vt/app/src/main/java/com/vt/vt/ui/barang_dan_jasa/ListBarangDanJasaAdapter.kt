package com.vt.vt.ui.barang_dan_jasa

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
import com.vt.vt.core.data.source.remote.products.model.ProductResponseItem
import com.vt.vt.databinding.ItemBarangDanJasaBinding

class ListBarangDanJasaAdapter(private val viewModel: ListBarangDanJasaViewModel) :
    ListAdapter<ProductResponseItem, ListBarangDanJasaAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemBarangDanJasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(private val binding: ItemBarangDanJasaBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bind(data: ProductResponseItem) {
            binding.categoryName.text = data.categoryName.toString()
            binding.productsName.text = data.productName.toString()
            binding.unitMeasurement.text = data.unitMeasurement.toString()
            binding.btnOptionsItemGoodsAndServices.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_item_goods_and_services -> {
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
                    val getItemId = currentList[adapterPosition]
                    DataBarangDanJasaFragment.IS_UPDATE_DATA = true
                    val mBundle = Bundle().apply {
                        getItemId.productId?.let { putInt("id", it) }
                    }
                    itemView.findNavController()
                        .navigate(
                            R.id.action_dataBarangDanJasaFragment_to_addDataBarangJasaFragment,
                            mBundle
                        )
                    return true
                }

                R.id.menu_delete -> {
                    DataBarangDanJasaFragment.IS_UPDATE_DATA = false
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val productId = currentList[adapterPosition]
                            viewModel.deleteProduct(
                                productId.productId.toString()
                            )
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
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ProductResponseItem?> =
            object : DiffUtil.ItemCallback<ProductResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: ProductResponseItem,
                    newItem: ProductResponseItem
                ): Boolean {
                    return oldItem.productId == newItem.productId
                }

                override fun areContentsTheSame(
                    oldItem: ProductResponseItem,
                    newItem: ProductResponseItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}