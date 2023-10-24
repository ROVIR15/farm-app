package com.vt.vt.ui.bottom_navigation.keuangan

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.budget.BudgetBreakdownItem
import com.vt.vt.databinding.FragmentKeuanganBinding
import com.vt.vt.ui.bottom_navigation.keuangan.bottom_dialog.AddBudgetBottomSheetDialogFragment
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.convertRupiah
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class KeuanganFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentKeuanganBinding? = null
    private val binding get() = _binding!!

    private val listBudgetAdapter by lazy { BudgetAdapter() }
    private val budgetViewModel: BudgetViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeuanganBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarKeuangan.topAppBar.apply {
                title = "Pengelolaan Keuangan"
                inflateMenu(R.menu.menu_keuangan)
                setOnMenuItemClickListener(this@KeuanganFragment)
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.pickMonthAndYear(requireActivity(), tvDatePick) { selectedDate ->
                    budgetViewModel.updateSelectedDate(selectedDate)
                }
            }
            rvAnggaranRealisasi.adapter = listBudgetAdapter
            rvAnggaranRealisasi.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
        }
        observerView()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        budgetViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        budgetViewModel.currentDate.observe(viewLifecycleOwner) { selectedDate ->
            val inputDateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            val date = inputDateFormat.parse(selectedDate)
            binding.tvDatePick.text = date?.let { outputDateFormat.format(it) }
            budgetViewModel.loadBudgetByMonth(selectedDate.toString())
            binding.refreshPage.setOnRefreshListener {
                budgetViewModel.loadBudgetByMonth(selectedDate.toString())
                binding.refreshPage.isRefreshing = false
            }

        }
        budgetViewModel.budgetEmmiter.observe(viewLifecycleOwner) { budget ->
            binding.isEmpty.isEmpty.isVisible = budget.budgetBreakdown.isNullOrEmpty().apply {
                binding.isEmpty.isEmpty.text = "Tidak Ada Pengeluaran Bulan ini"
            }
            val budgetLeftValue = budget.budgetLeft.toString()
            val decimalBudgetValue = budgetLeftValue.replace(".", "").toBigDecimal()
            binding.tvBudgetLeft.text = decimalBudgetValue.convertRupiah()

            val totalExpenditureValue = budget.totalExpenditure.toString()
            val decimalTotalExpenditure = totalExpenditureValue.replace(".", "").toBigDecimal()
            binding.tvTotalBudgetAmount.text = decimalTotalExpenditure.convertRupiah()

            val totalBudgetAmountValue = budget.totalBudgetAmount.toString()
            val decimalTotalBudgetAmountValue =
                totalBudgetAmountValue.replace(".", "").toBigDecimal()
            binding.tvTotalExpenditure.text = decimalTotalBudgetAmountValue.convertRupiah()

            listBudget(budget.budgetBreakdown)
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listBudget(data: List<BudgetBreakdownItem>?) {
        listBudgetAdapter.submitList(data)
    }

    private fun adapterSpinner(binding: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun showLoading(state: Boolean) {
        binding.loading.progressBar.isVisible = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_income -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_keuangan_to_addIncomeFragment)
                return true
            }

            R.id.action_add_expenditure -> {
                val addBudgetBottomSheetDialog = AddBudgetBottomSheetDialogFragment()
                addBudgetBottomSheetDialog.show(
                    childFragmentManager,
                    addBudgetBottomSheetDialog::class.java.simpleName
                )
                return true
            }
        }
        return false
    }
}