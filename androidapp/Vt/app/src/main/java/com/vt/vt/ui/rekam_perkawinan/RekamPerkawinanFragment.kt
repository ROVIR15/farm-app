package com.vt.vt.ui.rekam_perkawinan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.vt.vt.R
import com.vt.vt.databinding.FragmentRekamPerkawinanBinding
import com.vt.vt.utils.PickDatesUtils

class RekamPerkawinanFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private var _binding: FragmentRekamPerkawinanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRekamPerkawinanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            appBarRecordMating.topAppBar.apply {
                title = "Rekam Perkawinan"
                setNavigationIcon(R.drawable.baseline_arrow_back_24)
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
                post {
                    inflateMenu(R.menu.menu_rekam_perkawinan)
                    setOnMenuItemClickListener(this@RekamPerkawinanFragment)
                }
            }
            adapterSpinner(spinnerStallRecordMating)
            adapterSpinner(spinnerChooseMaleLivestockRecordMating)
            adapterSpinner(spinnerChooseFemaleLivestockRecordMating)
            ivDatePickerRecordAnimalMeting.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvRecordMatingDate)
            }
            swithMaterialRecordMating.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showBottomSheetDialog()
                } else {
                    // when toggle is true then turn set false.
                    // set date is null
                    // HERE UR CODE
                }
            }
        }
        setupViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_date_animal_pregnant)
        val ivPickDate =
            dialog.findViewById<AppCompatImageView>(R.id.iv_date_picker_animal_pregnant)
        val tvDateAnimalPregnant = dialog.findViewById<TextView>(R.id.tv_animal_pregnant_date)
        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save_animal_pregnant)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_animal_pregnant)
        dialog.setCancelable(false)

        dialog.show()
        ivPickDate?.setOnClickListener {
            PickDatesUtils.setupDatePicker(requireActivity(), tvDateAnimalPregnant!!)
        }
        btnSave?.setOnClickListener {
            binding.swithMaterialRecordMating.isChecked =
                !tvDateAnimalPregnant?.text.isNullOrEmpty()
            //startCamera()
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            binding.swithMaterialRecordMating.isChecked = false
            dialog.dismiss()
        }
    }

    private fun showBottomSheetDialogAddHistoryAnimalMating() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_history_animal_mating)
        val ivPickDate =
            dialog.findViewById<AppCompatImageView>(R.id.iv_date_picker_history_animal_mating)
        val tvDateAnimalPregnant = dialog.findViewById<TextView>(R.id.tv_history_animal_mating_date)
        val edtDescriptionHistoryAnimalMating =
            dialog.findViewById<AppCompatEditText>(R.id.edt_description_history_animal_mating)
        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save_animal_mating)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_animal_mating)
        dialog.show()
        ivPickDate?.setOnClickListener {
            PickDatesUtils.setupDatePicker(requireActivity(), tvDateAnimalPregnant!!)
        }
        btnSave?.setOnClickListener {
            //startCamera()
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {

            dialog.dismiss()
        }
    }

    private fun showBottomSheetDialogAddAnimalChild() {
        val dialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_add_animal_child)

        val nameAnimal = dialog.findViewById<AppCompatEditText>(R.id.edt_name_animal_child)
        val ivPickDate =
            dialog.findViewById<AppCompatImageView>(R.id.iv_date_picker_animal_child)
        val tvDateAnimalPregnant = dialog.findViewById<TextView>(R.id.tv_animal_child_date)

        val spinnerGender = dialog.findViewById<Spinner>(R.id.spinner_gender_animal_child)
        if (spinnerGender != null) adapterSpinnerRecordMating(spinnerGender)

        val spinnerBangsa = dialog.findViewById<Spinner>(R.id.spinner_bangsa_animal_child)
        if (spinnerBangsa != null) adapterSpinnerRecordMating(spinnerBangsa)

        val spinnerCageAnimal = dialog.findViewById<Spinner>(R.id.spinner_cage_animal_child)
        if (spinnerCageAnimal != null) adapterSpinnerRecordMating(spinnerCageAnimal)

        val edtAreaAnimal = dialog.findViewById<AppCompatEditText>(R.id.edt_area_animal_child)
        val edtDescriptionHistoryAnimalMating =
            dialog.findViewById<AppCompatEditText>(R.id.edt_description_history_animal_mating)

        val btnSave = dialog.findViewById<AppCompatButton>(R.id.btn_save_animal_mating)
        val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btn_cancel_animal_mating)
        dialog.show()
        ivPickDate?.setOnClickListener {
            PickDatesUtils.setupDatePicker(requireActivity(), tvDateAnimalPregnant!!)
        }
        btnSave?.setOnClickListener {
            //startCamera()
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {

            dialog.dismiss()
        }
    }

    private fun adapterSpinnerRecordMating(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.product_category_array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            spinner.adapter = adapter
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_add_history -> {
                showBottomSheetDialogAddHistoryAnimalMating()
                return true
            }

            R.id.action_menu_add_animal_child -> {
                showBottomSheetDialogAddAnimalChild()
                return true
            }
        }
        return false
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1_animal_mating_history,
            R.string.tab_text_2_animal_mating_history
        )
    }


}
