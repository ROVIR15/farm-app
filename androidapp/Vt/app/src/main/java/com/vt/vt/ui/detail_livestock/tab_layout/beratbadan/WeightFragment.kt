package com.vt.vt.ui.detail_livestock.tab_layout.beratbadan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.livestock.dto.WeightRecordsItem
import com.vt.vt.databinding.FragmentWeightBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeightFragment : Fragment() {
    private var _binding: FragmentWeightBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<EditLivestockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
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
                    binding.dataEmpty.isEmpty.isVisible = data.weightRecords.isEmpty()
                    listWeigth(data.weightRecords)
                }
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listWeigth(data: List<WeightRecordsItem>) {
        val adapter = ListWeightRecordAdapter()
        adapter.submitList(data)
        with(binding) {
            recyclerViewWeight.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerViewWeight.adapter = adapter
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