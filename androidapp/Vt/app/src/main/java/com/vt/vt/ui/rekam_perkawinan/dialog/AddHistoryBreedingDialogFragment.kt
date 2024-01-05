package com.vt.vt.ui.rekam_perkawinan.dialog

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
import com.vt.vt.databinding.FragmentAddHistoryBreedingDialogBinding
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddHistoryBreedingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddHistoryBreedingDialogBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHistoryBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("breedingId")
        with(binding) {
            ivDatePickerHistoryAnimalMating.setOnClickListener {
                PickDatesUtils.setupDatePicker(
                    requireActivity(), tvHistoryAnimalMatingDate
                )
            }
            btnSaveAnimalMating.setOnClickListener {
                val description = edtDescriptionHistoryAnimalMating.text.toString().trim()
                val formatter = tvHistoryAnimalMatingDate.text.toString().trim()
                val createAt = formatterDateFromCalendar(formatter)
                if (createAt.isNotEmpty() && description.isNotEmpty()) {
                    if (receiveId != null) {
                        recordBreedingViewModel.createHistoryBreeding(
                            createAt, receiveId, description
                        )
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.please_fill_all_column, Toast.LENGTH_SHORT).show()
                }
            }
            btnCancelAnimalMating.setOnClickListener { dismiss() }
        }
        observerView()
    }

    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        recordBreedingViewModel.createBreedingEmitter.observe(viewLifecycleOwner) { isCreated ->
            Toast.makeText(
                requireContext(),
                "${isCreated.status} menambahkan catatan",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveAnimalMating.isEnabled = !state
            btnCancelAnimalMating.isEnabled = !state

            btnSaveAnimalMating.setBackgroundColor(
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