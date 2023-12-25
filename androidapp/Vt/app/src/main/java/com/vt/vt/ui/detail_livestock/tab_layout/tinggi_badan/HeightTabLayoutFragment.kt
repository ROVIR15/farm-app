package com.vt.vt.ui.detail_livestock.tab_layout.tinggi_badan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.livestock.dto.HeightRecordsItem
import com.vt.vt.databinding.FragmentHeightTabLayoutBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeightTabLayoutFragment : Fragment() {

    private var _binding: FragmentHeightTabLayoutBinding? = null
    private val binding get() = _binding!!

    private val listHeightTabLayoutAdapter by lazy { ListHeightTabLayoutAdapter() }
    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeightTabLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())
        with(binding) {
            listHeight.adapter = listHeightTabLayoutAdapter
            listHeight.layoutManager = LinearLayoutManager(
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
            result?.let { data ->
                binding.dataEmpty.isEmpty.isVisible = data.weightRecords.isEmpty()
                listHeight(result.heightRecords)
            }
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listHeight(data: List<HeightRecordsItem>) {
        listHeightTabLayoutAdapter.submitList(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}