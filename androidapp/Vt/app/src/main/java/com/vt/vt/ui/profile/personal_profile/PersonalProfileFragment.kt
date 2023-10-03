package com.vt.vt.ui.profile.personal_profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPersonalProfileBinding
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentPersonalProfileBinding? = null
    private val binding get() = _binding!!

    private val personalProfileViewModel by viewModels<PersonalProfileViewModel>()

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
        observerView()
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

}