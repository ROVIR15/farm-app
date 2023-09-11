package com.vt.vt.ui.anggaran_dan_realisasi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.databinding.FragmentAnggaranDanRealisasiBinding

class AnggaranDanRealisasiFragment : Fragment() {

    private var _binding: FragmentAnggaranDanRealisasiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnggaranDanRealisasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this.appBarAnggaranRealisasi.topAppBar.apply {
                title = "Anggaran vs Realisasi"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}