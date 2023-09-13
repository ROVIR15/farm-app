package com.vt.vt.ui.bottom_navigation.keuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.keuangan.Pengeluaran
import com.vt.vt.databinding.FragmentKeuanganBinding
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KeuanganFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentKeuanganBinding? = null
    private val binding get() = _binding!!

    private val keuanganViewModel: KeuanganViewModel by viewModels()
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
                title = "Finance Summary"
                inflateMenu(R.menu.menu_keuangan)
                setOnMenuItemClickListener(this@KeuanganFragment)
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvDatePick)
            }
        }
        keuanganViewModel.pengeluaranItem.observe(viewLifecycleOwner) {
            listPengeluaran(it)
        }
    }

    private fun listPengeluaran(data: List<Pengeluaran>) {
        val adapter = KeuanganAdapter(data)
        with(binding) {
            rvAnggaranRealisasi.adapter = adapter
            rvAnggaranRealisasi.layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
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

    private fun addBudget() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_budget)
        val spSelectCategories = dialog.findViewById<Spinner>(R.id.sp_select_categories)
        if (spSelectCategories != null) adapterSpinner(spSelectCategories)
        val edtBudgetAdded = dialog.findViewById<TextInputEditText>(R.id.added_budget)
        val btnAdd = dialog.findViewById<AppCompatButton>(R.id.btn_save_add_budget)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_add_budget)
        dialog.show()

        btnAdd?.setOnClickListener {
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_folder -> {
                addBudget()
                return true
            }
        }
        return false
    }
}