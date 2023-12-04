package com.vt.vt.ui.pengeluaran.add_pengeluaran

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAddPengeluaranBinding
import com.vt.vt.ui.barang_dan_jasa.ListBarangDanJasaViewModel
import com.vt.vt.ui.bottom_navigation.keuangan.BudgetViewModel
import com.vt.vt.ui.pengeluaran.ExpenditureViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal

@AndroidEntryPoint
class AddPengeluaranFragment : Fragment() {

    private var _binding: FragmentAddPengeluaranBinding? = null
    private val binding get() = _binding!!

    private val expenditureViewModel by viewModels<ExpenditureViewModel>()
    private val budgetViewModel by viewModels<BudgetViewModel>()
    private val listProducts by viewModels<ListBarangDanJasaViewModel>()

    private var budgetCategoryId: Int = 0
    private var budgetSubCategoryId: Int = 0
    private var skuId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPengeluaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        budgetViewModel.getCategoriesBudget()
        listProducts.getAllProducts()
        with(binding) {
            appBarAddAnggaran.topAppBar.apply {
                title = "Tambah Pengeluaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            containerDate.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvPengeluaranCreatedAt)
            }
            btnSave.setOnClickListener {
                val amount = expenditureValue.text.toString().trim()
                val expenditure = expenditureValue.getNumericValueBigDecimal()
                val limit = BigDecimal("100000000")
                val remarks = edtDescription.text.toString().trim()
                val formatter = tvPengeluaranCreatedAt.text.toString()
                val dateSelected = formatterDateFromCalendar(formatter)
                if (dateSelected.isNotEmpty() && amount.isNotEmpty())
                    if (expenditure <= limit) {
                        expenditureViewModel.addExpenditure(
                            budgetCategoryId,
                            budgetSubCategoryId,
                            expenditure,
                            skuId,
                            remarks,
                            dateSelected
                        )
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Maksimal Pengeluaran 100 Juta",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                else {
                    Toast.makeText(
                        requireActivity(),
                        "Lengkapi Tanggal dan Pengeluaran",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            btnCancel.setOnClickListener { findNavController().popBackStack() }
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
        budgetViewModel.categoriesBudgetEmitter.observe(viewLifecycleOwner) { category ->
            val categoryArrays = category.map { item ->
                item.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, categoryArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spCategoryAnggaran.adapter = adapter
            val receiveCategoryId = arguments?.getInt("categoryId")
            val `get-categoryId` = category.map {
                it.id
            }
            val desiredPosition = `get-categoryId`.indexOfFirst {
                receiveCategoryId == it
            }
            binding.spCategoryAnggaran.setSelection(desiredPosition)
            binding.spCategoryAnggaran.selected { position ->
                budgetViewModel.getSubCategoriesBudget(category[position].id.toString())
                budgetCategoryId = category[position].id
            }
        }
        budgetViewModel.subCategoriesBudgetEmitter.observe(viewLifecycleOwner) { subCategory ->
            val categoryArrays = subCategory.map { item ->
                item.name
            }.toTypedArray()
            val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, categoryArrays)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSubCategoryAnggaran.adapter = adapter
            binding.spSubCategoryAnggaran.selected { position ->
                budgetSubCategoryId = subCategory[position].id
            }
        }
        listProducts.productsEmitter.observe(viewLifecycleOwner) { listProducts ->
            val productsArrays = listProducts.map { item ->
                item.productName
            }.toTypedArray()
            val prompt = "Select a products"
            val productsArrayWithPrompt = arrayOf(prompt) + productsArrays
            val adapter =
                ArrayAdapter(requireActivity(), R.layout.item_spinner, productsArrayWithPrompt)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spProducts.adapter = adapter
            binding.spProducts.selected { position ->
                if (position == 0) {
                    skuId = null
                } else {
                    skuId = listProducts[position - 1].skuId
                }
            }
        }
        expenditureViewModel.addExpenditureEmitter.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
        expenditureViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
        budgetViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireActivity(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
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

}