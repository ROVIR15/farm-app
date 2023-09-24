package com.vt.vt.ui.file_provider.dataarea

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentDataAreaBinding
import com.vt.vt.ui.penyimpan_ternak.adapter.PenyimpananTernakAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataAreaFragment : Fragment() {

    private var _binding: FragmentDataAreaBinding? = null
    private val binding get() = _binding!!

    private val dataAreaViewModel by viewModels<DataAreaViewModel>()
    private var id: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PenyimpananTernakAdapter.isUpdate) {
            binding.btnSimpan.text = "Update"
            id = arguments?.getInt("id").toString()
            dataAreaViewModel.getBlockArea(id)
        }

        with(binding) {
            this.appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Tambah Area"
                toolbar.setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            this.ivPhotoDataArea.setOnClickListener {
                requestPermissionsIfNeeded()
            }
            this.btnSimpan.setOnClickListener {
                val name = binding.edtAreaName.text.toString().trim()
                val description = binding.edtDescription.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    if (PenyimpananTernakAdapter.isUpdate) {
                        dataAreaViewModel.updateBlockAndArea(id, name, description)
                    } else {
                        dataAreaViewModel.createBlockAndArea(name, description)
                    }
                } else {
                    Toast.makeText(requireContext(), "Lengkapi Kolom ", Toast.LENGTH_SHORT).show()
                }
            }
        }
        observeView()
    }

    private fun observeView() {
        dataAreaViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        dataAreaViewModel.isCreatedBlockAndArea.observe(viewLifecycleOwner) {
            view?.findNavController()?.popBackStack()
        }
        dataAreaViewModel.getBlockArea.observe(viewLifecycleOwner) {
            binding.edtAreaName.setText(it?.name)
            binding.edtDescription.setText(it?.description)
        }
        dataAreaViewModel.isUpdatedBlockAndArea.observe(viewLifecycleOwner) {
            view?.findNavController()?.popBackStack()
            Toast.makeText(requireContext(), it?.message.toString(), Toast.LENGTH_SHORT).show()
        }
        dataAreaViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            if (state) {
                loading.progressBar.visibility = View.VISIBLE
                binding.apply {
                    btnSimpan.apply {
                        isEnabled = false
                        setBackgroundColor(Color.GRAY)
                    }
                    btnBatal.isEnabled = false
                }
            } else {
                binding.apply {
                    btnSimpan.apply {
                        isEnabled = true
                        setBackgroundColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.btn_blue_icon
                            )
                        )
                    }
                    btnBatal.isEnabled = true
                }
                loading.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                requireContext(), Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsIfNeeded() {
        val readStoragePermissionGranted = hasReadStoragePermission()
        val cameraPermissionGranted = hasCameraPermission()

        if (!readStoragePermissionGranted || !cameraPermissionGranted) {
            val permissions = mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
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

}