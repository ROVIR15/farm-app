package com.vt.vt.ui.pemberian_ternak.tambahan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentTambahanBinding

class TambahanFragment : Fragment() {
    private var _binding: FragmentTambahanBinding? = null
    private val binding get() = _binding!!

    private var value: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTambahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarHijauan.topAppBar.apply {
                title = "Pakan Tambahan"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanTambahan.setOnClickListener {
                if (!editTextRekamPemberianTambahan.text.isNullOrEmpty()) {
                    view.findNavController()
                        .navigate(R.id.action_tambahanFragment_to_pemberianTernakFragment)
                } else {
                    Toast.makeText(requireContext(), "Isi Pemberian Tambahan", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}