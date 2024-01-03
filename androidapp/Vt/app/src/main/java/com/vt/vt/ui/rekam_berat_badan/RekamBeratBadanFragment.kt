package com.vt.vt.ui.rekam_berat_badan

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
import com.vt.vt.databinding.FragmentRekamBeratBadanBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RekamBeratBadanFragment : Fragment() {

    private var _binding: FragmentRekamBeratBadanBinding? = null
    private val binding get() = _binding!!

    private val rekamBeratBadanViewModel by viewModels<RekamBeratBadanViewModel>()
    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamBeratBadanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receiveId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveId.toString())

        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Rekam Berat Badan"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }

            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvShowDate)
            }
            btnSimpanRekamBeratBadan.setOnClickListener {
                val score = editTextRekamBeratBadan.text.toString().trim()
                val date = formatterDateFromCalendar(tvShowDate.text.toString())
                if (score.isNotEmpty() && date.isNotEmpty()) {
                    rekamBeratBadanViewModel.createWeightRecordById(
                        receiveId,
                        date,
                        score.toDouble(),
                        "None"
                    )
                } else {
                    Toast.makeText(requireActivity(), "Lengkapi Kolom", Toast.LENGTH_SHORT).show()
                }
            }
            btnBatalRekamBeratBadan.setOnClickListener {
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
                    tvInfo.text = livestock?.info.toString()
                    tvDescriptionLivestock.text = livestock?.description.toString()
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        rekamBeratBadanViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            createWeightRecordEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(
                    requireActivity(),
                    "${it.status} menambahkan berat badan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpanRekamBeratBadan.isEnabled = !state
            btnBatalRekamBeratBadan.isEnabled = !state

            btnSimpanRekamBeratBadan.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )
            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}