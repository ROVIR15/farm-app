package com.vt.vt.ui.edit_livestock

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.databinding.FragmentEditLivestockBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLivestockFragment : Fragment() {

    private var _binding: FragmentEditLivestockBinding? = null
    private val binding get() = _binding!!
    private val livestockViewModel by viewModels<LivestockViewModel>()
    private val editLivestockViewModel by viewModels<EditLivestockViewModel>()
    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()

    private var receiveId: String = ""
    private var parentMaleId: Int = 0
    private var parentFemaleId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditLivestockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveId = arguments?.getInt("id").toString()
        if (receiveId.isNotEmpty()) {
            editLivestockViewModel.getLivestockById(receiveId)
        }
        livestockViewModel.getLivestocksMale()
        livestockViewModel.getLivestocksFemale()

        with(binding) {
            appBarLayout.topAppBar.also { toolbar ->
                toolbar.title = "Edit Profile Livestock"
                toolbar.setNavigationOnClickListener {
                    it?.findNavController()?.popBackStack()
                }
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvDateLivestock)
            }
            ivProfileLivestock.setOnClickListener {
                showBottomSheetDialog()
            }
            var gender = 0
            spinnerGenderUmum.selected { position ->
                gender = position
            }
            btnSaveEditLivestock.setOnClickListener {
                val name = edtNamaAddLivestock.text.toString().trim()
                val nation = edtBangsa.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                val birthDate = tvDateLivestock.text.toString().trim()
                val createdAt = formatterDateFromCalendar(birthDate)
                if (name.isNotEmpty() && description.isNotEmpty() && nation.isNotEmpty() && createdAt.isNotEmpty() && parentMaleId != 0 && parentFemaleId != 0
                ) {
                    if (gender != 0) {
                        editLivestockViewModel.updateLivestockById(
                            receiveId,
                            name,
                            gender,
                            nation,
                            description,
                            createdAt,
                            parentMaleId,
                            parentFemaleId
                        )
                    }
                }
            }
            adapterSpinnerStaticGender(binding.spinnerGenderUmum)
        }
        observerView()
    }

    private fun observerView() {
        editLivestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                detailAreaBlockViewModel.getSleds()
                detailAreaBlockViewModel.sledItems.observe(viewLifecycleOwner) { sleds ->
                    val namesArray = sleds.map { data ->
                        data.name
                    }.toTypedArray()
                    adapterSpinner(binding.spinnerKandangUmum, namesArray)
                    binding.spinnerKandangUmum.selected { position ->
                        binding.edtArea.setText(sleds[position].blockAreaName)
                    }
                }
                with(binding) {
                    livestock?.let { data ->
                        appBarLayout.topAppBar.title = "Edit ${data.name}"
                        title.text = data.name
                        edtNamaAddLivestock.setText(data.name)
                        spinnerGenderUmum.setSelection(data.gender)
                        edtDescription.setText(data.description)
                        edtBangsa.setText(data.bangsa)
                        tvDateLivestock.setText(data.birthDate)
                    }
                }
            }
            isUpdateLivestock.observe(viewLifecycleOwner) { livestock ->
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireContext(), livestock?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        livestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        livestockViewModel.livestocksMaleEmitter.observe(viewLifecycleOwner) { livestockMale ->
            val livestockMales = livestockMale.map {
                it.name
            }.toTypedArray()
            adapterSpinner(binding.spinnerPilihLivestockJantan, livestockMales)
        }
        livestockViewModel.livestocksMaleEmitter.observe(viewLifecycleOwner) { livestockMale ->
            val livestockMales = livestockMale.map {
                it.name
            }.toTypedArray()
            adapterSpinner(binding.spinnerPilihLivestockJantan, livestockMales)
            binding.spinnerPilihLivestockJantan.selected { position ->
                parentMaleId = livestockMale[position].id
            }
        }
        livestockViewModel.livestocksFemaleEmitter.observe(viewLifecycleOwner) { livestockFemale ->
            val livestockFemales = livestockFemale.map {
                it.name
            }.toTypedArray()
            adapterSpinner(binding.spinnerPilihLivestockBetina, livestockFemales)
            binding.spinnerPilihLivestockBetina.selected { position ->
                parentFemaleId = livestockFemale[position].id
            }
        }
        livestockViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun adapterSpinner(binding: Spinner, textArray: Array<String>) {
        ArrayAdapter(
            requireActivity(),
            R.layout.item_spinner,
            textArray,
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun adapterSpinnerStaticGender(binding: Spinner) {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.gender_animal,
            R.layout.item_spinner,
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner)
            binding.adapter = adapter
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(R.layout.bottom_sheet_open_camera_gallery)
        val btnCamera = dialog.findViewById<RelativeLayout>(R.id.rl_camera)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_gallery)
        dialog.show()
        btnCamera?.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
    }

    fun startCamera() {
        Toast.makeText(requireContext(), "belum ada action ke camera", Toast.LENGTH_SHORT).show()
    }

    fun startGallery() {
        Toast.makeText(requireContext(), "belum ada action ambil gambar", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSaveEditLivestock.isEnabled = !state
            btnCancelEditLivestock.isEnabled = !state

            btnSaveEditLivestock.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }
}