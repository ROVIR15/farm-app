package com.vt.vt.ui.pemberian_ternak.hijauan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentHijauanBinding
import com.vt.vt.ui.pemberian_ternak.PemberianTernakFragment

class HijauanFragment : Fragment() {

    private var _binding: FragmentHijauanBinding? = null
    private val binding get() = _binding!!

    private var value: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHijauanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("blockId")

        with(binding) {
            appBarHijauan.topAppBar.apply {
                title = "Hijauan"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanHijauan.setOnClickListener {

            }
            btnBatalHijauan.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}