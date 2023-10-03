package com.vt.vt.ui.rekam_perkawinan

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.vt.vt.R
import com.vt.vt.databinding.FragmentBreedingRecordBinding
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.rekam_perkawinan.dialog.AddAnimalPregnantFragment
import com.vt.vt.ui.rekam_perkawinan.dialog.AddBreedingDialogFragment
import com.vt.vt.ui.rekam_perkawinan.dialog.AddHistoryBreedingDialogFragment
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedingRecordFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentBreedingRecordBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    private val sledViewModel by viewModels<DetailAreaBlockViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedingRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receiveId = arguments?.getInt("id")
        recordBreedingViewModel.getBreedingById(receiveId.toString())

        sledViewModel.getSleds()
        with(binding) {
            toolbar3.apply {
                title = "Rekam Perkawinan"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
                setOnMenuItemClickListener(this@BreedingRecordFragment)
            }
//            adapterSpinner(spinnerStallRecordMating)
            adapterSpinner(spinnerChooseMaleLivestockRecordMating)
            adapterSpinner(spinnerChooseFemaleLivestockRecordMating)
            ivDatePickerRecordAnimalMeting.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvRecordMatingDate)
            }
            swithMaterialRecordMating.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showDialogAnimalPregnant()
                } else {
                    // when toggle is true then turn set false.
                    // set date is null
                    // HERE UR CODE
                }
            }
        }
        observerView()
        setupViewPager()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        recordBreedingViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {}
            breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
                val desiredSledId = breeding.sledId?.toInt()
                sledViewModel.sledItems.observe(viewLifecycleOwner) { sled ->
                    with(binding) {
                        val nameArray = sled.map {
                            it.name
                        }.toTypedArray()
                        val adapter =
                            ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArray)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinnerStallRecordMating.adapter = adapter
                        val desiredPosition = sled.indexOfFirst {
                            it.id == desiredSledId
                        }
                        if (desiredPosition != -1) {
                            spinnerStallRecordMating.selected {
                                edtArea.setText(sled[desiredPosition].blockAreaName)
                            }
                        } else {
                            edtArea.setText("No Data Area Selected ")
                        }
                    }
                }
                binding.swithMaterialRecordMating.isChecked = breeding.isActive
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setupViewPager() {
        val bundle = Bundle()
        val adapter = ViewPagerRecordAnimalMatingAdapter(this, bundle)
        with(binding) {
            viewPagerRecordMating.adapter = adapter
            TabLayoutMediator(tabLayoutRecordMating, viewPagerRecordMating) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun adapterSpinner(binding: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.product_category_array, R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun showDialogAnimalPregnant() {
        val bottomSheetAnimalPregnant = AddAnimalPregnantFragment()
        bottomSheetAnimalPregnant.show(
            childFragmentManager, AddAnimalPregnantFragment::class.java.simpleName
        )
    }

    private fun adapterSpinnerRecordMating(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.product_category_array, R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            spinner.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_add_history -> {
                val addHistoryBreedingDialogFragment = AddHistoryBreedingDialogFragment()
                addHistoryBreedingDialogFragment.show(
                    childFragmentManager, AddHistoryBreedingDialogFragment::class.java.simpleName
                )
                return true
            }

            R.id.action_menu_add_animal_child -> {
                val addBreedingDialogFragment = AddBreedingDialogFragment()
                addBreedingDialogFragment.show(
                    childFragmentManager, AddBreedingDialogFragment::class.java.simpleName
                )
                return true
            }
        }
        return false
    }

    internal var onBottomSheetDialogListener =
        object : AddAnimalPregnantFragment.OnBottomSheetDialogListener {
            override fun onDateSelected(text: String?) {
                if (!text.isNullOrEmpty()) {
                    Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
                } else {
                    binding.swithMaterialRecordMating.isChecked = false
                }
            }
        }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1_animal_mating_history, R.string.tab_text_2_animal_mating_history
        )
    }
}
