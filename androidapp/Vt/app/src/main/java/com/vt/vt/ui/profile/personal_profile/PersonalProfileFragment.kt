package com.vt.vt.ui.profile.personal_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPersonalProfileBinding
import com.vt.vt.utils.PickDatesUtils

class PersonalProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPersonalProfileBinding? = null
    private val binding get() = _binding!!

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
                title = "Profile Pribadi"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivDatePicker.setOnClickListener(this@PersonalProfileFragment)
            btnSavePersonalProfile.setOnClickListener(this@PersonalProfileFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_date_picker -> {
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvDatePersonalProfile)
            }

            R.id.btn_save_personal_profile -> {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}