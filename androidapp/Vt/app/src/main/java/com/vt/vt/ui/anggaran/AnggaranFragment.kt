package com.vt.vt.ui.anggaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vt.vt.R
import com.vt.vt.databinding.FragmentAnggaranBinding
import com.vt.vt.ui.anggaran.adapter.BudgetAdapter
import com.vt.vt.utils.PickDatesUtils

class AnggaranFragment : Fragment() {

    private var _binding: FragmentAnggaranBinding? = null
    private val binding get() = _binding!!

    private val budgetItemList = mutableListOf<BudgetItem>()
    private lateinit var adapter: BudgetAdapter


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


        with(binding) {
            anggaranAppBar.topAppBar.apply {
                title = "Anggaran"
                setNavigationOnClickListener { findNavController().popBackStack() }
            }
            ivDatePickerAnggaran.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvDateContentAnggaran)
            }
            footerAnggaranForm.btnTambahAnggaran.setOnClickListener {
                findNavController().navigate(R.id.action_anggaranFragment_to_addBudgetFragment)
            }
        }


    }

    data class BudgetItem(val name: String, val amount: Int)

}