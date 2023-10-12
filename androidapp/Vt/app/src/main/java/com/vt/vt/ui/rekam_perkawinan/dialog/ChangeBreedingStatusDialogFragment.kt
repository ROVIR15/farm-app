package com.vt.vt.ui.rekam_perkawinan.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.databinding.FragmentChangeBreedingStatusDialogBinding
import com.vt.vt.ui.rekam_perkawinan.BreedingRecordFragment
import com.vt.vt.utils.PickDatesUtils

class ChangeBreedingStatusDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChangeBreedingStatusDialogBinding? = null
    private val binding get() = _binding!!

    private var onBottomSheetDialogListener: ChangeBreedingStatusListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeBreedingStatusDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        with(binding) {
            ivDatePickerAnimalMatings.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvAddAnimalMatingDate)
            }
            btnSave.setOnClickListener {
                val createdAt = tvAddAnimalMatingDate.text.toString().trim()
                val category = edtAreaCategory.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                if (category.isNotEmpty() && description.isNotEmpty()) {
                    onBottomSheetDialogListener?.onTextSelected(
                        createdAt, category, description
                    )
                    dismiss()
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnBatal.setOnClickListener {
                onBottomSheetDialogListener?.onTextSelected(
                    null, null, null
                )
                dismiss()
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment

        if (fragment is BreedingRecordFragment) {
            this.onBottomSheetDialogListener = fragment.changeStatusBreedingListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.onBottomSheetDialogListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface ChangeBreedingStatusListener {
        fun onTextSelected(date: String?, categoryText: String?, description: String?)
    }
}