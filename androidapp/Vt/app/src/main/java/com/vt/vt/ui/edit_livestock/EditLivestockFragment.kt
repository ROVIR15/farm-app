package com.vt.vt.ui.edit_livestock

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.permission.PermissionAlertDialog.showPermissionDeniedDialog
import com.vt.vt.core.data.permission.PermissionManager
import com.vt.vt.core.data.source.remote.livestock.dto.LivestockResponseItem
import com.vt.vt.core.data.source.remote.sleds.dto.SledsResponseItem
import com.vt.vt.databinding.FragmentEditLivestockBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.common.SnapSheetFragment
import com.vt.vt.ui.common.SnapSheetListener
import com.vt.vt.ui.detail_area_block.DetailAreaBlockViewModel
import com.vt.vt.ui.file_provider.addlivestock.AddLivestockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.fileToMultipart
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class EditLivestockFragment : Fragment(), View.OnClickListener, SnapSheetListener {

    private var _binding: FragmentEditLivestockBinding? = null
    private val binding get() = _binding!!
    private val livestockViewModel by viewModels<LivestockViewModel>()
    private val addLivestockViewModel by viewModels<AddLivestockViewModel>()
    private val editLivestockViewModel by viewModels<EditLivestockViewModel>()
    private val detailAreaBlockViewModel by viewModels<DetailAreaBlockViewModel>()

    private var receiveId: String = ""
    private var sledId: Int = 0
    private var blockAreaId: Int = 0
    private var parentMaleId: Int? = null
    private var parentFemaleId: Int? = null
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
        detailAreaBlockViewModel.getSleds()

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
            ivProfileLivestock.setOnClickListener(this@EditLivestockFragment)
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
                if (name.isNotEmpty() && description.isNotEmpty() && nation.isNotEmpty() && createdAt.isNotEmpty()) {
                    if (gender != 0) {
                        editLivestockViewModel.updateLivestockById(
                            id = receiveId,
                            name = name,
                            gender = gender,
                            nation = nation,
                            description = description,
                            birthDate = createdAt,
                            parentFemaleId = parentFemaleId,
                            parentMaleId = parentMaleId,
                            file = "[in development]"
                        )
                        livestockViewModel.livestockMoveSled(
                            receiveId.toInt(), sledId, blockAreaId
                        )
                    }
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnCancelEditLivestock.setOnClickListener {
                view.findNavController().popBackStack()
            }
            adapterSpinnerStaticGender(binding.spinnerGenderUmum)
        }
        observerView()
    }

    private val requestResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            var permissionGranted = true
            permission.entries.forEach {
                if (it.key in PermissionManager.REQUIRED_PERMISSION && !it.value) {
                    permissionGranted = false
                }
            }
            if (!permissionGranted) {
                showPermissionDeniedDialog(requireActivity())
            }
        }

    private fun observerView() {
        editLivestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            getLivestockById.observe(viewLifecycleOwner) { livestock ->
                with(binding) {
                    livestock?.let { data ->
                        appBarLayout.topAppBar.title = "Edit ${data.name}"
                        title.text = data.name
                        edtNamaAddLivestock.setText(data.name)
                        spinnerGenderUmum.setSelection(data.gender)
                        edtDescription.setText(data.description)
                        edtBangsa.setText(data.bangsa)
                        tvDateLivestock.text = data.birthDate
                    }
                }
                detailAreaBlockViewModel.sledItems.observe(viewLifecycleOwner) { sleds ->
                    val namesArray = sleds.map { data ->
                        data.name
                    }.toTypedArray()
                    adapterSpinner(binding.spinnerKandangUmum, namesArray)
                    val currentSledId = livestock?.sledId
                    val position = findPositionSledById(sleds, currentSledId)
                    if (currentSledId != null) {
                        if (position != -1) {
                            binding.spinnerKandangUmum.setSelection(position)
                        }
                    }
                    binding.spinnerKandangUmum.selected { pos ->
                        sledId = sleds[pos].id
                        blockAreaId = sleds[pos].blockAreaId
                        binding.edtArea.setText(sleds[pos].blockAreaName)
                    }
                }
                livestockViewModel.livestocksMaleEmitter.observe(viewLifecycleOwner) { livestockMale ->
                    val nameArray = livestockMale.map {
                        it.name
                    }.toTypedArray()
                    adapterSpinner(binding.spinnerPilihLivestockJantan, nameArray)
                    val desiredId = livestock?.descendant?.parentMaleId
                    val position = findPositionById(livestockMale, desiredId)
                    if (desiredId != null) {
                        if (position != -1) {
                            binding.spinnerPilihLivestockJantan.setSelection(position)
                        }
                    }
                    binding.spinnerPilihLivestockJantan.selected { pos ->
                        parentMaleId = livestockMale[pos].id
                    }
                }
                livestockViewModel.livestocksFemaleEmitter.observe(viewLifecycleOwner) { livestockFemale ->
                    val livestockFemaleArray = livestockFemale.map {
                        it.name
                    }.toTypedArray()
                    adapterSpinner(binding.spinnerPilihLivestockBetina, livestockFemaleArray)
                    val desiredId = livestock?.descendant?.parentFemaleId
                    val position = findPositionById(livestockFemale, desiredId)
                    if (desiredId != null) {
                        if (position != -1) {
                            binding.spinnerPilihLivestockBetina.setSelection(position)
                        }
                    }
                    binding.spinnerPilihLivestockBetina.selected { pos ->
                        parentFemaleId = livestockFemale[pos].id
                    }
                }
            }
            isUpdateLivestock.observe(viewLifecycleOwner) { livestock ->
                Log.d(TAG, "observerView: ${livestock?.message}")
                livestockViewModel.livestockMoveSledEmitter.observe(viewLifecycleOwner) { livestockMoveSled ->
                    Toast.makeText(
                        requireContext(), livestock?.message.toString(), Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "observerView: ${livestockMoveSled?.message}")
                    view?.findNavController()?.popBackStack()
                }
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        addLivestockViewModel.observeLoading().observe(viewLifecycleOwner) {
            showLoading(it)
        }
        addLivestockViewModel.postImageLivestock.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), "${it.message}", Toast.LENGTH_SHORT).show()
        }
        addLivestockViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
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
        livestockViewModel.isError().observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun bitmapPhotos(photo: Bitmap?) {
        Log.d(TAG, "BITMAP PHOTO : $photo")
        if (photo != null) {
            with(binding) {
                ivProfileLivestock.setImageBitmap(photo)
                ivProfileLivestock.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_image_24)
                ivProfileLivestock.clipToOutline = true
                iconFilePhoto.visibility = View.GONE
                tvUploadFilePhoto.visibility = View.GONE
            }
        } else {
            Log.e(TAG, "BITMAP PHOTO : $photo")
        }
    }

    override fun uriFile(photo: Uri?) {
        Log.d(TAG, "URI PHOTO : $photo")
        with(binding) {
            ivProfileLivestock.apply {
                setImageURI(photo)
                background = ContextCompat.getDrawable(
                    requireContext(), R.drawable.ic_outline_image_24
                )
                clipToOutline = true
            }
            iconFilePhoto.visibility = View.GONE
            tvUploadFilePhoto.visibility = View.GONE
        }
    }

    override fun getFile(file: File?) {
        if (file != null) {
            Log.d(TAG, "get File: $file")
            lifecycleScope.launch {
                try {
                    val myFile = withContext(Dispatchers.Main) {
                        fileToMultipart(TAG, file)
                    }
                    addLivestockViewModel.postImageLivestock(myFile!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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

    private fun findPositionById(itemList: List<LivestockResponseItem>, desiredId: Int?): Int {
        for ((index, item) in itemList.withIndex()) {
            if (item.id == desiredId) {
                return index
            }
        }
        return -1
    }

    private fun findPositionSledById(itemList: List<SledsResponseItem>, desiredId: Int?): Int {
        for ((index, item) in itemList.withIndex()) {
            if (item.id == desiredId) {
                return index
            }
        }
        return -1
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_profile_livestock -> {
                if (PermissionManager(requireContext()).hasPermission()) {
                    val snapShotDialog = SnapSheetFragment()
                    snapShotDialog.show(
                        childFragmentManager, snapShotDialog::class.java.simpleName
                    )
                } else {
                    requestResultLauncher.launch(PermissionManager.REQUIRED_PERMISSION)
                }
            }
        }
    }

    companion object {
        private val TAG = EditLivestockFragment::class.java.simpleName
    }

}