package com.vt.vt.ui.anggaran.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.budget.ExpendituresItem
import com.vt.vt.databinding.ItemAnggaranBinding
import com.vt.vt.ui.pengeluaran.ExpenditureViewModel
import com.vt.vt.utils.convertRupiah
import java.text.NumberFormat
import java.util.Locale

class ListBudgetExpenditureAdapter(private val expenditureViewModel: ExpenditureViewModel) :
    ListAdapter<ExpendituresItem, ListBudgetExpenditureAdapter.BudgetViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = ItemAnggaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budgetItem = getItem(position)
        holder.bind(budgetItem)
    }

    inner class BudgetViewHolder(private val binding: ItemAnggaranBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        fun bind(expendituresItem: ExpendituresItem) {
            val amountExpenditure = expendituresItem.amount?.convertRupiah()
            binding.tvBudget.text = amountExpenditure
            binding.btnOptionsExpenditure.setOnClickListener(this@BudgetViewHolder)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_options_expenditure -> {
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
                    return true
                }

                R.id.menu_delete -> {
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val getId = currentList[adapterPosition].id
                            expenditureViewModel.deleteExpenditure(getId.toString())
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
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ExpendituresItem> =
            object : DiffUtil.ItemCallback<ExpendituresItem>() {
                override fun areItemsTheSame(
                    oldItem: ExpendituresItem,
                    newItem: ExpendituresItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: ExpendituresItem,
                    newItem: ExpendituresItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}