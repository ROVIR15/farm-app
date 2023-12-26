package com.vt.vt.ui.file_provider.datakandang

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.permission.PermissionAlertDialog.showPermissionDeniedDialog
import com.vt.vt.core.data.permission.PermissionManager
import com.vt.vt.databinding.FragmentAddCageBinding
import com.vt.vt.ui.common.SnapSheetFragment
import com.vt.vt.ui.common.SnapSheetListener
import com.vt.vt.ui.penyimpan_ternak.LivestockStorageViewModel
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCageFragment : Fragment(), View.OnClickListener, SnapSheetListener {

    private var _binding: FragmentAddCageBinding? = null
    private val binding get() = _binding!!

    private val cageDataViewModel by viewModels<DataKandangViewModel>()
    private val viewModel by viewModels<LivestockStorageViewModel>()

    private var blockAreaId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllBlockAndArea()

        with(binding) {
            this.appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Tambah Kandang"
                toolbar.setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            ivPhotoDataKandang.setOnClickListener(this@AddCageFragment)
            btnSave.setOnClickListener(this@AddCageFragment)
            btnCancel.setOnClickListener(this@AddCageFragment)
        }
        observerView()
    }

    private val requestResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            var permissionGranted = true
            permission.entries.forEach {
                if (it.key in PermissionManager.REQUIRED_PERMISSION && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                showPermissionDeniedDialog(requireActivity())
            }
        }

    private fun observerView() {
        viewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            allBlockAndAreas.observe(viewLifecycleOwner) { data ->
                val blocksArray = data.map {
                    it.name
                }.toTypedArray()
                val prompt = "Pilih Blok"
                val blocksArrayWithPrompt = arrayOf(prompt) + blocksArray
                val adapter =
                    ArrayAdapter(requireActivity(), R.layout.item_spinner, blocksArrayWithPrompt)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCageCategory.adapter = adapter
                binding.spinnerCageCategory.selected { position ->
                    if (position > 0) {
                        blockAreaId = data[position - 1].id
                    } else {
                        blockAreaId = -1
                    }
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        cageDataViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            createSled.observe(viewLifecycleOwner) {
                view?.findNavController()
                    ?.navigate(R.id.action_dataKandangFragment_to_navigation_home)
                Toast.makeText(
                    requireContext(), "${it.status} Menambahkan Kandang", Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSave.isEnabled = !state
            btnCancel.isEnabled = !state

            btnSave.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun bitmapPhotos(photo: Bitmap?) {
        Log.d(TAG, "BITMAP PHOTO : $photo")
        if (photo != null) {
            with(binding) {
                ivPhotoDataKandang.setImageBitmap(photo)
                ivPhotoDataKandang.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_image_24)
                ivPhotoDataKandang.clipToOutline = true
                iconFilePhoto.visibility = View.GONE
                tvUploadFilePhoto.visibility = View.GONE
            }
        }
    }

    override fun uriFile(photo: Uri?) {
        Log.d(TAG, "URI PHOTO : $photo")
        with(binding) {
            ivPhotoDataKandang.apply {
                setImageURI(photo)
                background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_outline_image_24
                )
                clipToOutline = true
            }
            iconFilePhoto.visibility = View.GONE
            tvUploadFilePhoto.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_photo_data_kandang -> {
                if (PermissionManager(requireContext()).hasPermission()) {
                    val snapShotDialog = SnapSheetFragment()
                    snapShotDialog.show(
                        childFragmentManager, snapShotDialog::class.java.simpleName
                    )
                } else {
                    requestResultLauncher.launch(PermissionManager.REQUIRED_PERMISSION)
                }
            }

            R.id.btn_save -> {
                val name = binding.edtNamaKandang.text.toString().trim()
                val description = binding.edtDescription.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty() && blockAreaId != null) {
                    cageDataViewModel.createSled(blockAreaId, name, description)
                } else {
                    Toast.makeText(requireContext(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            R.id.btn_cancel -> {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        private val TAG = AddCageFragment::class.java.simpleName
    }
}
