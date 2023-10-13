package com.vt.vt.ui.rekam_perkawinan.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentChangeBreedingStatusDialogBinding
import com.vt.vt.ui.rekam_perkawinan.BreedingRecordFragment
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.core.data.source.base.bottomdialog.listener.OnBottomSheetListener
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeBreedingStatusDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChangeBreedingStatusDialogBinding? = null
    private val binding get() = _binding!!

    private var onBottomSheetDialogListener: OnBottomSheetListener? = null
    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()

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
        val receiveId = arguments?.getInt("breedingId")
        dialog?.setCancelable(false)
        with(binding) {
            ivDatePickerAnimalMatings.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvAddAnimalMatingDate)
            }
            btnSave.setOnClickListener {
                val createdAt = tvAddAnimalMatingDate.text.toString().trim()
                val category = edtAreaCategory.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                if (createdAt.isNotEmpty() && description.isNotEmpty()) {
                    recordBreedingViewModel.updatePregnancy(receiveId.toString(), createdAt, false, description)
                    dismiss()
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnBatal.setOnClickListener {
                dismiss()
            }
        }
        observerView()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onBottomSheetDialogListener?.onBottomSheetClose()
    }

    private fun observerView(){
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner){
            showLoading(it)
        }
        recordBreedingViewModel.updatePregnancyEmitter.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSave.isEnabled = !state
            btnBatal.isEnabled = !state

            btnSave.setBackgroundColor(
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