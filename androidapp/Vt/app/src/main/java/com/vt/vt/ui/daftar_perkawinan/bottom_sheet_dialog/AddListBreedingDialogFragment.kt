package com.vt.vt.ui.daftar_perkawinan.bottom_sheet_dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddListBreedingDialogBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.daftar_perkawinan.ListBreedingViewModel
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddListBreedingDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentAddListBreedingDialogBinding? = null
    private val binding get() = _binding!!

    private val listBreedingViewModel by viewModels<ListBreedingViewModel>()
    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()
    private val livestockViewModel by viewModels<LivestockViewModel>()

    private var sledId: Int = 0
    private var blockId: Int = 0
    private var livestockMaleId: Int? = null
    private var livestockFemaleId: Int? = null

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
        livestockViewModel.getLivestocks()
        livestockViewModel.getLivestocksMale()
        livestockViewModel.getLivestocksFemale()
        with(binding) {
            ivDatePickerAnimalMatings.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvAddAnimalMatingDate)
            }
            btnSaveAddAnimalMating.setOnClickListener {
                val createdAt = tvAddAnimalMatingDate.text.toString().trim()
                if (createdAt.isNotEmpty()) {
                    listBreedingViewModel.createBreeding(
                        createdAt,
                        livestockMaleId,
                        livestockFemaleId,
                        sledId,
                        blockId
                    )
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnCancelAddAnimalMating.setOnClickListener {
                dismiss()
            }
        }
        observerView()
    }


    private fun observerView() {
        listBreedingViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        listBreedingViewModel.createBreedingEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "${it.status} menambahkan perkawinan",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
        listBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                it.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
        detailAreaBlockViewModel.apply {
            getSleds()
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
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
                    sledId = sleds[position].id
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
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            livestockViewModel.livestocksMaleEmitter.observe(viewLifecycleOwner) { livestockMales ->
                val nameArray = livestockMales.map {
                    it.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerChooseMaleAddAnimalMating.adapter = adapter
                binding.spinnerChooseMaleAddAnimalMating.selected { position ->
                    livestockMales[position].name
                    livestockMaleId = livestockMales[position].id.toInt()
                }
            }
            livestockViewModel.livestocksFemaleEmitter.observe(viewLifecycleOwner) { livestockFemales ->
                val nameArray = livestockFemales.map {
                    it.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerChooseFemaleAddAnimalMating.adapter = adapter
                binding.spinnerChooseFemaleAddAnimalMating.selected { position ->
                    livestockFemales[position].name
                    livestockFemaleId = livestockFemales[position].id
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

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveAddAnimalMating.isEnabled = !state
            btnCancelAddAnimalMating.isEnabled = !state

            btnSaveAddAnimalMating.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )
            progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}