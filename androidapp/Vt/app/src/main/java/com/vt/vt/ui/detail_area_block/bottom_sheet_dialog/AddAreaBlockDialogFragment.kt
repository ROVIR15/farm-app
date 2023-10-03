package com.vt.vt.ui.detail_area_block.bottom_sheet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddAreaBlockDialogBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAreaBlockDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddAreaBlockDialogBinding? = null
    private val binding get() = _binding!!

    private val sledViewModel by viewModels<DetailAreaBlockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAreaBlockDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnSaveAddAnimalCage.setOnClickListener {
                Toast.makeText(requireActivity(), "no action", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            btnCancelAddAnimalCage.setOnClickListener {
                dismiss()
            }
            btnAddNewAnimalCage.setOnClickListener {
                val parentFragment = parentFragment
                if (parentFragment != null) {
                    parentFragment.findNavController()
                        .navigate(R.id.action_detailAreaBlockFragment_to_dataKandangFragment)
                    dismiss()
                } else {
                    activity?.let { activity ->
                        activity.findNavController(R.id.nav_host_fragment_activity_main)
                            .navigate(R.id.action_detailAreaBlockFragment_to_dataKandangFragment)
                        dismiss()
                    }
                }
                dismiss()
            }
        }
        observerView()
    }

    private fun observerView() {
        sledViewModel.getSleds()
        sledViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.progressBarAddAreaBlock.isVisible = it
        }
        sledViewModel.sledItems.observe(viewLifecycleOwner) { sleds ->
            val nameArrays = sleds.map {
                it.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCageAddAnimalCage.adapter = adapter
        }
        sledViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}