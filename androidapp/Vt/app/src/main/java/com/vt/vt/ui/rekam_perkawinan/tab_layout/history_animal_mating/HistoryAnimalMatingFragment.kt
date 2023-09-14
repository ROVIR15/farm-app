package com.vt.vt.ui.rekam_perkawinan.tab_layout.history_animal_mating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.dummy.tablayout.historyperanakan.HistoryPeranakan
import com.vt.vt.databinding.FragmentAnimalMatingHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryAnimalMatingFragment : Fragment() {
    private var _binding: FragmentAnimalMatingHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyAnimalMatingViewModel: HistoryAnimalMatingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimalMatingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyAnimalMatingViewModel.historyPeranakan.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }
    }

    private fun setupRecyclerView(data: List<HistoryPeranakan>) {
        val adapter = HistoryAnimalMatingAdapter(data)
        with(binding) {
            this.rvAnimalMatingHistory.apply {
                this.adapter = adapter
                this.layoutManager =
                    LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}