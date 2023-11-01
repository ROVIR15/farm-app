package com.vt.vt.ui.pemberian_ternak

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentPemberianTernakBinding
import com.vt.vt.ui.pemberian_ternak.hijauan.HijauanViewModel
import com.vt.vt.ui.profile.personal_profile.PersonalProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PemberianTernakFragment : Fragment() {

    private var _binding: FragmentPemberianTernakBinding? = null
    private val binding get() = _binding!!

    private lateinit var mBundle: Bundle

    private val hijauanViewModel by viewModels<HijauanViewModel>()
    private val pemberianTernakViewModel by viewModels<PemberianTernakViewModel>()
    private val personalProfileViewModel by viewModels<PersonalProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemberianTernakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("id")
        mBundle = Bundle().apply {
            receiveBlockId?.let {
                putInt("blockId", it)
                putInt("feedCategoryHijauanId", 1)
                putInt("feedCategoryKimiaId", 2)
                putInt("feedCategoryVitaminId", 3)
                putInt("feedCategoryTambahanId", 4)
            }
        }

        observewView()
        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Pemberian Ternak"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_hijauanFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_kimiaFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_vitaminFragment, mBundle)
            }
            contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_pemberianTernakFragment_to_tambahanFragment, mBundle)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observewView() {
        personalProfileViewModel.getProfile()
        personalProfileViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.progressBar.isVisible = isLoading
        }
        personalProfileViewModel.getProfileEmitter.observe(viewLifecycleOwner) { data ->
            binding.contentPemberianMakanTernak.tvUsernameGreetings.text =
                "Hello, ${data.message?.name}👋"
        }
        personalProfileViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        pemberianTernakViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.loading.progressBar.isVisible = isLoading
        }
        pemberianTernakViewModel.feedingEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
        pemberianTernakViewModel.stack.observe(viewLifecycleOwner) { feedingStack ->
            if (feedingStack.size == 4) {
                binding.contentPemberianMakanTernak.btnSave.visibility = View.VISIBLE
                binding.contentPemberianMakanTernak.btnCancel.visibility = View.VISIBLE
                binding.contentPemberianMakanTernak.btnSave.setOnClickListener {
                    pemberianTernakViewModel.createFeedingRecord(feedingStack)
                }
                binding.contentPemberianMakanTernak.btnCancel.setOnClickListener {
                    view?.findNavController()?.popBackStack()
                }
            } else {
                binding.contentPemberianMakanTernak.btnSave.visibility = View.GONE
                binding.contentPemberianMakanTernak.btnCancel.visibility = View.GONE
            }
        }
        pemberianTernakViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        // hijauan filled
        pemberianTernakViewModel.isHijauanFilled.observe(viewLifecycleOwner) { isFilled ->
            Log.d("PFT", "hijauan ${isFilled}")
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.isEnabled = !isFilled
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.setBackgroundColor(
                if (isFilled) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.white)
            )
        }
        pemberianTernakViewModel.isKimiaFilled.observe(viewLifecycleOwner) { isFilled ->
            Log.d("PFT", "kimia ${isFilled}")
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.isEnabled = !isFilled
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.setBackgroundColor(
                if (isFilled) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.white)
            )
        }
        pemberianTernakViewModel.isVitaminFilled.observe(viewLifecycleOwner) { isFilled ->
            Log.d("PFT", "vitamin ${isFilled}")
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.isEnabled =
                !isFilled
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.setBackgroundColor(
                if (isFilled) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.white)
            )
        }
        pemberianTernakViewModel.isTambahanFilled.observe(viewLifecycleOwner) { isFilled ->
            Log.d("PFT", "tambahan ${isFilled}")
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.isEnabled =
                !isFilled
            binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.setBackgroundColor(
                if (isFilled) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.white)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // when user go back from this page the stack will be clear
        pemberianTernakViewModel.clear()
        pemberianTernakViewModel.clearSessionFeeding()
        _binding = null
    }
}