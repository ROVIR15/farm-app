package com.vt.vt.ui.edit_area_block

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditAreaBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAreaBlockFragment : Fragment() {

    private var _binding: FragmentEditAreaBlockBinding? = null
    private val binding get() = _binding!!

    private val areaBlockViewModel by viewModels<AreaBlockViewModel>()
    private var receiveId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAreaBlockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Edit Area/Block"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivEditImageAreaBlock.setOnClickListener {
                showBottomSheetDialog()
            }
            btnSaveEditAreaBlock.setOnClickListener {
                val name = edtNamaAreaBlock.text.toString().trim()
                val description = edtDescAreaBlock.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    areaBlockViewModel.updateSledById(receiveId, name, description)
                } else {
                    Toast.makeText(requireContext(), "Lengkapi Kolom ", Toast.LENGTH_SHORT).show()
                }
            }
            btnCancelEditAreaBlock.setOnClickListener {

            }
        }
        observerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observerView() {
        receiveId = arguments?.getInt("id").toString()
        areaBlockViewModel.getSledById(receiveId)

        areaBlockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getSledById.observe(viewLifecycleOwner) { item ->
                binding.appBarLayout.topAppBar.title = "Edit ${item.name}"
                binding.edtNamaAreaBlock.setText(item.name)
                binding.edtDescAreaBlock.setText(item.description)
            }
            updateSledById.observe(viewLifecycleOwner) { update ->
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireContext(), update?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveEditAreaBlock.isEnabled = !state
            btnCancelEditAreaBlock.isEnabled = !state

            btnSaveEditAreaBlock.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
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