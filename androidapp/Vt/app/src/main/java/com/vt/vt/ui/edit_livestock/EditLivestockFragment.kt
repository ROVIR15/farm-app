package com.vt.vt.ui.edit_livestock

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditLivestockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLivestockFragment : Fragment() {

    private var _binding: FragmentEditLivestockBinding? = null
    private val binding get() = _binding!!
    private val editLivestockViewModel by viewModels<EditLivestockViewModel>()

    var receiveId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveId = arguments?.getInt("id").toString()
        editLivestockViewModel.getLivestockById(receiveId)

        with(binding) {
            appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Edit Profile Livestock"
                toolbar.setNavigationOnClickListener {
                    it?.findNavController()?.popBackStack()
                }
            }
            ivProfileLivestock.setOnClickListener {
                showBottomSheetDialog()
            }
            btnSaveEditLivestock.setOnClickListener {
                val name = edtNamaAddLivestock.text.toString().trim()
                val nation = edtBangsa.text.toString().trim()
                val gender = 1
                val description = edtDescription.text.toString().trim()
                editLivestockViewModel.updateLivestockById(
                    receiveId,
                    name,
                    gender,
                    nation,
                    description
                )
            }
            /* Spinner Adapter */
            adapterSpinner(binding.spinnerGenderUmum)
            adapterSpinner(binding.spinnerKandangUmum)
            adapterSpinner(binding.spinnerPilihLivestockJantan)
            adapterSpinner(binding.spinnerPilihLivestockBetina)
        }
        observerView()
    }

    private fun observerView() {
        editLivestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                with(binding) {
                    appBarLayout.topAppBar.title = "Edit ${livestock?.name}"
                    edtNamaAddLivestock.setText(livestock?.name.toString())
                    edtDescription.setText(livestock?.description.toString())
                    edtBangsa.setText(livestock?.bangsa)
                }
            }
            isUpdateLivestock.observe(viewLifecycleOwner) { livestock ->
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireContext(), livestock?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun adapterSpinner(binding: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(R.layout.bottom_sheet_open_camera_gallery)
        val btnCamera = dialog.findViewById<RelativeLayout>(R.id.rl_camera)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_gallery)
        dialog.show()
        btnCamera?.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
    }

    fun startCamera() {
        Toast.makeText(requireContext(), "belum ada action ke camera", Toast.LENGTH_SHORT).show()
    }

    fun startGallery() {
        Toast.makeText(requireContext(), "belum ada action ambil gambar", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveEditLivestock.isEnabled = !state
            btnCancelEditLivestock.isEnabled = !state

            btnSaveEditLivestock.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }
}