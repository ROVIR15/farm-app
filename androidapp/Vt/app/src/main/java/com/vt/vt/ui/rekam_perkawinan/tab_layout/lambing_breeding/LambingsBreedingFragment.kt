package com.vt.vt.ui.rekam_perkawinan.tab_layout.lambing_breeding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.base.refresh.SwipeRefreshListener
import com.vt.vt.core.data.source.remote.breeding.dto.LambingItem
import com.vt.vt.databinding.FragmentLambingsBreedingBinding
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LambingsBreedingFragment : Fragment(), SwipeRefreshListener {
    private var _binding: FragmentLambingsBreedingBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    private val listLambingBreeding by lazy { LambingBreedingAdapter(recordBreedingViewModel) }
    private var receiveId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLambingsBreedingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveId = arguments?.getInt("breedingId")
        recordBreedingViewModel.getBreedingById(receiveId.toString())
        setupSwipeRefresh(binding.refreshPage)
        observerView()
    }

    override fun onRefreshRequested() {
        recordBreedingViewModel.getBreedingById(receiveId.toString())
    }

    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.progressBar.isVisible = isLoading
        }
        recordBreedingViewModel.breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
            binding.dataEmpty.isEmpty.isVisible = breeding.lambing.isNullOrEmpty()
            if (!breeding.lambing.isNullOrEmpty()) {
                binding.rvLembing.visibility = View.VISIBLE
                setupRecyclerView(breeding.lambing)
            } else {
                binding.rvLembing.visibility = View.GONE
            }
        }
        recordBreedingViewModel.deleteBreedingEmitter.observe(viewLifecycleOwner) {
            recordBreedingViewModel.getBreedingById(receiveId.toString())
            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        with(binding) {
            rvLembing.adapter = listLambingBreeding
            rvLembing.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupRecyclerView(data: List<LambingItem>?) {
        if (data != null) {
            if (data.isEmpty()) return
            listLambingBreeding.submitList(data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}