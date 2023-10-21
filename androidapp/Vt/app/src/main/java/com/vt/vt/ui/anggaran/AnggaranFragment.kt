package com.vt.vt.ui.anggaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.budget.ExpendituresItem
import com.vt.vt.databinding.FragmentAnggaranBinding
import com.vt.vt.ui.anggaran.adapter.ListBudgetExpenditureAdapter
import com.vt.vt.ui.bottom_navigation.keuangan.BudgetViewModel
import com.vt.vt.utils.formatAsIDR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnggaranFragment : Fragment() {

    private var _binding: FragmentAnggaranBinding? = null
    private val binding get() = _binding!!
    private val listBudgetExpenditureAdapter by lazy { ListBudgetExpenditureAdapter() }
    private val budgetViewModel by viewModels<BudgetViewModel>()
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
        val budgetId = arguments?.getInt("id")
        budgetViewModel.getBudgetById(budgetId.toString())
        with(binding) {
            anggaranAppBar.topAppBar.apply {
                title = "Anggaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            footerAnggaranForm.btnTambahAnggaran.setOnClickListener {
                findNavController().navigate(R.id.action_anggaranFragment_to_addPengeluaranFragment)
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
        budgetViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.loading.progressBar.isVisible = it
        }
        budgetViewModel.budgetByIdEmmiter.observe(viewLifecycleOwner) { budget ->
            binding.dataEmpty.isEmpty.isVisible = budget.expenditures.isNullOrEmpty()
            val budgetAmount = budget.amount?.let { formatAsIDR(it) }
            binding.amountBudget.setText(budgetAmount.toString())
            listExpenditures(budget.expenditures)
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) {
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

    override fun onDestroy() {
        super.onDestroy()
    }

}