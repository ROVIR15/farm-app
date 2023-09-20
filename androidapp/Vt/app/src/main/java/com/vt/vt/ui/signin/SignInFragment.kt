package com.vt.vt.ui.signin

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            signInBtnLogin.setOnClickListener {
                val username = signInCvEmail.text.toString()
                val password = signInCvPassword.text.toString()
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    signInViewModel.login(username, password)
                }
            }
            signInBtnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
        setWindowTouchable()
        observeView()
    }


    private fun observeView() {
        signInViewModel.loginState.observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                view?.findNavController()?.popBackStack()
            }
        }
        signInViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        signInViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        signInViewModel.isLogin.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            if (state) {
                progressBar.visibility = View.VISIBLE
                signInBtnLogin.isEnabled = false
                signInBtnSignUp.isEnabled = false
                signInBtnLogin.setBackgroundColor(Color.GRAY)
            } else {
                progressBar.visibility = View.GONE
                signInBtnLogin.isEnabled = true
                signInBtnSignUp.isEnabled = true
                signInBtnLogin.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.btn_blue_icon
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWindowTouchable() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            })

    }
}