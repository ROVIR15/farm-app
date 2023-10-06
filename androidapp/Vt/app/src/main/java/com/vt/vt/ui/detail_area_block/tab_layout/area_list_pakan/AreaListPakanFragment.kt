package com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.core.data.source.remote.dummy.livestock.Pakan
import com.vt.vt.databinding.FragmentAreaListPakanBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AreaListPakanFragment : Fragment() {

    private var _binding: FragmentAreaListPakanBinding? = null
    private val binding get() = _binding!!

    private val detailAreaBlockViewModel: DetailAreaBlockViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaListPakanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailAreaBlockViewModel.pakanEmitter.observe(viewLifecycleOwner) {
            listPakan(it)
        }
    }

    private fun listPakan(data: List<Pakan>) {
        val adapter = AreaListPakanAdapter(data)
        with(binding) {
            listPakan.adapter = adapter
            listPakan.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}