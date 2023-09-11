package com.vt.vt.ui.pemberian_ternak.vitamin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentKimiaBinding
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
        with(binding) {
            appBarVitamin.topAppBar.apply {
                title = "Vitamin"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanVitamin.setOnClickListener {
                if (editTextRekamPemberianVitamin.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Isi Pemberian Vitamin", Toast.LENGTH_SHORT).show()
                } else {
                    view.findNavController()
                        .navigate(R.id.action_vitaminFragment_to_tambahanFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}