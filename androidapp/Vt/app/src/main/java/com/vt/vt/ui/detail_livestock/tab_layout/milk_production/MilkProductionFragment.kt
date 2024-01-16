package com.vt.vt.ui.detail_livestock.tab_layout.milk_production

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.livestock.dto.HeightRecordsItem
import com.vt.vt.core.data.source.remote.livestock.dto.MilkProductionRecordsItem
import com.vt.vt.databinding.FragmentMilkProductionBinding
import com.vt.vt.databinding.FragmentRecordMilkProductionBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MilkProductionFragment : Fragment() {
    private var _binding: FragmentMilkProductionBinding? = null
    private val binding get() = _binding!!

    private val listMilkProductionAdapter by lazy { ListMilkProductionAdapter() }
    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMilkProductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())
        with(binding) {
            listMilkProduction.adapter = listMilkProductionAdapter
            listMilkProduction.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
        observerView()
    }

    private fun observerView() {
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.loading.progressBar.isVisible = it
        }
        livestockViewModel.getLivestockById.observe(viewLifecycleOwner) { result ->
            Log.d(TAG, "observerView: ${result?.milkRecords}")
            result?.let { data ->
                binding.dataEmpty.isEmpty.isVisible = data.milkRecords.isEmpty()
                listMilkProductionRecord(result.milkRecords)
            }
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listMilkProductionRecord(data: List<MilkProductionRecordsItem>) {
        listMilkProductionAdapter.submitList(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding
    }

    companion object {
        private val TAG = MilkProductionFragment::class.java.simpleName
    }
}