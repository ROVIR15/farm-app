package com.vt.vt.ui.bottom_navigation.livestock.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.sleds.model.SledOptionResponseItem
import com.vt.vt.databinding.FragmentListSledBottomDialogBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.file_provider.datakandang.DataKandangViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListSledBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentListSledBottomDialogBinding? = null
    private val binding get() = _binding!!

    private val listSledBottomSheetAdapter by lazy { ListSledBottomSheetAdapter() }
    private val sledViewModel by viewModels<DataKandangViewModel>()
    private val livestockViewModel by viewModels<LivestockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSledBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveLivestockArgument = arguments?.getInt("livestockId")
        sledViewModel.getSledOptions()
        with(binding) {
            listOptionSled.adapter = listSledBottomSheetAdapter
            listOptionSled.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

            listSledBottomSheetAdapter.onClickListener = { sled ->
                btnSave.setOnClickListener {
                    val livestockId = receiveLivestockArgument
                    val sledId = sled.id
                    val blockAreaId = sled.blockAreaId
                    if (livestockId != null && sledId != null && blockAreaId != null) {
                        livestockViewModel.livestockMoveSled(livestockId, sledId, blockAreaId)
                    } else {
                        Toast.makeText(requireActivity(), "Pilih Kandang", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            btnCancel.setOnClickListener { dismiss() }
        }
        observerView()
    }

    private fun observerView() {
        sledViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        sledViewModel.sledOptionsEmitter.observe(viewLifecycleOwner) { sled ->
            listSledOptions(sled)
        }
        livestockViewModel.livestockMoveSledEmitter.observe(viewLifecycleOwner) { livestockMove ->
            Toast.makeText(requireActivity(), livestockMove.message, Toast.LENGTH_SHORT).show()
            dismiss()
        }
        sledViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listSledOptions(data: List<SledOptionResponseItem>) {
        listSledBottomSheetAdapter.submitList(data)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.isVisible = isLoading
        binding.btnSave.setBackgroundColor(
            if (isLoading) Color.GRAY
            else ContextCompat.getColor(requireActivity(), R.color.purple_500)
        )
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}