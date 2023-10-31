package com.vt.vt.ui.income.edit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditIncomeBinding
import com.vt.vt.ui.income.IncomeViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditIncomeFragment : Fragment() {
    private var _binding: FragmentEditIncomeBinding? = null
    private val binding get() = _binding!!

    private val incomeViewModel by viewModels<IncomeViewModel>()

    private var incomeCategoryId: Int? = null
    private var receiveIncomeId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveIncomeId = arguments?.getInt("incomeId")
        incomeViewModel.incomeCategories()
        incomeViewModel.getIncomeById(receiveIncomeId.toString())
        with(binding) {
            appBar.topAppBar.title = "Pendapatan"
            appBar.topAppBar.setNavigationOnClickListener {
                view.findNavController().popBackStack()
            }
            containerDate.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), incomeCreatedAt, true)
            }
            btnSave.setOnClickListener {
                val formatter = incomeCreatedAt.text.toString()
                val dateSelected = formatterDateFromCalendar(formatter)
                val remarks = edtDescription.text.toString().trim()
                val amount = amountBudget.text.toString()
                if (dateSelected.isNotEmpty() && amount.isNotEmpty() && incomeCategoryId != null && receiveIncomeId != null) {
                    incomeViewModel.updateIncome(
                        receiveIncomeId.toString(),
                        dateSelected,
                        amount.toDouble(),
                        remarks,
                        incomeCategoryId
                    )
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnCancel.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        observerView()
    }

    private fun observerView() {
        incomeViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        incomeViewModel.incomeCategoriesEmitter.observe(viewLifecycleOwner) { category ->
            val categoryArrays = category.map { item ->
                item.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, categoryArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
            binding.spinner.selected { position ->
                incomeCategoryId = category[position].id
            }
        }
        incomeViewModel.updateIncomeEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
        incomeViewModel.incomeByIdEmitter.observe(viewLifecycleOwner) { income ->
            binding.incomeCreatedAt.text = income.date.toString()
            binding.amountBudget.setText(income.amount.toString())
            binding.edtDescription.setText(income.remarks)
        }
        incomeViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.loading.progressBar.isVisible = state
        binding.btnSave.isEnabled = !state
        binding.btnCancel.isEnabled = !state

        binding.btnSave.setBackgroundColor(
            if (state) Color.GRAY
            else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}