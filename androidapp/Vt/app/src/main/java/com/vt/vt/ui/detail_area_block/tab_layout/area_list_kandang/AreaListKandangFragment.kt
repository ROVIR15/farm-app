package com.vt.vt.ui.detail_area_block.tab_layout.area_list_kandang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.block_areas.model.Sleds
import com.vt.vt.databinding.FragmentAreaListKandangBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import com.vt.vt.ui.file_provider.datakandang.DataKandangViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AreaListKandangFragment : Fragment() {

    private var _binding: FragmentAreaListKandangBinding? = null
    private val binding get() = _binding!!

    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()
    private val dataAreaViewModel by viewModels<DataAreaViewModel>()
    private val sledsViewModel by viewModels<DataKandangViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaListKandangBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("areaBlockId")
        dataAreaViewModel.getBlockArea(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        dataAreaViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                binding.loading.progressBar.isVisible = it
            }
            getBlockArea.observe(viewLifecycleOwner) { data ->
                binding.dataEmpty.isEmpty.isVisible = data.sleds.isNullOrEmpty()
                setupRecyclerView(data.sleds)
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireActivity(),
                    it.toString(),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        sledsViewModel.deleteSledById.observe(viewLifecycleOwner) {
            detailAreaBlockViewModel.getSleds()
        }
        sledsViewModel.isDeleted.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventMessage ->
                Toast.makeText(
                    requireActivity(),
                    eventMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun setupRecyclerView(data: List<Sleds>?) {
        val adapter = AreaListKandangAdapter(requireContext(), sledsViewModel)
        adapter.submitList(data)
        with(binding) {
            recyclerViewListDetailArea.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerViewListDetailArea.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}