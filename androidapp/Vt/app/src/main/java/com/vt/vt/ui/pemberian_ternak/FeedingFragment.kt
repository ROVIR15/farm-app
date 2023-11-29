package com.vt.vt.ui.pemberian_ternak

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.databinding.FragmentPemberianTernakBinding
import com.vt.vt.ui.pemberian_ternak.bottondialog.ConfirmationFeedingBottomDialogFragment
import com.vt.vt.ui.profile.personal_profile.PersonalProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedingFragment : Fragment() {

    private var _binding: FragmentPemberianTernakBinding? = null
    private val binding get() = _binding!!

    private lateinit var mBundle: Bundle

    private val feedingViewModel by viewModels<FeedingViewModel>()
    private val personalProfileViewModel by viewModels<PersonalProfileViewModel>()

    var receiveBlockId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPemberianTernakBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveBlockId = arguments?.getInt("id")
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
            binding.appBarLayout.topAppBar.apply {
                title = "Pemberian Ternak"
                setNavigationOnClickListener { view.findNavController().popBackStack() }
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
        feedingViewModel.load().observe(viewLifecycleOwner) { source ->
            handleUIFeeding(source)
        }
    }

    private fun handleUIFeeding(source: Map<Int, List<ConsumptionRecordItem>>) {
        val listConsumptionRecord = source[receiveBlockId]
        Log.d("FEEDING", "all list consumption : ${listConsumptionRecord}")
        if (!listConsumptionRecord.isNullOrEmpty()) {
            if (listConsumptionRecord.isNotEmpty()) {
                if (listConsumptionRecord.size >= 4) {
                    binding.contentPemberianMakanTernak.tvGreetingsFinish.visibility = View.VISIBLE
                    binding.contentPemberianMakanTernak.tvUsernameGreetings.visibility = View.GONE
                    binding.contentPemberianMakanTernak.contentHomeSubtitle.visibility =
                        View.VISIBLE
                }
                binding.contentPemberianMakanTernak.btnSave.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        val confirmationFeeding = ConfirmationFeedingBottomDialogFragment()
                        val mBundle = Bundle()
                        receiveBlockId?.let { blockId -> mBundle.putInt("blockId", blockId) }
                        confirmationFeeding.arguments = mBundle
                        confirmationFeeding.show(
                            childFragmentManager, confirmationFeeding::class.java.simpleName
                        )
                    }
                }
                binding.contentPemberianMakanTernak.btnCancel.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        feedingViewModel.clearSessionFeeding(receiveBlockId!!)
                    }
                }
            }
        } else {
            binding.contentPemberianMakanTernak.tvGreetingsFinish.visibility = View.GONE
            binding.contentPemberianMakanTernak.tvUsernameGreetings.visibility = View.VISIBLE
            binding.contentPemberianMakanTernak.contentHomeSubtitle.visibility = View.VISIBLE
            binding.contentPemberianMakanTernak.btnSave.visibility = View.GONE
            binding.contentPemberianMakanTernak.btnCancel.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}