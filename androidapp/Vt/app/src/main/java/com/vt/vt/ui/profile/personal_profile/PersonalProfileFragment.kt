package com.vt.vt.ui.profile.personal_profile

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
import com.vt.vt.core.data.permission.PermissionAlertDialog
import com.vt.vt.core.data.permission.PermissionManager
import com.vt.vt.databinding.FragmentPersonalProfileBinding
import com.vt.vt.ui.snapsheet.SnapSheetFragment
import com.vt.vt.ui.snapsheet.SnapSheetListener
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.fileToMultipart
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PersonalProfileFragment : Fragment(), View.OnClickListener, SnapSheetListener {

    private var _binding: FragmentPersonalProfileBinding? = null
    private val binding get() = _binding!!

    private val personalProfileViewModel by viewModels<PersonalProfileViewModel>()
    private var getFile: File? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarPersonalProfile.topAppBar.apply {
                title = "Profil Pribadi"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivDatePicker.setOnClickListener(this@PersonalProfileFragment)
            btnEditProfilePicture.setOnClickListener(this@PersonalProfileFragment)
            btnSavePersonalProfile.setOnClickListener(this@PersonalProfileFragment)
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
                PermissionAlertDialog.showPermissionDeniedDialog(requireActivity())
            }
        }

    private fun observerView() {
        personalProfileViewModel.apply {
            getProfile()
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getProfileEmitter.observe(viewLifecycleOwner) { profile ->
                with(binding) {
                    edtEditNamePersonalProfile.setText(profile.message?.name)
                    edtEditEmailPersonalProfile.setText(profile.message?.email)
                    tvDatePersonalProfile.text = profile.message?.date
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bitmapPhotos(photo: Bitmap?) {
        Log.d(TAG, "bitmapPhotos: $photo")
        binding.circleImageView.setImageBitmap(photo)
    }

    override fun uriFile(photo: Uri?) {
        Log.d(TAG, "uriPhotos: $photo")
        binding.circleImageView.setImageURI(photo)
    }

    override fun getFile(file: File?) {
        Log.d(TAG, "get file : $file")
        if (file != null) {
            val myFile = fileToMultipart(TAG, file)
            Log.d(TAG, "getFileMultipart: $myFile")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_profile_picture -> {
                if (PermissionManager(requireContext()).hasPermission()) {
                    val snapShotDialog = SnapSheetFragment()
                    snapShotDialog.show(
                        childFragmentManager, snapShotDialog::class.java.simpleName
                    )
                } else {
                    requestResultLauncher.launch(PermissionManager.REQUIRED_PERMISSION)
                }
            }

            R.id.iv_date_picker -> {
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvDatePersonalProfile)
            }

            R.id.btn_save_personal_profile -> {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSavePersonalProfile.isEnabled = !state

            btnSavePersonalProfile.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private val TAG = PersonalProfileFragment::class.java.simpleName
    }
}