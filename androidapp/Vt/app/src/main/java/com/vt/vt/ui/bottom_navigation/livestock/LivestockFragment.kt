package com.vt.vt.ui.bottom_navigation.livestock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.local.livestock.Livestock
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
        livestockViewModel.livestockItems.observe(viewLifecycleOwner) { items ->
            setRecyclerView(items)
        }
    }

    private fun setRecyclerView(data: List<Livestock>) {
        val livestockAdapter = LivestockAdapter(data)
        with(binding) {
            recyclerView.adapter = livestockAdapter
            recyclerView.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}