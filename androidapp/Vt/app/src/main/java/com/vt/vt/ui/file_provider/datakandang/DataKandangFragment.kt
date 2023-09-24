package com.vt.vt.ui.file_provider.datakandang

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDataKandangBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataKandangFragment : Fragment() {

    private var _binding: FragmentDataKandangBinding? = null
    private val binding get() = _binding!!

    private val cageDataViewModel by viewModels<DataKandangViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataKandangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            this.appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Tambah Kandang"
                toolbar.setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            this.ivPhotoDataKandang.setOnClickListener { requestPermissionsIfNeeded() }
            btnSimpan.setOnClickListener {
                val name = edtNamaKandang.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                cageDataViewModel.createSled(name, description)
            }
        }
        spinnerAdapter()
        observerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerView() {
        cageDataViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            createSled.observe(viewLifecycleOwner) {
                view?.findNavController()
                    ?.navigate(R.id.action_dataKandangFragment_to_navigation_home)
                Toast.makeText(
                    requireContext(),
                    "${it.status} Menambahkan Kandang",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun spinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(
                R.layout.item_spinner
            )
            binding.spinnerCageCategory.adapter = adapter
        }
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }
        }

    private fun hasReadStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsIfNeeded() {
        val readStoragePermissionGranted = hasReadStoragePermission()
        val cameraPermissionGranted = hasCameraPermission()

        if (!readStoragePermissionGranted || !cameraPermissionGranted) {
            val permissions = mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

            requestPermissions.launch(permissions.toTypedArray())
        } else {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(R.layout.bottom_sheet_open_camera_gallery)
        val btnCamera = dialog.findViewById<RelativeLayout>(R.id.rl_camera)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_gallery)
        dialog.show()
        btnCamera?.setOnClickListener {
            //startCamera()
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            //startGallery()
            dialog.dismiss()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpan.isEnabled = !state
            btnBatal.isEnabled = !state

            btnSimpan.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

}