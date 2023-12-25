package com.vt.vt.ui.detail_livestock.tab_layout.pakan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.block_areas.dto.FeedingRecordsItem
import com.vt.vt.databinding.FragmentPakanBinding
import com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan.ListFoodAreasAdapter
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PakanFragment : Fragment() {

    private var _binding: FragmentPakanBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPakanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        livestockViewModel.getLivestockById.observe(viewLifecycleOwner) {
            if (it?.feedingRecords != null) {
                listPakan(it.feedingRecords)
            }
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
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