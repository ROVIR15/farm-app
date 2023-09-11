package com.vt.vt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vt.vt.databinding.FragmentToDetailAfterScanBinding

class ToDetailAfterScanFragment : Fragment() {

    private var _binding: FragmentToDetailAfterScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDetailAfterScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cekMasuk = arguments?.getString(EXTRA_SCAN_ID)
        Toast.makeText(requireContext(), "$cekMasuk", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var EXTRA_SCAN_ID = "extra_scan_id"
    }
}