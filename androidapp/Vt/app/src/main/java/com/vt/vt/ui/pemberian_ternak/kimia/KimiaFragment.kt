package com.vt.vt.ui.pemberian_ternak.kimia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentKimiaBinding

class KimiaFragment : Fragment() {
    private var _binding: FragmentKimiaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKimiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("blockId")
        with(binding) {
            appBarKimia.topAppBar.apply {
                title = "Kimia"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanKimia.setOnClickListener {
            }
            btnBatalKimia.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}