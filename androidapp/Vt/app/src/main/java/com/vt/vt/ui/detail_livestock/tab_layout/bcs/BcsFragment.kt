package com.vt.vt.ui.detail_livestock.tab_layout.bcs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.livestock.dto.BcsRecordsItem
import com.vt.vt.databinding.FragmentBcsBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BcsFragment : Fragment() {
    private var _binding: FragmentBcsBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBcsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                livestock?.let { data ->
                    binding.dataEmpty.isEmpty.isVisible = data.bcsRecords.isEmpty()
                    listBcsRecord(data.bcsRecords)
                }
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun listBcsRecord(data: List<BcsRecordsItem>) {
        val adapter = BcsRecordAdapter()
        adapter.submitList(data)
        with(binding) {
            recyclerViewBcs.adapter = adapter
            recyclerViewBcs.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

}