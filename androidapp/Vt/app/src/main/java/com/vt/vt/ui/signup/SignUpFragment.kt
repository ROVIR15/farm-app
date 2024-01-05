package com.vt.vt.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this.signUpBtnCreateAccount.setOnClickListener { view ->
                val username = signUpCvUsername.text.toString().trim()
                val email = signUpCvEmail.text.toString().trim()
                val password = signUpCvPassword.text.toString().trim()
                val retypePassword = signUpCvRetypePassword.text.toString().trim()
                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && retypePassword.isNotEmpty() && password == retypePassword) {
                    val bundle = Bundle().apply {
                        putString("username", username)
                        putString("email", email)
                        putString("password", password)
                    }
                    view.findNavController()
                        .navigate(R.id.action_signUpFragment_to_updateSignUpProfileFragment, bundle)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.password_is_not_matching_or_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}