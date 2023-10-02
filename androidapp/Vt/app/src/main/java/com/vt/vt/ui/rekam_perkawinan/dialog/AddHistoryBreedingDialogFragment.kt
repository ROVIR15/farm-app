package com.vt.vt.ui.rekam_perkawinan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.databinding.FragmentAddHistoryBreedingDialogBinding
import com.vt.vt.utils.PickDatesUtils

class AddHistoryBreedingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddHistoryBreedingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHistoryBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            ivDatePickerHistoryAnimalMating.setOnClickListener {
                PickDatesUtils.setupDatePicker(
                    requireActivity(),
                    tvHistoryAnimalMatingDate
                )
            }
            btnSaveAnimalMating.setOnClickListener {
                val description = edtDescriptionHistoryAnimalMating.text.toString().trim()
                val pickDates = tvHistoryAnimalMatingDate.text.toString().trim()
//                if (pickDates.isNotEmpty() && description.isNotEmpty()) {
//
//                } else {
//                    Toast.makeText(requireActivity(), "Lengkapi Kolom", Toast.LENGTH_SHORT).show()
//                }
                dismiss()
            }
            btnCancelAnimalMating.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}