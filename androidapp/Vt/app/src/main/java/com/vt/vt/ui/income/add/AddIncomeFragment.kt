package com.vt.vt.ui.income.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddIncomeBinding
import com.vt.vt.ui.income.IncomeViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class AddIncomeFragment : Fragment() {

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding get() = _binding!!

    private val incomeViewModel by viewModels<IncomeViewModel>()

    private var incomeCategoryId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomeViewModel.incomeCategories()
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
                val budget = amountBudget.getNumericValueBigDecimal()
                val limit = BigDecimal("100000000")
                if (dateSelected.isNotEmpty() && amount.isNotEmpty() && incomeCategoryId != null) {
                    if (budget <= limit) {
                        incomeViewModel.createIncome(
                            dateSelected,
                            budget,
                            remarks,
                            incomeCategoryId
                        )
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            R.string.maximal_budget,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.please_fill_all_column, Toast.LENGTH_SHORT)
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
        incomeViewModel.createIncomeEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), "${it.status}", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.popBackStack()
        }
        incomeViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.loading.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}