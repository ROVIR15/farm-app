package com.vt.vt.ui.detail_area_block.tab_layout.area_list_pakan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vt.vt.databinding.FragmentAreaListPakanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AreaListPakanFragment : Fragment() {

    private var _binding: FragmentAreaListPakanBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaListPakanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}