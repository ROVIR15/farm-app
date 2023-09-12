package com.vt.vt.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentUpdateSignUpProfileBinding
import com.vt.vt.utils.PickDatesUtils

class UpdateSignUpProfileFragment : Fragment() {
    private var _binding: FragmentUpdateSignUpProfileBinding? = null
    private val binding get() = _binding!!

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
            ivDatePicker.setOnClickListener{
                PickDatesUtils.setupDatePicker(requireActivity(), tvFarmCreatedDate)
            }
            signUpTvLogin.setOnClickListener {
                findNavController().navigate(R.id.action_updateSignUpProfileFragment_to_signInFragment)
            }
            btnSaveUpdateProfileSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_updateSignUpProfileFragment_to_signInFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}