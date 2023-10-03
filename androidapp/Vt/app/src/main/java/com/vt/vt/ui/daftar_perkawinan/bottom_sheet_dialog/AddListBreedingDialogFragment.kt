package com.vt.vt.ui.daftar_perkawinan.bottom_sheet_dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddListBreedingDialogBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddListBreedingDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddListBreedingDialogBinding? = null
    private val binding get() = _binding!!

    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()

    private var Id: Int? = 0
    private var blockId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddListBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            spinnerAdapter(spinnerChooseMaleAddAnimalMating)
            spinnerAdapter(spinnerChooseFemaleAddAnimalMating)
            btnSaveAddAnimalMating.setOnClickListener { }
            btnCancelAddAnimalMating.setOnClickListener { }
        }
        observerView()
    }


    private fun observerView() {
        detailAreaBlockViewModel.apply {
            getSleds()
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.btnSaveAddAnimalMating.isEnabled = !isLoading
                binding.btnSaveAddAnimalMating.setBackgroundColor(
                    if (isLoading) Color.GRAY
                    else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
                )
                binding.progressBarAnimalMating.visibility =
                    if (isLoading) View.VISIBLE else View.GONE
            }
            sledItems.observe(viewLifecycleOwner) { sleds ->
                val namesArray = sleds.map { data ->
                    data.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, namesArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCageAddAnimalMating.adapter = adapter
                binding.spinnerCageAddAnimalMating.selected { position ->
                    binding.edtAreaAddAnimalMating.setText(sleds[position].blockAreaName)
                    Id = sleds[position].id
                    blockId = sleds[position].blockAreaId
                }

            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    it ?: "Unkown Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun spinnerAdapter(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(
                R.layout.item_spinner
            )
            spinner.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}