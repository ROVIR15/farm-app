package com.vt.vt.ui.daftar_perkawinan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.dummy.list_animal_matings.AnimalMatings
import com.vt.vt.databinding.FragmentListAnimalMatingsBinding
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListAnimalMatingsFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private var _binding: FragmentListAnimalMatingsBinding? = null
    private val binding get() = _binding!!

    private val listAnimalMatingsViewModel: ListAnimalMatingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListAnimalMatingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarListAnimalMatings.topAppBar.apply {
                title = "Daftar Perkawinan"
                setNavigationOnClickListener { findNavController().popBackStack() }
                inflateMenu(R.menu.menu_goods_and_service)
                setOnMenuItemClickListener(this@ListAnimalMatingsFragment)
            }
        }
        listAnimalMatingsViewModel.animalMatings.observe(viewLifecycleOwner) {
            setupRecyclerView(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView(animalMatings: List<AnimalMatings>) {
        val adapter = ListAnimalMatingsAdapter(animalMatings)
        binding.recyclerViewListAnimalMatings.apply {
            this.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_goods_and_services -> {
                showBottomSheetDialog()
                return true
            }
        }
        return false
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_animal_mating)

        val showCalendar =
            dialog.findViewById<AppCompatImageView>(R.id.iv_date_picker_animal_matings)
        val setDate = dialog.findViewById<TextView>(R.id.tv_add_animal_mating_date)
        showCalendar?.setOnClickListener {
            PickDatesUtils.setupDatePicker(requireActivity(), setDate!!)
        }

        val spinnerAddCage = dialog.findViewById<Spinner>(R.id.spinner_cage_add_animal_mating)
        if (spinnerAddCage != null) spinnerAdapter(spinnerAddCage)

        val spinnerMaleAnimalMating =
            dialog.findViewById<Spinner>(R.id.spinner_choose_male_add_animal_mating)
        if (spinnerMaleAnimalMating != null) spinnerAdapter(spinnerMaleAnimalMating)

        val spinnerFemaleAnimalMating =
            dialog.findViewById<Spinner>(R.id.spinner_choose_female_add_animal_mating)
        if (spinnerFemaleAnimalMating != null) spinnerAdapter(spinnerFemaleAnimalMating)

        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save_add_animal_mating)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_add_animal_mating)

        dialog.show()
        btnSave?.setOnClickListener {
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun spinnerAdapter(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(
                R.layout.item_spinner
            )
            spinner.adapter = adapter
        }
    }

}