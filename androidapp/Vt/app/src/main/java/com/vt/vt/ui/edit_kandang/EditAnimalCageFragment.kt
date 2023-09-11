package com.vt.vt.ui.edit_kandang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditAnimalCageBinding

class EditAnimalCageFragment : Fragment() {

    private var _binding: FragmentEditAnimalCageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAnimalCageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Edit Kandang"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivEditImageAreaBlock.setOnClickListener {
                showBottomSheetDialog()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_open_camera_gallery)
        val btnCamera = dialog.findViewById<RelativeLayout>(R.id.rl_camera)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_gallery)
        dialog.show()
        btnCamera?.setOnClickListener {
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            dialog.dismiss()
        }
    }
}