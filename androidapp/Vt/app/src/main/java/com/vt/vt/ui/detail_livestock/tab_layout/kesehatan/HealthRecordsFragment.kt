package com.vt.vt.ui.detail_livestock.tab_layout.kesehatan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.health_record.model.HealthRecordResponseItem
import com.vt.vt.databinding.FragmentHealthRecordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthRecordsFragment : Fragment() {

    private var _binding: FragmentHealthRecordsBinding? = null
    private val binding get() = _binding!!

    private val healthRecordViewModel by viewModels<HealthRecordViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthRecordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerView()
    }

    private fun observerView() {
        healthRecordViewModel.apply {
            getListHealthRecords()
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            healthRecordsEmitter.observe(viewLifecycleOwner) { data ->
                binding.dataEmpty.isEmpty.isVisible = data.isEmpty()
                listHealthRecords(data)
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listHealthRecords(data: List<HealthRecordResponseItem>) {
        val adapter = ListHealthRecordAdapter()
        adapter.submitList(data)
        with(binding) {
            rvHealthRecord.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            rvHealthRecord.adapter = adapter
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