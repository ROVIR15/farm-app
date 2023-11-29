package com.vt.vt.ui.pemberian_ternak.bottondialog

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.databinding.FragmentConfirmationFeedingBottomDialogBinding
import com.vt.vt.ui.barang_dan_jasa.ListBarangDanJasaViewModel
import com.vt.vt.ui.pemberian_ternak.FeedingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmationFeedingBottomDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentConfirmationFeedingBottomDialogBinding? = null
    private val binding get() = _binding!!

    private val feedingViewModel by viewModels<FeedingViewModel>()
    private val listBarangDanJasaViewModel by viewModels<ListBarangDanJasaViewModel>()
    private val listDetailFeedingAdapter by lazy {
        ListDetailFeedingAdapter(
            listBarangDanJasaViewModel, feedingViewModel
        )
    }

    private var blockId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentConfirmationFeedingBottomDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blockId = arguments?.getInt("blockId")
        listBarangDanJasaViewModel.getAllProducts()
        with(binding) {
            listFeeding.adapter = listDetailFeedingAdapter
            listFeeding.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
        observerView()
    }

    private fun observerView() {
        listBarangDanJasaViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        feedingViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        feedingViewModel.feedingEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        feedingViewModel.load().observe(viewLifecycleOwner) { listConsumptionRecord ->
            listBarangDanJasaViewModel.productsEmitter.observe(viewLifecycleOwner) {
                if (blockId != null) {
                    val data = listConsumptionRecord[blockId]
                    Log.d("FEEDING", "data list in dialog : ${listConsumptionRecord}")
                    data?.let { listConsumptionRecord(it.toMutableList()) }
                    binding.btnSave.apply {
                        isEnabled = !data.isNullOrEmpty()
                        setBackgroundColor(
                            if (data.isNullOrEmpty()) Color.GRAY else ContextCompat.getColor(
                                requireActivity(),
                                R.color.white
                            )
                        )
                        setOnClickListener {
                            feedingViewModel.createFeedingRecord(
                                blockId = blockId!!,
                                consumptionRecord = data
                            )
                        }
                    }
                }
            }
        }
        feedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listConsumptionRecord(data: List<ConsumptionRecordItem>) {
        listDetailFeedingAdapter.submitData(data)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnSave.setBackgroundColor(
            if (isLoading) Color.GRAY
            else ContextCompat.getColor(requireActivity(), R.color.purple_500)
        )
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}