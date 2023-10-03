package com.vt.vt.ui.bottom_navigation.livestock

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
import com.vt.vt.databinding.FragmentLivestockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LivestockFragment : Fragment() {

    private var _binding: FragmentLivestockBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel: LivestockViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        livestockViewModel.getLivestocks()
        observerView()
    }

    private fun observerView() {
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            livestockItems.observe(viewLifecycleOwner) { livestocks ->
                binding.dataEmpty.isEmpty.isVisible = livestocks.isEmpty()
                setRecyclerView(livestocks)
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
                getLivestocks()
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setRecyclerView(data: List<LivestockResponseItem>) {
        val livestockAdapter = LivestockAdapter(requireActivity(), livestockViewModel)
        livestockAdapter.submitList(data)
        with(binding) {
            recyclerView.adapter = livestockAdapter
            recyclerView.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
    }

    private fun showLoading(state: Boolean) {
        //  binding.refresh.isRefreshing = state
        with(binding) {
            if (state) {
                loading.progressBar.visibility = View.VISIBLE
            } else {
                loading.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}