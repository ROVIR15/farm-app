package com.vt.vt.ui.income.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.vt.vt.databinding.FragmentAddIncomeBinding
import com.vt.vt.utils.PickDatesUtils

class AddIncomeFragment : Fragment() {

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding get() = _binding!!

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
        with(binding) {
            appBar.topAppBar.title = "Pendapatan"
            appBar.topAppBar.setNavigationOnClickListener {
                view.findNavController().popBackStack()
            }
            containerDate.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), incomeCreatedAt, true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}