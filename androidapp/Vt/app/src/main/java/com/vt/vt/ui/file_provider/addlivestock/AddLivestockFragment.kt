package com.vt.vt.ui.file_provider.addlivestock

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddLivestockBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.createCustomTempFile
import com.vt.vt.utils.getRotateImage
import com.vt.vt.utils.selected
import com.vt.vt.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddLivestockFragment : Fragment() {

    private var _binding: FragmentAddLivestockBinding? = null
    private val binding get() = _binding!!

    private val addLivestockViewModel by viewModels<AddLivestockViewModel>()
    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()

    private var id = 0
    private var blockId = 0
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                PickDatesUtils.datePickToEdittext(requireActivity(), tvBirth as TextInputEditText)
            }
            ivPhotoDataArea.setOnClickListener {
                requestPermissionsIfNeeded()
            }
            var genderId: Int = 0
            spinnerGender.selected {
                genderId = it
            }
            btnSave.setOnClickListener {
                val name = edtNamaAddLivestock.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                val bangsa = edtCountry.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty() && bangsa.isNotEmpty()) {
                    if (genderId != 0) {
                        addLivestockViewModel.createLivestock(
                            name, description,
                            genderId, bangsa
                        )
                    } else {
                        Toast.makeText(requireActivity(), "Pilih Jenis Hewan", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnBatal.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        spinnerGenderAdapter()
        observerView()
    }

    private fun observerView() {
        addLivestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            createLivestock.observe(viewLifecycleOwner) { data ->
                showBottomSheetAddLivestock(data.livestockId)
                Toast.makeText(requireActivity(), data.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            storeLivestock.observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    "Menambahkan Livestock Berhasil",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerViewBottomSheet(
        spinner: Spinner,
        textInputEditText: TextInputEditText,
        progressBar: ProgressBar
    ) {
        detailAreaBlockViewModel.apply {
            getSleds()
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            sledItems.observe(viewLifecycleOwner) { sleds ->
                val namesArray = sleds.map { data ->
                    data.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, namesArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.selected { position ->
                    textInputEditText.setText(sleds[position].blockAreaName)
                    id = sleds[position].id
                    blockId = sleds[position].blockAreaId
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(
                    requireContext(),
                    it ?: "Unkown Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showBottomSheetAddLivestock(livestockId: Int) {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_livestock)
        val progressBar = dialog.findViewById<ProgressBar>(R.id.progressBarLivestock)
        val edtBlockArea = dialog.findViewById<TextInputEditText>(R.id.et_block_area)
        val spinnerAddCage = dialog.findViewById<Spinner>(R.id.sp_select_categories)
        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel)

        if (progressBar != null && edtBlockArea != null && spinnerAddCage != null) {
            observerViewBottomSheet(spinnerAddCage, edtBlockArea, progressBar)
        }

        dialog.show()
        btnSave?.setOnClickListener {
            addLivestockViewModel.storeLivestock(livestockId, id, blockId)
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
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

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireActivity()).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                requireActivity(), "com.vt.vt.ui.file_provider", it
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
                            requireContext(), R.drawable.ic_outline_image_24
                        )
                        clipToOutline = true
                    }
                    iconFilePhoto.visibility = View.GONE
                    tvUploadFilePhoto.visibility = View.GONE
                }
            }
        }

    private fun spinnerGenderAdapter() {
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.gender_animal, R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.spinnerGender.adapter = adapter
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSave.isEnabled = !state
            btnBatal.isEnabled = !state

            btnSave.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

}