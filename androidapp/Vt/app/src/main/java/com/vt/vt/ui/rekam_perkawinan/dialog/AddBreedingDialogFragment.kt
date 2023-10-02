package com.vt.vt.ui.rekam_perkawinan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.databinding.FragmentAddBreedingDialogBinding

class AddBreedingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddBreedingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBreedingDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnSaveAnimalChild.setOnClickListener { dismiss() }
            btnCancelAnimalChild.setOnClickListener { dismiss() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}