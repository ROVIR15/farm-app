package com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.base.bottomdialog.listener.OnBottomSheetListener
import com.vt.vt.core.data.source.remote.breeding.BreedingHistoryItem
import com.vt.vt.databinding.FragmentAnimalMatingHistoryBinding
import com.vt.vt.ui.rekam_perkawinan.RecordBreedingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryAnimalMatingFragment : Fragment() {
    private var _binding: FragmentAnimalMatingHistoryBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()

    private var receiveId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalMatingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveId = arguments?.getInt("breedingId")
        recordBreedingViewModel.getBreedingById(receiveId.toString())
        observerView()
    }

    private fun observerView() {
        recordBreedingViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.progressBar.isVisible = isLoading
        }
        recordBreedingViewModel.breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
            binding.dataEmpty.isEmpty.isVisible = breeding.breedingHistory.isNullOrEmpty()
            if (!breeding.breedingHistory.isNullOrEmpty()) {
                setupRecyclerView(breeding.breedingHistory)
            }
        }
        recordBreedingViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView(data: List<BreedingHistoryItem>?) {
        val adapter = HistoryAnimalMatingAdapter()
        adapter.submitList(data)
        with(binding) {
            rvAnimalMatingHistory.adapter = adapter
            rvAnimalMatingHistory.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}