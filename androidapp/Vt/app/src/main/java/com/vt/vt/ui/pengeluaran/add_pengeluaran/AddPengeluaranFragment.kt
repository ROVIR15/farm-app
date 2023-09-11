package com.vt.vt.ui.pengeluaran.add_pengeluaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vt.vt.databinding.FragmentAddPengeluaranBinding

class AddPengeluaranFragment : Fragment() {

    private var _binding: FragmentAddPengeluaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPengeluaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarAddAnggaran.topAppBar.apply {
                title = "Tambah Pengeluaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}