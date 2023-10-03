package com.vt.vt.ui.rekam_bcs

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentRekamBCSBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import com.vt.vt.utils.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RekamBCSFragment : Fragment() {

    private var _binding: FragmentRekamBCSBinding? = null
    private val binding get() = _binding!!

    private val rekamBCSViewModel by viewModels<RekamBCSViewModel>()
    private val livestockViewModel by viewModels<EditLivestockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamBCSBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())

        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Rekam BCS"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            btnSimpanRekamBcs.setOnClickListener {
                val score = editTextRekamBcs.text.toString().trim()
                val date = getCurrentDate()
                if (score.isNotEmpty())
                    rekamBCSViewModel.createBcsRecordById(receiveId, date, score.toDouble(), "None")
                else {
                    Toast.makeText(requireActivity(), "Lengkapi Kolom", Toast.LENGTH_SHORT).show()
                }
            }
            btnBatalRekamBcs.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        observerView()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                with(binding) {
                    tvTitleLivestock.text = livestock?.name.toString()
                    tvBangsaAnimal.text = livestock?.bangsa.toString()
                    tvDescriptionLivestock.text = livestock?.description.toString()
                    when (livestock?.gender) {
                        1 -> {
                            tvDetailLivestockAnimalGender.text = "Jantan"
                        }

                        2 -> {
                            tvDetailLivestockAnimalGender.text = "Betina"
                        }

                        else -> {
                            tvDetailLivestockAnimalGender.text = "None"
                        }
                    }
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        rekamBCSViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            createBcsRecordEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(
                    requireActivity(),
                    "${it.status} menambahkan bcs",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpanRekamBcs.isEnabled = !state
            btnBatalRekamBcs.isEnabled = !state

            btnSimpanRekamBcs.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )
            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }
}