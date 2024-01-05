package com.vt.vt.ui.rekam_perkawinan.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponseItem
import com.vt.vt.core.data.source.remote.sleds.dto.SledsResponseItem
import com.vt.vt.databinding.FragmentEditBreedingDialogBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditBreedingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEditBreedingDialogBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    private val livestockViewModel by viewModels<LivestockViewModel>()
    private val sledViewModel by viewModels<DetailAreaBlockViewModel>()

    private var getSled: String? = null
    private var getLivestockMale: String? = null
    private var getLivestockFemale: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("breedingId")
        recordBreedingViewModel.getBreedingById(receiveId.toString())
        sledViewModel.getSleds()
        livestockViewModel.getLivestocks()
        livestockViewModel.getLivestocksMale()
        livestockViewModel.getLivestocksFemale()
        with(binding) {
            spinnerEditBreeding.selected {
                edtAreaEditBreeding.setText(it)
            }
            btnSaveEditAnimalMating.setOnClickListener {
                val blockArea = edtAreaEditBreeding.text.toString().trim()
                if (blockArea.isNotEmpty() && !getSled.isNullOrEmpty() && !getLivestockMale.isNullOrEmpty() && !getLivestockFemale.isNullOrEmpty()) {
                    Toast.makeText(requireActivity(), "UPDATE (API) BELOM ADA", Toast.LENGTH_SHORT)
                        .show()
                    // PUT API HERE...
                } else {
                    Toast.makeText(requireActivity(), R.string.please_fill_all_column, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnCancelAddAnimalMating.setOnClickListener { dismiss() }
        }
        observerView()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        recordBreedingViewModel.breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
            sledViewModel.sledItems.observe(viewLifecycleOwner) { sled ->
                with(binding) {
                    val nameArray = sled.map {
                        it.name
                    }.toTypedArray()
                    val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    val originalSledId = breeding.sledId
                    val position = findPositionSledById(sled, originalSledId?.toInt())
                    if (originalSledId != null) {
                        if (position != -1) {
                            binding.spinnerEditBreeding.setSelection(position)
                        }
                    }
                    spinnerEditBreeding.adapter = adapter
                    spinnerEditBreeding.selected { pos ->
                        getSled = sled[pos].name
                        edtAreaEditBreeding.setText(sled[pos].blockAreaName)
                    }
                }
            }
            livestockViewModel.livestocksMaleEmitter.observe(viewLifecycleOwner) { livestockMales: List<LivestockResponseItem> ->
                val nameArray = livestockMales.map {
                    it.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerChooseMaleAddAnimalMating.adapter = adapter
                val desiredId = breeding?.livestockMale?.id
                val position = findPositionById(livestockMales, desiredId)
                if (desiredId != null) {
                    if (position != -1) {
                        binding.spinnerChooseMaleAddAnimalMating.setSelection(position)
                    }
                }
                binding.spinnerChooseMaleAddAnimalMating.selected { pos ->
                    getLivestockMale = livestockMales[pos].name
                }
            }
            livestockViewModel.livestocksFemaleEmitter.observe(viewLifecycleOwner) { livestockFemales ->
                val nameArray = livestockFemales.map {
                    it.name
                }.toTypedArray()
                val desiredId = breeding?.livestockFemale?.id
                val position = findPositionById(livestockFemales, desiredId)
                if (desiredId != null) {
                    if (position != -1) {
                        binding.spinnerChooseFemaleAddAnimalMating.setSelection(position)
                    }
                }
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerChooseFemaleAddAnimalMating.adapter = adapter
                binding.spinnerChooseFemaleAddAnimalMating.selected { pos ->
                    getLivestockFemale = livestockFemales[pos].name
                }
            }
        }

        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun findPositionById(itemList: List<LivestockResponseItem>, desiredId: Int?): Int {
        for ((index, item) in itemList.withIndex()) {
            if (item.id == desiredId) {
                return index
            }
        }
        return -1
    }

    private fun findPositionSledById(itemList: List<SledsResponseItem>, desiredId: Int?): Int {
        for ((index, item) in itemList.withIndex()) {
            if (item.id == desiredId) {
                return index
            }
        }
        return -1
    }
}