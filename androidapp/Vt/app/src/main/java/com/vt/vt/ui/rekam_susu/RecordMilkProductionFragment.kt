package com.vt.vt.ui.rekam_susu

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentRecordMilkProductionBinding
import com.vt.vt.ui.edit_livestock.EditLivestockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordMilkProductionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRecordMilkProductionBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<EditLivestockViewModel>()
    private val milkProductionViewModel by viewModels<MilkProductionViewModel>()

    private var receiveLivestockId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecordMilkProductionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveLivestockId = arguments?.getInt("livestockId")
        livestockViewModel.getLivestockById(receiveLivestockId.toString())
        binding.appBarLayout.topAppBar.apply {
            title = "Rekam Produksi Susu"
            setNavigationOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        binding.ivDatePicker.setOnClickListener {
            PickDatesUtils.setupDatePicker(requireActivity(), binding.tvShowDate)
        }
        binding.btnSave.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        observerView()
    }

    private fun observerView() {
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        livestockViewModel.getLivestockById.observe(viewLifecycleOwner) { livestock ->
            with(binding) {
                tvTitleLivestock.text = livestock?.name.toString()
                tvInfo.text = livestock?.info.toString()
                tvDescriptionLivestock.text = livestock?.description.toString()
            }
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        milkProductionViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        milkProductionViewModel.createMilkProductionEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        milkProductionViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSave.isEnabled = !state
            btnCancel.isEnabled = !state

            btnSave.setBackgroundColor(
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                val scoreMilk =
                    binding.editTextMilkProductionRecord.text.toString().trim()
                val date = formatterDateFromCalendar(binding.tvShowDate.text.toString())
                val alertScoreLessThan =
                    getString(R.string.score_less_or_more_than, "lebih dari ", 0)

                val alertScoreMoreThan =
                    getString(R.string.score_less_or_more_than, "kurang dari ", 200)
                if (scoreMilk.isNotEmpty() && date.isNotEmpty()) {
                    if (scoreMilk.toDouble() <= 0) {
                        Toast.makeText(requireActivity(), alertScoreLessThan, Toast.LENGTH_SHORT)
                            .show()
                    } else if (scoreMilk.toDouble() >= 200) {
                        Toast.makeText(requireActivity(), alertScoreMoreThan, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.d(TAG, "onClick: $scoreMilk at $date")
                        milkProductionViewModel.createMilkProduction(
                            livestockId = receiveLivestockId,
                            date = date,
                            score = scoreMilk.toDouble(),
                            remarks = "None"
                        )
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.please_fill_all_column,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            R.id.btn_cancel -> {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        private val TAG = RecordMilkProductionFragment::class.java.simpleName
    }
}