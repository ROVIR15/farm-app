package com.vt.vt.ui.daftar_perkawinan

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.breeding.BreedingResponseItem
import com.vt.vt.databinding.ItemListAnimalMatingsBinding
import com.vt.vt.ui.daftar_perkawinan.bottom_sheet_dialog.ListAnimalMatingDiffUtils

class ListAnimalMatingsAdapter(private val listBreedingViewModel: ListBreedingViewModel) :
    ListAdapter<BreedingResponseItem, ListAnimalMatingsAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val breedings = ArrayList<BreedingResponseItem>()
    fun setBreeding(newBreeding: List<BreedingResponseItem>) {
        val diffCallback = ListAnimalMatingDiffUtils(breedings, newBreeding)
        val diffBreeding = DiffUtil.calculateDiff(diffCallback)
        breedings.clear()
        breedings.addAll(newBreeding)
        diffBreeding.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemListAnimalMatingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bindTo(data)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bindTo(getItem(position))
        }
    }

    inner class ViewHolder(private val binding: ItemListAnimalMatingsBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        @SuppressLint("SetTextI18n")
        fun bindTo(data: BreedingResponseItem) {
            when (data.isActive) {
                true -> {
                    binding.tvAnimalActive.text = "Aktif"
                    binding.tvAnimalActive.setTextColor(Color.GREEN)
                    binding.tvAnimalActive.setBackgroundResource(R.color.green_grass)
                }

                false -> {
                    binding.tvAnimalActive.text = "Tidak Aktif"
                    binding.tvAnimalActive.setTextColor(Color.RED)
                    binding.tvAnimalActive.setBackgroundResource(R.color.btn_red)
                }
            }
            binding.createAt.text = data.createdAt.toString()
            binding.tvNameItemAnimalMating.text = data.name
            binding.btnUpdateList.setOnClickListener(this)
            binding.btnDeleteList.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.btn_update_list -> {
                    val getId = currentList[adapterPosition].id
                    val mBundle = Bundle()
                    mBundle.putInt("id", getId)
                    v.findNavController()
                        .navigate(
                            R.id.action_listAnimalMatingsFragment_to_breedingRecordFragment,
                            mBundle
                        )
                }

                R.id.btn_delete_list -> {
                    AlertDialog.Builder(itemView.context).setTitle("Delete")
                        .setIcon(R.drawable.ic_outline_delete_outline_24)
                        .setMessage("Are you sure delete this Information")
                        .setPositiveButton("Yes") { dialog, _ ->
                            val breedingId = currentList[adapterPosition].id
                            listBreedingViewModel.deleteBreedingById(breedingId.toString())
                            dialog.dismiss()
                        }.setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }.create().show()
                }
            }
        }

    }


    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BreedingResponseItem?> =
            object : DiffUtil.ItemCallback<BreedingResponseItem?>() {
                override fun areItemsTheSame(
                    oldItem: BreedingResponseItem,
                    newItem: BreedingResponseItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: BreedingResponseItem,
                    newItem: BreedingResponseItem
                ): Boolean {
                    return oldItem == newItem
                }

                override fun getChangePayload(
                    oldItem: BreedingResponseItem,
                    newItem: BreedingResponseItem
                ): Any? {
                    return super.getChangePayload(oldItem, newItem)
                }
            }
    }
}