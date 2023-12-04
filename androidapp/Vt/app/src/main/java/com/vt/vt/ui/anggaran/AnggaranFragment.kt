package com.vt.vt.ui.anggaran

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.budget.ExpendituresItem
import com.vt.vt.databinding.FragmentAnggaranBinding
import com.vt.vt.ui.anggaran.adapter.ListBudgetExpenditureAdapter
import com.vt.vt.ui.bottom_navigation.keuangan.BudgetViewModel
import com.vt.vt.ui.pengeluaran.ExpenditureViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnggaranFragment : Fragment() {

    private var _binding: FragmentAnggaranBinding? = null
    private val binding get() = _binding!!
    private var budgetId: Int? = null
    private val budgetViewModel by viewModels<BudgetViewModel>()
    private val expenditureViewModel by viewModels<ExpenditureViewModel>()
    private val listBudgetExpenditureAdapter by lazy {
        ListBudgetExpenditureAdapter(
            expenditureViewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnggaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetId = arguments?.getInt("id")
        val categoryId = arguments?.getInt("categoryId")
        val mBundle = Bundle().apply {
            if (categoryId != null) putInt("categoryId", categoryId)
        }

        budgetViewModel.getBudgetById(budgetId.toString())
        with(binding) {
            anggaranAppBar.topAppBar.apply {
                title = "Anggaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            footerAnggaranForm.btnAddExpenditure.setOnClickListener { view ->
                view.findNavController()
                    .navigate(R.id.action_anggaranFragment_to_addPengeluaranFragment, mBundle)
            }
            rvPengeluaran.apply {
                adapter = listBudgetExpenditureAdapter
                layoutManager = LinearLayoutManager(
                    requireActivity(), LinearLayoutManager.VERTICAL, false
                )
            }
        }
        observerView()
    }

    private fun observerView() {
        budgetViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        expenditureViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        budgetViewModel.budgetByIdEmitter.observe(viewLifecycleOwner) { budget ->
            binding.dataEmpty.isEmpty.isVisible = budget.expenditures.isNullOrEmpty()
            val budgetAmount = budget.amount
            binding.amountBudget.setText(budgetAmount.toString())
            binding.categoryBudget.text = budget.budgetCategoryName
            listExpenditures(budget.expenditures)
        }
        expenditureViewModel.deleteExpenditureEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
            budgetViewModel.getBudgetById(budgetId.toString())
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireActivity(),
                it.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
        expenditureViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireActivity(),
                it.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun listExpenditures(data: List<ExpendituresItem>?) {
        listBudgetExpenditureAdapter.submitList(data)
    }

    private fun showLoading(state: Boolean) {
        binding.footerAnggaranForm.btnAddExpenditure.apply {
            isEnabled = !state
            setBackgroundColor(
                if (state) Color.GRAY else ContextCompat.getColor(
                    requireActivity(),
                    R.color.green_grass
                )
            )
            setTextColor(
                if (state) Color.BLACK else ContextCompat.getColor(
                    requireActivity(),
                    R.color.green_text_button
                )
            )
        }
        binding.loading.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}