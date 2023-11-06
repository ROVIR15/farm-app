package com.vt.vt.ui.pemberian_ternak

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
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

    var isCallbacksSet = false
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
        receiveBlockId?.let { pemberianTernakViewModel.isHijauanButtonFilled(it) }

        observewView()
        with(binding) {
            binding.appBarLayout.topAppBar.apply {
                title = "Pemberian Ternak"
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
            val data = feedingStack[receiveBlockId]
            Log.d("PFT", "data from stack observer ${feedingStack}")
            if (data != null) {
                setupCallbacks(data)
                setupNavigationBack(data)
                handleUiBasedOnStackSize(data)
            }
        }
        pemberianTernakViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        // hijauan filled
        pemberianTernakViewModel.isHijauanButtonFilled(receiveBlockId!!)
            .observe(viewLifecycleOwner) { isButtonFilled ->
                Log.d("PFT", "hijauan ${isButtonFilled}")
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.isEnabled =
                    isButtonFilled
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView.setBackgroundColor(
                    if (!isButtonFilled) Color.GRAY
                    else ContextCompat.getColor(requireActivity(), R.color.white)
                )
            }
        pemberianTernakViewModel.isKimiaButtonFilled(receiveBlockId!!)
            .observe(viewLifecycleOwner) { isButtonFilled ->
                Log.d("PFT", "kimia ${isButtonFilled}")
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.isEnabled =
                    isButtonFilled
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView2.setBackgroundColor(
                    if (!isButtonFilled) Color.GRAY
                    else ContextCompat.getColor(requireActivity(), R.color.white)
                )
            }
        pemberianTernakViewModel.isVitaminButtonFilled(receiveBlockId!!)
            .observe(viewLifecycleOwner) { isButtonFilled ->
                Log.d("PFT", "vitamin ${isButtonFilled}")
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.isEnabled =
                    isButtonFilled
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView3.setBackgroundColor(
                    if (!isButtonFilled) Color.GRAY
                    else ContextCompat.getColor(requireActivity(), R.color.white)
                )
            }
        pemberianTernakViewModel.isTambahanButtonFilled(receiveBlockId!!)
            .observe(viewLifecycleOwner) { isButtonFilled ->
                Log.d("PFT", "tambahan ${isButtonFilled}")
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.isEnabled =
                    isButtonFilled
                binding.contentPemberianMakanTernak.contentCategoryPemberianTernak.cardView4.setBackgroundColor(
                    if (!isButtonFilled) Color.GRAY
                    else ContextCompat.getColor(requireActivity(), R.color.white)
                )
            }
    }

    private fun setupCallbacks(list: List<ConsumptionRecordItem>) {
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (list.isNotEmpty()) alertDialog()
                    else view?.findNavController()?.popBackStack()
                }
            })
    }

    private fun setupNavigationBack(list: List<ConsumptionRecordItem>) {
        binding.appBarLayout.topAppBar.setNavigationOnClickListener {
            if (list.isNotEmpty()) alertDialog()
            else view?.findNavController()?.popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleUiBasedOnStackSize(list: List<ConsumptionRecordItem>) {
        if (list.size == 4) {
            binding.contentPemberianMakanTernak.tvGreetingsFinish.visibility = View.VISIBLE

            binding.contentPemberianMakanTernak.tvUsernameGreetings.visibility = View.GONE
            binding.contentPemberianMakanTernak.contentHomeSubtitle.text =
                "Klik Simpan atau Batal untuk Membatalkan"

            binding.contentPemberianMakanTernak.btnSave.visibility = View.VISIBLE
            binding.contentPemberianMakanTernak.btnCancel.visibility = View.VISIBLE

            binding.contentPemberianMakanTernak.btnSave.setOnClickListener {
                pemberianTernakViewModel.createFeedingRecord(
                    consumptionRecord = list
                )
            }

            binding.contentPemberianMakanTernak.btnCancel.setOnClickListener {
                pemberianTernakViewModel.clearSessionFeeding()
                pemberianTernakViewModel.clear()
                view?.findNavController()?.popBackStack()
            }
        } else {
            binding.contentPemberianMakanTernak.tvGreetingsFinish.visibility = View.GONE
            binding.contentPemberianMakanTernak.tvUsernameGreetings.visibility = View.VISIBLE
            binding.contentPemberianMakanTernak.contentHomeSubtitle.visibility = View.VISIBLE
            binding.contentPemberianMakanTernak.btnSave.visibility = View.GONE
            binding.contentPemberianMakanTernak.btnCancel.visibility = View.GONE
        }
    }

    fun alertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Peringatan")
        builder.setMessage("Apakah anda yakin ingin menghapus data ?")
        builder.setPositiveButton("Ya") { dialog, which ->
            pemberianTernakViewModel.clearSessionFeeding()
            pemberianTernakViewModel.clear()
            view?.findNavController()?.popBackStack()
            dialog.dismiss()
        }
        builder.setNegativeButton("Tidak") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}