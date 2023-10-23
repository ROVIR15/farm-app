package com.vt.vt.ui.bottom_navigation.keuangan.bottom_dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddBudgetBottomSheetDialogBinding
import com.vt.vt.ui.bottom_navigation.keuangan.BudgetViewModel
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBudgetBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddBudgetBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private val budgetViewModel by viewModels<BudgetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBudgetBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetViewModel.getCategoriesBudget()
        observerView()
    }

    private fun observerView() {
        budgetViewModel.observeLoading().observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        budgetViewModel.categoriesBudgetEmitter.observe(viewLifecycleOwner) { category ->
            val categoryArrays = category.map { item ->
                item.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, categoryArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSelectCategories.adapter = adapter
            binding.spSelectCategories.selected { position ->
                budgetViewModel.getSubCategoriesBudget(category[position].id.toString())
            }
        }
        budgetViewModel.subCategoriesBudgetEmitter.observe(viewLifecycleOwner) { subCategory ->
            val categoryArrays = subCategory.map { item ->
                item.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, categoryArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSelectSubCategories.adapter = adapter
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) {
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
            loading.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}