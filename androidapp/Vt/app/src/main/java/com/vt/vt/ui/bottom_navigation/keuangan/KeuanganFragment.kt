package com.vt.vt.ui.bottom_navigation.keuangan

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vt.vt.R
import com.vt.vt.core.data.source.base.BaseSwipeToDeleteAdapter
import com.vt.vt.core.data.source.remote.budget.dto.BudgetBreakdownItem
import com.vt.vt.core.data.source.remote.income.dto.IncomesItem
import com.vt.vt.databinding.FragmentKeuanganBinding
import com.vt.vt.ui.bottom_navigation.keuangan.bottom_dialog.AddBudgetBottomSheetDialogFragment
import com.vt.vt.ui.income.IncomeViewModel
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
    private val listIncomeAdapter by lazy { IncomeAdapter() }

    private val incomeViewModel: IncomeViewModel by viewModels()
    private val budgetViewModel: BudgetViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
            rvIncomes.adapter = listIncomeAdapter
            rvIncomes.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
            initActionBudget()
            initActionIncomes()
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
        budgetViewModel.budgetEmitter.observe(viewLifecycleOwner) { budget ->
            val isBudgetBreakdownEmpty = budget.budgetBreakdown.isNullOrEmpty()
            val isIncomesEmpty = budget.incomes.isNullOrEmpty()

            binding.anggaranText.isVisible = !isBudgetBreakdownEmpty
            binding.incomeText.isVisible = !isIncomesEmpty
            binding.horizontalLine1.isVisible = !isBudgetBreakdownEmpty
            binding.horizontalLine2.isVisible = !isIncomesEmpty
            binding.isEmpty.isEmpty.isVisible = isBudgetBreakdownEmpty && isIncomesEmpty

            if (isBudgetBreakdownEmpty && isIncomesEmpty) {
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
            listIncome(budget.incomes)
        }
        budgetViewModel.deleteBudgetEmitter.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventMessage ->
                Toast.makeText(requireActivity(), "${eventMessage.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
        incomeViewModel.isDeleted.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventMessage ->
                Toast.makeText(
                    requireActivity(), eventMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
        incomeViewModel.deleteIncomeEmitter.observe(viewLifecycleOwner) {
            if (it.status.equals(other = "success", ignoreCase = true)) {
                budgetViewModel.currentDate.observe(viewLifecycleOwner) { selectedDate ->
                    budgetViewModel.loadBudgetByMonth(selectedDate.toString())
                }
            }
        }
        incomeViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun listBudget(data: List<BudgetBreakdownItem>?) {
        listBudgetAdapter.submitList(data)
    }

    private fun listIncome(data: List<IncomesItem>?) {
        listIncomeAdapter.submitList(data)
    }

    private fun initActionBudget() {
        val swipeHandler = object : BaseSwipeToDeleteAdapter(requireActivity()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                AlertDialog.Builder(requireContext()).setTitle(R.string.title_alert_delete_item)
                    .setIcon(R.drawable.ic_outline_delete_outline_24)
                    .setMessage(R.string.message_delete_item)
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        val item = listBudgetAdapter.currentList[position]
                        budgetViewModel.deleteBudgetById(item.id.toString())
                        listBudgetAdapter.notifyItemRemoved(position)
                        dialog.dismiss()
                    }.setNegativeButton(R.string.no) { dialog, _ ->
                        listBudgetAdapter.notifyItemChanged(position)
                        dialog.dismiss()
                    }.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvAnggaranRealisasi)
    }

    private fun initActionIncomes() {
        val swipeHandler = object : BaseSwipeToDeleteAdapter(requireActivity()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                AlertDialog.Builder(requireContext()).setTitle(R.string.title_alert_delete_item)
                    .setIcon(R.drawable.ic_outline_delete_outline_24)
                    .setMessage(R.string.message_delete_item)
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        val item = listIncomeAdapter.currentList[position].id
                        incomeViewModel.deleteIncomeById(item.toString())
                        listIncomeAdapter.notifyItemRemoved(position)
                        dialog.dismiss()
                    }.setNegativeButton(R.string.no) { dialog, _ ->
                        listIncomeAdapter.notifyItemChanged(position)
                        dialog.dismiss()
                    }.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.rvIncomes)
    }


    private fun showLoading(state: Boolean) {
        binding.loading.progressBar.isVisible = state
        binding.anggaranText.isVisible = !state
        binding.incomeText.isVisible = !state
        binding.horizontalLine1.isVisible = !state
        binding.horizontalLine2.isVisible = !state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_budget -> {
                val addBudgetBottomSheetDialog = AddBudgetBottomSheetDialogFragment()
                addBudgetBottomSheetDialog.show(
                    childFragmentManager, addBudgetBottomSheetDialog::class.java.simpleName
                )
                return true
            }

            R.id.action_add_income -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_keuangan_to_addIncomeFragment)
                return true
            }

            R.id.action_add_expenditure -> {
                val addBudgetBottomSheetDialog = AddBudgetBottomSheetDialogFragment()
                addBudgetBottomSheetDialog.show(
                    childFragmentManager, addBudgetBottomSheetDialog::class.java.simpleName
                )
                return true
            }
        }
        return false
    }
}