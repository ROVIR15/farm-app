package com.vt.vt.ui.file_provider.dataarea

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.permission.PermissionAlertDialog.showPermissionDeniedDialog
import com.vt.vt.core.data.permission.PermissionManager
import com.vt.vt.databinding.FragmentDataAreaBinding
import com.vt.vt.ui.common.SnapSheetFragment
import com.vt.vt.ui.common.SnapSheetListener
import com.vt.vt.ui.penyimpan_ternak.adapter.LivestockStorageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataAreaFragment : Fragment(), View.OnClickListener, SnapSheetListener {

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
        if (LivestockStorageAdapter.isUpdate) {
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
            this.ivPhotoDataArea.setOnClickListener(this@DataAreaFragment)
            this.btnSimpan.setOnClickListener {
                val name = binding.edtAreaName.text.toString().trim()
                val description = binding.edtDescription.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty()) {
                    if (LivestockStorageAdapter.isUpdate) {
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
        dataAreaViewModel.getBlockArea.observe(viewLifecycleOwner) { data ->
            binding.appBarLayout.topAppBar.title = "Edit ${data?.name}"
            binding.edtAreaName.setText(data?.name)
            binding.edtDescription.setText(data?.description)
        }
        dataAreaViewModel.isUpdatedBlockAndArea.observe(viewLifecycleOwner) {
            view?.findNavController()?.popBackStack()
            Toast.makeText(requireContext(), it?.message.toString(), Toast.LENGTH_SHORT).show()
        }
        dataAreaViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
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

    override fun bitmapPhotos(photo: Bitmap?) {
        Log.d(TAG, "BITMAP PHOTO : $photo")
        if (photo != null) {
            with(binding) {
                ivPhotoDataArea.setImageBitmap(photo)
                ivPhotoDataArea.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_image_24)
                ivPhotoDataArea.clipToOutline = true
                iconFilePhoto.visibility = View.GONE
                tvUploadFilePhoto.visibility = View.GONE
            }
        }
    }

    override fun uriFile(photo: Uri?) {
        Log.d(TAG, "URI PHOTO : $photo")
        with(binding) {
            ivPhotoDataArea.apply {
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_photo_data_area -> {
                if (PermissionManager(requireContext()).hasPermission()) {
                    val snapShotDialog = SnapSheetFragment()
                    snapShotDialog.show(
                        childFragmentManager, snapShotDialog::class.java.simpleName
                    )
                } else {
                    requestResultLauncher.launch(PermissionManager.REQUIRED_PERMISSION)
                }
            }
        }
    }

    companion object {
        private val TAG = DataAreaFragment::class.java.simpleName
    }

}