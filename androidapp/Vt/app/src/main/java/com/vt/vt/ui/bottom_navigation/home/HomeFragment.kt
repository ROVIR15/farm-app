package com.vt.vt.ui.bottom_navigation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentHomeBinding
import com.vt.vt.ui.profile.personal_profile.PersonalProfileViewModel
import com.vt.vt.ui.signin.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val signInViewModel: SignInViewModel by viewModels()
    private val personalProfileViewModel: PersonalProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.hide()
        personalProfileViewModel.getProfile()
        signInViewModel.loginState.observe(viewLifecycleOwner) { user ->
            if (user.token.isBlank() or user.token.isEmpty()) {
                view.findNavController().navigate(R.id.action_navigation_home_to_signInFragment)
            }
        }

        with(binding) {
            contentHome.homeRefreshLayout.isRefreshing = false
            contentHome.homeRefreshLayout.isEnabled = false
            contentHome.contentHomeCategoryAddLivestock.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_home_to_addLivestockFragment)
            }
            contentHome.contentHomeCategoryAddExpenses.setOnClickListener {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
            contentHome.contentHomeCategorySeeCage.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_navigation_home_to_penyimpanTernakFragment)
            }
            contentHome.imgBtnBreeding.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_home_to_breedingFragment)
            }
            contentHome.imgBtnFattening.setOnClickListener {
                it.findNavController().navigate(R.id.action_navigation_home_to_fatteningFragment)
            }
        }
        observerView()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        personalProfileViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.contentHome.loading.progressBar.isVisible = isLoading
            }
            getProfileEmitter.observe(viewLifecycleOwner) { data ->
                binding.contentHome.contentHomeGreeting.text = "Hello, ${data.message?.name}👋"
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}