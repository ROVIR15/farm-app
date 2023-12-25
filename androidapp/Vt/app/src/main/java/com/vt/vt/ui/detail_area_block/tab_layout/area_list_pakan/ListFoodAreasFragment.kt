package com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.block_areas.dto.FeedingRecordsItem
import com.vt.vt.databinding.FragmentListFoodAreasBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFoodAreasFragment : Fragment() {

    private var _binding: FragmentListFoodAreasBinding? = null
    private val binding get() = _binding!!

    private val detailAreaBlockViewModel: DetailAreaBlockViewModel by viewModels()
    private val dataAreaViewModel by viewModels<DataAreaViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFoodAreasBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("areaBlockId")
        dataAreaViewModel.getBlockArea(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        dataAreaViewModel.getBlockArea.observe(viewLifecycleOwner) {
            if (it.feedingRecords != null) {
                println("feed view ${it.feedingRecords}")
                listPakan(it.feedingRecords)
            }
        }
    }

    private fun listPakan(data: List<FeedingRecordsItem>) {
        val adapter = ListFoodAreasAdapter()
        adapter.submitList(data)
        with(binding) {
            listPakan.adapter = adapter
            listPakan.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}