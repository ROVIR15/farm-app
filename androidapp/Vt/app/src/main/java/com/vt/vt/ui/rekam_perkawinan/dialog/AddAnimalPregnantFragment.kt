package com.vt.vt.ui.rekam_perkawinan.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.databinding.FragmentAddAnimalPregnantBinding
import com.vt.vt.ui.rekam_perkawinan.BreedingRecordFragment
import com.vt.vt.utils.PickDatesUtils

class AddAnimalPregnantFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddAnimalPregnantBinding? = null
    private val binding get() = _binding!!
    private var onBottomSheetDialogListener: OnBottomSheetDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAnimalPregnantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        with(binding) {
            ivDatePickerAnimalPregnant.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvAnimalPregnantDate)
            }
            btnSaveAnimalPregnant.setOnClickListener {
                onBottomSheetDialogListener?.onDateSelected(
                    tvAnimalPregnantDate.text.toString().trim()
                )
                dialog?.dismiss()
            }
            btnCancelAnimalPregnant.setOnClickListener {
                onBottomSheetDialogListener?.onDateSelected(
                    null
                )
                dialog?.dismiss()
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment

        if (fragment is BreedingRecordFragment) {
            this.onBottomSheetDialogListener = fragment.onBottomSheetDialogListener
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.onBottomSheetDialogListener = null
    }

    interface OnBottomSheetDialogListener {
        fun onDateSelected(text: String?)
    }
}