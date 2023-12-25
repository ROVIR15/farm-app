package com.vt.vt.ui.signup

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.auth.dto.register.request.FarmProfile
import com.vt.vt.databinding.FragmentUpdateSignUpProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateSignUpProfileFragment : Fragment() {
    private var _binding: FragmentUpdateSignUpProfileBinding? = null
    private val binding get() = _binding!!

    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateSignUpProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            signUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_updateSignUpProfileFragment_to_signInFragment)
            }
            btnSignUp.setOnClickListener {
                val username = arguments?.getString("username").toString()
                val email = arguments?.getString("email").toString()
                val password = arguments?.getString("password").toString()
                val farmName = updateSignUpCvFarmName.text.toString().trim()
                val addressOne = edtEditAddressOneFarmProfile.text.toString().trim()
                val addressTwo = edtEditAddressTwoFarmProfile.text.toString().trim()
                val city = edtEditCityFarmProfile.text.toString().trim()
                val province = edtEditProviceFarmProfile.text.toString().trim()
                val farmProfile = FarmProfile(
                    farmName, addressOne, addressTwo, city, province
                )
                signUpViewModel.registration(username, email, password, farmProfile)
            }
        }
        observerView()
    }

    private fun observerView() {
        signUpViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        signUpViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        signUpViewModel.isRegistration.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_updateSignUpProfileFragment_to_signInFragment)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSignUp.isEnabled = false
            binding.btnSignUp.setBackgroundColor(Color.GRAY)
        } else {
            binding.progressBar.visibility = View.GONE
            binding.btnSignUp.isEnabled = true
            binding.btnSignUp.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.btn_blue_icon
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}