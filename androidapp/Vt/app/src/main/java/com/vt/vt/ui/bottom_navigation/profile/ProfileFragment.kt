package com.vt.vt.ui.bottom_navigation.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentProfileBinding
import com.vt.vt.ui.profile.personal_profile.PersonalProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private val personalProfileViewModel: PersonalProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnLogout.setOnClickListener {
                profileViewModel.logout()
            }
            personalProfile.setOnClickListener(this@ProfileFragment)
            farmProfile.setOnClickListener(this@ProfileFragment)
            profileGoodsAndService.setOnClickListener(this@ProfileFragment)
            profileAnimalCageArea.setOnClickListener(this@ProfileFragment)
            profileAnimalAddCageArea.setOnClickListener(this@ProfileFragment)
            tvForgotPassword.setOnClickListener(this@ProfileFragment)
        }

        observerView()
    }

    private fun observerView() {
        personalProfileViewModel.getProfile()
        personalProfileViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        personalProfileViewModel.getProfileEmitter.observe(viewLifecycleOwner) {
            binding.tvProfileUsername.text = it?.message?.name
        }
        personalProfileViewModel.isError().observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }
        profileViewModel.isLogout.observe(viewLifecycleOwner) {
            view?.findNavController()?.navigate(R.id.action_navigation_profile_to_signInFragment)
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            if (state) {
                progressBar.visibility = View.VISIBLE
                btnLogout.isEnabled = false
                btnLogout.setBackgroundColor(Color.GRAY)
            } else {
                progressBar.visibility = View.GONE
                btnLogout.isEnabled = true
                btnLogout.setBackgroundColor(Color.BLACK)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.personal_profile -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_personalProfileFragment)
            }

            R.id.farm_profile -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_personalFarmProfileFragment)
            }

            R.id.profile_goods_and_service -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_data_items_and_service_fragment)
            }

            R.id.profile_animal_cage_area -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_penyimpanTernakFragment)
            }

            R.id.profile_animal_add_cage_area -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_dataKandangFragment)
            }

            R.id.tv_forgot_password -> {
                v.findNavController().navigate(R.id.action_navigation_profile_to_forgotPasswordFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}