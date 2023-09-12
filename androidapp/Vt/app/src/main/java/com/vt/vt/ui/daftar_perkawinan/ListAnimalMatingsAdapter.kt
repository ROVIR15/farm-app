package com.vt.vt.ui.daftar_perkawinan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatings
import com.vt.vt.databinding.ItemListAnimalMatingsBinding

class ListAnimalMatingsAdapter(private val animalMatings: List<AnimalMatings>) :
    RecyclerView.Adapter<ListAnimalMatingsAdapter.ViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemListAnimalMatingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(animalMatings[position])
    }

    override fun getItemCount(): Int = animalMatings.size

    inner class ViewHolder(private val binding: ItemListAnimalMatingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(data: AnimalMatings) {
            binding.tvNameItemAnimalMating.text = data.pasangan
            binding.tvCreatedAtItemListAnimalMatings.text = data.createdAt
            binding.btnUpdateList.setOnClickListener(this@ListAnimalMatingsAdapter)
            binding.btnDeleteList.setOnClickListener(this@ListAnimalMatingsAdapter)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_update_list -> {
                v.findNavController()
                    .navigate(R.id.action_listAnimalMatingsFragment_to_rekamPerkawinanFragment)
            }

            R.id.btn_delete_list -> {
                Toast.makeText(v.context, "no action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}