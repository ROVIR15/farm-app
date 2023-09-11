package com.vt.vt.ui.rekam_bcs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vt.vt.databinding.FragmentRekamBCSBinding

class RekamBCSFragment : Fragment() {

    private var _binding: FragmentRekamBCSBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamBCSBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Rekam BCS"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}