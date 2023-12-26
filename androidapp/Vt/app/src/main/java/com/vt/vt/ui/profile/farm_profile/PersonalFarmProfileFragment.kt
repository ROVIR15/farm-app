package com.vt.vt.ui.profile.farm_profile

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
import com.vt.vt.databinding.FragmentPersonalFarmProfileBinding
import com.vt.vt.ui.common.SnapSheetFragment
import com.vt.vt.ui.common.SnapSheetListener
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalFarmProfileFragment : Fragment(), View.OnClickListener, SnapSheetListener {

    private var _binding: FragmentPersonalFarmProfileBinding? = null
    private val binding get() = _binding!!

    private val farmProfileViewModel by viewModels<FarmProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalFarmProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarFarmProfile.topAppBar.apply {
                title = "Profil Perternakan"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivDatePicker.setOnClickListener(this@PersonalFarmProfileFragment)
            btnEditProfilePicture.setOnClickListener(this@PersonalFarmProfileFragment)
            btnSaveFarmProfile.setOnClickListener(this@PersonalFarmProfileFragment)
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
        farmProfileViewModel.apply {
            getFarmProfile()
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            farmProfileEmitter.observe(viewLifecycleOwner) { farmProfile ->
                if (farmProfile != null) {
                    with(binding) {
                        edtEditNameFarmProfile.setText(farmProfile.name)
                        val createdAt = formatDate(farmProfile.createdAt, "dd-MMMM-yyyy")
                        tvDateFarmProfile.text = createdAt
                        edtEditAddressOneFarmProfile.setText(farmProfile.addressOne)
                        edtEditAddressTwoFarmProfile.setText(farmProfile.addressTwo)
                        edtEditCityFarmProfile.setText(farmProfile.city)
                        edtEditProviceFarmProfile.setText(farmProfile.province)
                    }
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
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvDateFarmProfile)
            }

            R.id.btn_save_farm_profile -> {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveFarmProfile.isEnabled = !state

            btnSaveFarmProfile.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private val TAG = PersonalFarmProfileFragment::class.java.simpleName
    }
}