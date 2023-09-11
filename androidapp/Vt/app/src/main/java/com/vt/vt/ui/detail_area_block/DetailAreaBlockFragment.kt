package com.vt.vt.ui.detail_area_block

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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.core.data.local.list_animal_cage.AnimalCage
import com.vt.vt.databinding.FragmentDetailAreaBlockBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailAreaBlockFragment : Fragment(), Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private var _binding: FragmentDetailAreaBlockBinding? = null
    private val binding get() = _binding!!

    private val detailAreaBlockViewModel: DetailAreaBlockViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAreaBlockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarDetailAreaBlock.topAppBar.apply {
                title = "Detail Area/Block Name"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener { findNavController().popBackStack() }
                inflateMenu(R.menu.menu_detail_area_block)
                setOnMenuItemClickListener(this@DetailAreaBlockFragment)
            }
            btnEditAreaBlock.setOnClickListener(this@DetailAreaBlockFragment)
        }
        detailAreaBlockViewModel.animalCageItems.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_animal_cage)
        val spinner =
            dialog.findViewById<Spinner>(R.id.spinner_cage_add_animal_cage)
        if (spinner != null) {
            spinnerAddCage(spinner)
        }
        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save_add_animal_cage)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_add_animal_cage)
        dialog.show()
        btnSave?.setOnClickListener {
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun spinnerAddCage(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            spinner.adapter = adapter
        }
    }

    fun setupRecyclerView(animalCage: List<AnimalCage>) {
        val adapter = ListDetailAreaBlockAdapter(animalCage)
        binding.recyclerViewListDetailArea.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_cage_detail_area -> {
                showBottomSheetDialog()
                return true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_area_block -> {
                v.findNavController()
                    .navigate(R.id.action_detailAreaBlockFragment_to_editAreaBlockFragment)
            }
        }
    }

}