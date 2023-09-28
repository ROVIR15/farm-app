package com.vt.vt.ui.pemberian_ternak.vitamin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.databinding.FragmentVitaminBinding

class VitaminFragment : Fragment() {
    private var _binding: FragmentVitaminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVitaminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("blockId")
        with(binding) {
            appBarVitamin.topAppBar.apply {
                title = "Vitamin"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanVitamin.setOnClickListener {
            }
            btnBatalVitamin.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}