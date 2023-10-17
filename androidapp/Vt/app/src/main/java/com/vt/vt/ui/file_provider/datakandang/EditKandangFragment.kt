package com.vt.vt.ui.file_provider.datakandang

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditKandangBinding
import com.vt.vt.ui.penyimpan_ternak.PenyimpanTernakViewModel
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditKandangFragment : Fragment() {
    private var _binding: FragmentEditKandangBinding? = null
    private val binding get() = _binding!!

    private val blockAreaViewModel by viewModels<PenyimpanTernakViewModel>()
    private val sledsViewModel by viewModels<DataKandangViewModel>()

    private var sledsId: Int? = null
    private var blockAreaId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditKandangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sledsId = arguments?.getInt("id")
        sledsViewModel.getSledById(sledsId.toString())
        blockAreaViewModel.getAllBlockAndArea()
        with(binding) {
            this.appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Edit Kandang"
                toolbar.setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            btnSimpan.setOnClickListener {
                val name = edtNamaKandang.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    sledsViewModel.updateSledById(
                        sledsId.toString(), blockAreaId, name, description
                    )
                } else {
                    Toast.makeText(requireContext(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        observerView()
    }

    private fun observerView() {
        sledsViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        sledsViewModel.getSledById.observe(viewLifecycleOwner) { sled ->
            val id = sled.blockAreaId
            blockAreaViewModel.allBlockAndAreas.observe(viewLifecycleOwner) { blockArea ->
                val blocksArray = blockArea.map {
                    it.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, blocksArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCageCategory.adapter = adapter
                val selectedIndex = blockArea.indexOfFirst {
                    it.id == id
                }
                binding.spinnerCageCategory.selected {
                    blockAreaId = blockArea[it].id
                }
                if (selectedIndex != -1) {
                    binding.spinnerCageCategory.setSelection(selectedIndex)
                } else {
                    binding.spinnerCageCategory.setSelection(0)
                }
            }
            with(binding) {
                edtNamaKandang.setText(sled.name.trim())
                edtDescription.setText(sled.description.trim())
            }
        }
        sledsViewModel.updateSledById.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.status, Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
        sledsViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpan.isEnabled = !state
            btnBatal.isEnabled = !state

            btnSimpan.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}