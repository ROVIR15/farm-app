package com.vt.vt.ui.anggaran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.keuangan.Pengeluaran
import com.vt.vt.databinding.FragmentAnggaranBinding
import com.vt.vt.ui.anggaran.adapter.BudgetAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnggaranFragment : Fragment() {

    private var _binding: FragmentAnggaranBinding? = null
    private val binding get() = _binding!!
    private val anggaranViewModel: AnggaranViewModel by viewModels()
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
            footerAnggaranForm.btnTambahAnggaran.setOnClickListener {
                findNavController().navigate(R.id.action_anggaranFragment_to_addPengeluaranFragment)
            }
        }
        anggaranViewModel.pengeluaranItem.observe(viewLifecycleOwner) {
            listPengeluaran(it)
        }
    }

    private fun listPengeluaran(data: List<Pengeluaran>) {
        val adapter = BudgetAdapter(data)
        with(binding) {
            rvPengeluaran.apply {
                this.adapter = adapter
                this.layoutManager = LinearLayoutManager(
                    requireActivity(), LinearLayoutManager.VERTICAL, false
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}