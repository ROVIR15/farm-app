package com.vt.vt.ui.rekam_perkawinan

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
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
import com.vt.vt.ui.rekam_perkawinan.dialog.ChangeBreedingStatusDialogFragment
import com.vt.vt.ui.rekam_perkawinan.dialog.EditBreedingDialogFragment
import com.vt.vt.utils.formatDateBreeding
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedingRecordFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentBreedingRecordBinding? = null
    private val binding get() = _binding!!

    private val recordBreedingViewModel by viewModels<RecordBreedingViewModel>()
    private val sledViewModel by viewModels<DetailAreaBlockViewModel>()

    private var receiveId: Int? = null
    private var originalToggleState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedingRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveId = arguments?.getInt("id")
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
            swithMaterialRecordMating.isChecked = originalToggleState
            swithMaterialRecordMating.setOnClickListener {
                if (!originalToggleState) {
                    showDialogAnimalPregnant()
                } else {
                    showChangesStatusBottomSheet()
                }
            }
        }
        observerView()
        setupViewPager()
    }

    @SuppressLint("SetTextI18n")
    private fun observerView() {
        recordBreedingViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            breedingByIdEmitter.observe(viewLifecycleOwner) { breeding ->
                val createdAt = formatDateBreeding(breeding.createdAt, "dd-MMMM-yyyy")
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
                binding.tvRecordMatingDate.text = createdAt
                binding.tvLivestockMale.text = breeding.livestockMale?.name.toString()
                binding.tvLivestockFemale.text = breeding.livestockFemale?.name.toString()
                binding.swithMaterialRecordMating.isChecked = breeding.pregnancy.isActive
                originalToggleState = breeding.pregnancy.isActive
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setupViewPager() {
        val mBundle = Bundle()
        receiveId?.let { mBundle.putInt("breedingId", it) }
        val adapter = ViewPagerRecordAnimalMatingAdapter(this, mBundle)
        with(binding) {
            viewPagerRecordMating.adapter = adapter
            TabLayoutMediator(tabLayoutRecordMating, viewPagerRecordMating) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    private fun showDialogAnimalPregnant() {
        val bottomSheetAnimalPregnant = AddAnimalPregnantFragment()
        val mBundle = Bundle()
        receiveId?.let { mBundle.putInt("breedingId", it) }
        bottomSheetAnimalPregnant.arguments = mBundle
        bottomSheetAnimalPregnant.show(
            childFragmentManager, AddAnimalPregnantFragment::class.java.simpleName
        )
    }

    private fun showChangesStatusBottomSheet() {
        val changeBreedingStatusDialog = ChangeBreedingStatusDialogFragment()
        val mBundle = Bundle()
        receiveId?.let { mBundle.putInt("breedingId", it) }
        changeBreedingStatusDialog.arguments = mBundle
        changeBreedingStatusDialog.show(
            childFragmentManager, ChangeBreedingStatusDialogFragment::class.java.simpleName
        )
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_edit_breeding -> {
                val mBundle = Bundle()
                mBundle.putInt("breedingId", receiveId!!)
                val editBreedingDialogFragment = EditBreedingDialogFragment()
                editBreedingDialogFragment.arguments = mBundle
                editBreedingDialogFragment.show(
                    childFragmentManager, editBreedingDialogFragment::class.java.simpleName
                )
                return true
            }

            R.id.action_menu_add_history -> {
                val addHistoryBreedingDialogFragment = AddHistoryBreedingDialogFragment()
                val mBundle = Bundle()
                receiveId?.let { mBundle.putInt("breedingId", it) }
                addHistoryBreedingDialogFragment.arguments = mBundle
                addHistoryBreedingDialogFragment.show(
                    childFragmentManager, AddHistoryBreedingDialogFragment::class.java.simpleName
                )
                return true
            }

            R.id.action_menu_add_animal_child -> {
                val addBreedingDialogFragment = AddBreedingDialogFragment()
                val mBundle = Bundle()
                receiveId?.let { mBundle.putInt("breedingId", it) }
                addBreedingDialogFragment.arguments = mBundle
                addBreedingDialogFragment.show(
                    childFragmentManager, AddBreedingDialogFragment::class.java.simpleName
                )
                return true
            }
        }
        return false
    }

    private fun showLoading(state: Boolean) {
        binding.loading.progressBar.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    internal var onBottomSheetDialogListener =
        object : AddAnimalPregnantFragment.OnBottomSheetDialogListener {
            override fun onBottomSheetClose() {
                recordBreedingViewModel.getBreedingById(receiveId.toString())
            }
        }

    internal var changeStatusBreedingListener =
        object : ChangeBreedingStatusDialogFragment.ChangeBreedingStatusListener {
            override fun onBottomSheetClose() {
                recordBreedingViewModel.getBreedingById(receiveId.toString())
            }
        }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1_animal_mating_history, R.string.tab_text_2_animal_mating_history
        )
    }
}
