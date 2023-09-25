package com.vt.vt.ui.file_provider.addlivestock

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddLivestockBinding
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.createCustomTempFile
import com.vt.vt.utils.getRotateImage
import com.vt.vt.utils.uriToFile
import java.io.File

class AddLivestockFragment : Fragment() {

    private var _binding: FragmentAddLivestockBinding? = null
    private val binding get() = _binding!!

    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Tambah Livestock"
                toolbar.setNavigationOnClickListener {
                    it?.findNavController()?.popBackStack()
                }
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvBirth)
            }
            ivPhotoDataArea.setOnClickListener {
                requestPermissionsIfNeeded()
            }
        }
        spinnerAdapter()
    }

    private fun spinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinnerGender.adapter = adapter
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
            startCamera()
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity()).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "com.vt.vt.ui.file_provider",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultCode = result.resultCode
        if (resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val filePhoto = getRotateImage(
                myFile.absolutePath,
                BitmapFactory.decodeFile(myFile.path),
            )
            binding.ivPhotoDataArea.setImageBitmap(filePhoto)
            binding.ivPhotoDataArea.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_image_24)
            binding.ivPhotoDataArea.clipToOutline = true
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                val selectedImg = it.data?.data as Uri
                val myFile = uriToFile(selectedImg, requireContext())
                with(binding) {
                    ivPhotoDataArea.apply {
                        setImageURI(selectedImg)
                        background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_outline_image_24
                        )
                        clipToOutline = true
                    }
                    iconFilePhoto.visibility = View.GONE
                    tvUploadFilePhoto.visibility = View.GONE
                }
            }
        }


}