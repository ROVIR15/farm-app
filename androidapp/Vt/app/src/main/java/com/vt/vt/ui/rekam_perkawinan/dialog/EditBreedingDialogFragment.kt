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
import com.vt.vt.databinding.FragmentEditBreedingDialogBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatDateBreeding
import com.vt.vt.utils.formatterDateFromCalendar
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
        with(binding) {
            ivDatePickerAnimalMatings.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), date)
            }
            spinnerEditBreeding.selected {
                edtAreaEditBreeding.setText(it)
            }
            btnSaveAddAnimalMating.setOnClickListener {
                val updateAt = formatterDateFromCalendar(date.text.toString().trim())
                val blockArea = edtAreaEditBreeding.text.toString().trim()
                if (updateAt.isNotEmpty() && blockArea.isNotEmpty() && !getSled.isNullOrEmpty() && !getLivestockMale.isNullOrEmpty() && !getLivestockFemale.isNullOrEmpty()) {
                    println("hore")
                    // PUT API HERE...
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
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
        recordBreedingViewModel.breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
            val (desiredLivestockMaleId, desiredLivestockFemaleId) = breeding.run {
                livestockMaleId to livestockFemaleId
            }
            val desiredSledId = breeding.sledId?.toInt()
            binding.date.text = formatDateBreeding(breeding.createdAt.toString(), "dd-MMMM-yyyy")
            sledViewModel.sledItems.observe(viewLifecycleOwner) { sled ->
                with(binding) {
                    val nameArray = sled.map {
                        it.name
                    }.toTypedArray()
                    val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerEditBreeding.adapter = adapter
                    val desiredPosition = sled.indexOfFirst {
                        it.id == desiredSledId
                    }
                    if (desiredPosition != -1) {
                        spinnerEditBreeding.selected {
                            getSled = sled[it].name
                            edtAreaEditBreeding.setText(sled[desiredPosition].blockAreaName)
                        }
                    } else {
                        spinnerEditBreeding.selected { position ->
                            getSled = sled[position].name
                            edtAreaEditBreeding.setText(sled[position].blockAreaName)
                        }
                    }
                }
            }
            livestockViewModel.livestockItems.observe(viewLifecycleOwner) { livestock ->
                with(binding) {
                    val nameArray = livestock.map { it.name }.toTypedArray()
                    val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerChooseMaleAddAnimalMating.adapter = adapter
                    spinnerChooseFemaleAddAnimalMating.adapter = adapter
                    val selectedIndex = livestock.indexOfFirst {
                        it.id == desiredLivestockMaleId
                    }
                    val selectedFemaleIndex = livestock.indexOfFirst {
                        it.id == desiredLivestockFemaleId
                    }
                    spinnerChooseMaleAddAnimalMating.selected { position ->
                        getLivestockMale = livestock[position].name
                    }
                    spinnerChooseFemaleAddAnimalMating.selected { position ->
                        getLivestockFemale = livestock[position].name
                    }
                    spinnerChooseMaleAddAnimalMating.setSelection(selectedIndex.takeIf { it != -1 }
                        ?: 0)
                    spinnerChooseFemaleAddAnimalMating.setSelection(selectedFemaleIndex.takeIf { it != -1 }
                        ?: 0)
                }
            }
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}