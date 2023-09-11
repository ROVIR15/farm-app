package com.vt.vt.ui.rekam_kesehatan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentRekamKesehatanBinding

class RekamKesehatanFragment : Fragment() {

    private var _binding: FragmentRekamKesehatanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamKesehatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this.appBarLayout.topAppBar.apply {
                title = "Rekam Kesehatan"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}