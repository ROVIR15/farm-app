package com.vt.vt.ui.rekam_perkawinan.dialog

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
import com.vt.vt.databinding.FragmentAddBreedingDialogBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBreedingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddBreedingDialogBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()

    private var sledId: Int = 0
    private var blockId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("breedingId")
        with(binding) {
            ivDatePickerAnimalChild.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvAnimalChildDate)
            }
            spinnerGenderAdapter()
            btnSaveAnimalChild.setOnClickListener {
                val name = edtNameAnimalChild.text.toString().trim()
                val nation = edtCountry.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                val weight = edtWeight.text.toString().trim().toDouble()
                val height = edtHeight.text.toString().trim().toDouble()
                val gender = spinnerGender.selectedItemId.toInt()
                val createAt = formatterDateFromCalendar(tvAnimalChildDate.text.toString().trim())
                if (name.isNotEmpty() && nation.isNotEmpty() && description.isNotEmpty() && createAt.isNotEmpty() && gender != 0 && blockId != 0 && sledId != 0) {
                    if (receiveId != null) {
                        recordBreedingViewModel.createLambing(
                            receiveId.toInt(),
                            name,
                            gender,
                            nation,
                            description,
                            blockId,
                            weight, height,
                            sledId,
                            createAt
                        )
                    }
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnCancelAnimalChild.setOnClickListener { dismiss() }
        }
        observerView()
    }

    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        recordBreedingViewModel.createBreedingEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireActivity(), "${it.status} menambahkan anak hewan", Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        detailAreaBlockViewModel.apply {
            getSleds()
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            sledItems.observe(viewLifecycleOwner) { sleds ->
                val namesArray = sleds.map { data ->
                    data.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, namesArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerBlock.adapter = adapter
                binding.spinnerBlock.selected { position ->
                    binding.edtArea.setText(sleds[position].blockAreaName)
                    sledId = sleds[position].id
                    blockId = sleds[position].blockAreaId
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(), it ?: "Unkown Error", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun spinnerGenderAdapter() {
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.gender_animal, R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinnerGender.adapter = adapter
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveAnimalChild.isEnabled = !state
            btnCancelAnimalChild.isEnabled = !state

            btnSaveAnimalChild.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )
            progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}