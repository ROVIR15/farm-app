package com.vt.vt.ui.detail_area_block

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.list_animal_cage.AnimalCage
import com.vt.vt.databinding.ItemDetailAreaBlockBinding

class ListDetailAreaBlockAdapter(private val animalCage: List<AnimalCage>) :
    RecyclerView.Adapter<ListDetailAreaBlockAdapter.ViewHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemDetailAreaBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(animalCage[position])
    }

    override fun getItemCount(): Int {
        return animalCage.size
    }

    inner class ViewHolder(private val binding: ItemDetailAreaBlockBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(animalCage: AnimalCage) {
            with(binding) {
                tvTitleAnimalCage.text = animalCage.title
                descriptionItemDetailAreaBlock.text = animalCage.description
                btnOptionsDetailArea.setOnClickListener(this@ListDetailAreaBlockAdapter)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_options_detail_area -> {
                val popup = PopupMenu(v.context, v)
                popup.inflate(R.menu.menu_options_detail_area_block)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_edit_animal_cage -> {
                            // navigation controller here
                            v.findNavController()
                                .navigate(R.id.action_detailAreaBlockFragment_to_editAreaBlockFragment)
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
        }
    }

}