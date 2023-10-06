package com.vt.vt.ui.detail_area_block.tab_layout.area_list_livestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.livestock.model.LivestockResponseItem
import com.vt.vt.databinding.FragmentAreaListLivestockBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AreaListLivestockFragment : Fragment() {

    private var _binding: FragmentAreaListLivestockBinding? = null
    private val binding get() = _binding!!
    private val dataAreaViewModel by viewModels<DataAreaViewModel>()
    private val livestockViewModel by viewModels<LivestockViewModel>()

    private var receiveId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaListLivestockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveId = arguments?.getInt("areaBlockId")
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
                listAreaLivestock(data.livestocks)
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
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                binding.loading.progressBar.isVisible = it
            }
            isDeleted.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let { eventMessage ->
                    Toast.makeText(
                        requireActivity(),
                        eventMessage,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            deleteLivestock.observe(viewLifecycleOwner) {
                dataAreaViewModel.getBlockArea(receiveId.toString())
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun listAreaLivestock(data: List<LivestockResponseItem>?) {
        val adapter = AreaListLivestockAdapter(livestockViewModel)
        adapter.submitList(data)
        binding.listBlockLivestock.adapter = adapter
        binding.listBlockLivestock.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}