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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

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
        }

        observerView()
        profileViewModel.getUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerView() {
        profileViewModel.isError().observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireActivity(), error.toString(), Toast.LENGTH_SHORT).show()
        }
        profileViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        profileViewModel.getUser.observe(viewLifecycleOwner) {
            binding.tvProfileUsername.text = it?.username
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
                    .navigate(R.id.action_navigation_profile_to_dataBarangDanJasaFragment)
            }

            R.id.profile_animal_cage_area -> {
                v.findNavController()
                    .navigate(R.id.action_navigation_profile_to_detailAreaBlockFragment)
            }

        }
    }
}