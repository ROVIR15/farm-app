package com.vt.vt.ui.forgot_password

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
import com.vt.vt.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ForgotPasswordViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnChangePassword.setOnClickListener(this@ForgotPasswordFragment)
        }
        observerView()
    }

    private fun observerView() {
        viewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.changePasswordEmitter.observe(viewLifecycleOwner) { response ->
            Toast.makeText(requireContext(), "${response?.message}", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        viewModel.isError().observe(viewLifecycleOwner) { isError ->
            Toast.makeText(requireActivity(), isError, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            if (state) {
                progressBar.visibility = View.VISIBLE
                btnChangePassword.isEnabled = false
                btnChangePassword.setBackgroundColor(Color.GRAY)
            } else {
                progressBar.visibility = View.GONE
                btnChangePassword.isEnabled = true
                btnChangePassword.setBackgroundColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.btn_blue_icon
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_change_password -> {
                val password = binding.password.text.toString().trim()
                val oldPassword = binding.cvOldPassword.text.toString().trim()
                val newPassword = binding.cvNewPassword.text.toString().trim()
                if (password.isNotEmpty() && password.isNotEmpty() && newPassword.isNotEmpty()) {
                    if (password != oldPassword) {
                        Toast.makeText(
                            requireActivity(),
                            R.string.password_is_not_matching_or_empty,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.doChangePassword(
                            password = password,
                            oldPassword = oldPassword,
                            newPassword = newPassword
                        )
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.password_is_not_matching_or_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}