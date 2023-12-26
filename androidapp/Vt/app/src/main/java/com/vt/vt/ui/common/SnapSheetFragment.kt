package com.vt.vt.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentSnapSheetBinding
import com.vt.vt.ui.file_provider.datakandang.AddCageFragment
import com.vt.vt.utils.createCustomTempFile
import com.vt.vt.utils.getRotateImage
import com.vt.vt.utils.uriToFile
import java.io.File

class SnapSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentSnapSheetBinding? = null
    private val binding get() = _binding!!
    private var snapSheetListener: SnapSheetListener? = null

    private lateinit var currentPhotoPath: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSnapSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rlCamera.setOnClickListener(this@SnapSheetFragment)
            rlGallery.setOnClickListener(this@SnapSheetFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragment = parentFragment

        if (fragment is SnapSheetListener) {
            this.snapSheetListener = fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.snapSheetListener = null
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
            snapSheetListener?.bitmapPhotos(filePhoto).also {
                dismiss()
            }
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
                snapSheetListener?.uriFile(selectedImg)
                dismiss()
            }
        }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_camera -> {
                startCamera()
            }

            R.id.rl_gallery -> {
                startGallery()
            }
        }
    }
}