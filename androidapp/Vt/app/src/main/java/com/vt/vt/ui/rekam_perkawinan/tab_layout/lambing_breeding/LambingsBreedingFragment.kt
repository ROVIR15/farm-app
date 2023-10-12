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
import com.vt.vt.core.data.source.remote.breeding.LambingItem
import com.vt.vt.databinding.FragmentLambingsBreedingBinding
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LambingsBreedingFragment : Fragment() {
    private var _binding: FragmentLambingsBreedingBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLambingsBreedingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("breedingId")
        recordBreedingViewModel.getBreedingById(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.progressBar.isVisible = isLoading
        }
        recordBreedingViewModel.breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
            binding.dataEmpty.isEmpty.isVisible = breeding.lambing.isNullOrEmpty()
            if (!breeding.lambing.isNullOrEmpty()) {
                setupRecyclerView(breeding.lambing)
            }
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(data: List<LambingItem>?) {
        val adapter = LambingBreedingAdapter()
        adapter.submitList(data)
        with(binding) {
            rvLembing.adapter = adapter
            rvLembing.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}