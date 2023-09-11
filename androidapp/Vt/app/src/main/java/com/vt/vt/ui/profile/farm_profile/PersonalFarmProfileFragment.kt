package com.vt.vt.ui.profile.farm_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPersonalFarmProfileBinding
import com.vt.vt.utils.PickDatesUtils

class PersonalFarmProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPersonalFarmProfileBinding? = null
    private val binding get() = _binding!!

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
                title = "Profile Perternakan"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivDatePicker.setOnClickListener(this@PersonalFarmProfileFragment)
            btnSaveFarmProfile.setOnClickListener(this@PersonalFarmProfileFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_date_picker -> {
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvDateFarmProfile)
            }

            R.id.btn_save_farm_profile -> {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
        }
    }
}