package com.vt.vt.ui.pengeluaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPengeluaranBinding

class PengeluaranFragment : Fragment() {

    private var _binding: FragmentPengeluaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPengeluaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            pengeluaranAppBar.topAppBar.apply {
                title = "Pengeluaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            footerPengeluaranForm.btnTambahPengeluaran.setOnClickListener {
                findNavController().navigate(R.id.action_pengeluaranFragment_to_addPengeluaranFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}