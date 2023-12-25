package com.vt.vt.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentSnapSheetBinding

class SnapSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentSnapSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
                // TODO: ACTIVE CAMERA WHEN PERMISSION GRANTED
            }

            R.id.rl_gallery -> {
                //TODO: ACTIVE GALLERY WHEN PERMISSION GRANTED
            }
        }
    }
}