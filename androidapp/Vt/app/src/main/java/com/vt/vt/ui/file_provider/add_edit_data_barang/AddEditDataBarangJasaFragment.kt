package com.vt.vt.ui.file_provider.add_edit_data_barang

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vt.vt.R
import com.vt.vt.core.data.source.local.model.SpinnerCategoriesItem
import com.vt.vt.core.data.source.remote.categories.viewmodel.CategoriesViewModel
import com.vt.vt.databinding.FragmentAddEditDataBarangJasaBinding
import com.vt.vt.ui.barang_dan_jasa.DataBarangDanJasaFragment.Companion.IS_UPDATE_DATA
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditDataBarangJasaFragment : Fragment() {

    private var _binding: FragmentAddEditDataBarangJasaBinding? = null
    private val binding get() = _binding!!

    private val dataBarangDanJasaViewModel by viewModels<DataBarangJasaViewModel>()
    private val categoriesViewModel by viewModels<CategoriesViewModel>()

    private var spinnerItems: MutableList<SpinnerCategoriesItem> = mutableListOf()
    private var categoryId: Int = 1

    private var receiveId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditDataBarangJasaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("BARANG JASA ", "IS EDIT ? ${IS_UPDATE_DATA}")
        if (IS_UPDATE_DATA) {
            receiveId = arguments?.getInt("id").toString()
            dataBarangDanJasaViewModel.getProductById(receiveId)
        }
        with(binding) {
            appBarLayout.topAppBar.apply {
                title = "Tambah Barang Jasa"
                setNavigationOnClickListener {
                    view.findNavController().popBackStack()
                }
            }
            btnSimpan.setOnClickListener {
                Toast.makeText(requireContext(), "no action", Toast.LENGTH_SHORT).show()
            }
            ivPhotoDataBarangJasa.setOnClickListener {
                requestPermissionsIfNeeded()
            }
            spinnerProductCategory.selected { position ->
//                categoryId = 0
                categoryId = spinnerItems[position].id
            }
            btnSimpan.setOnClickListener {
                val name = edtNamaBarang.text.toString().trim()
                val description = edtDescription.text.toString().trim()
                val satuan = edtSatuan.text.toString().trim()
                if (name.isNotEmpty() && description.isNotEmpty() && satuan.isNotEmpty()) {
                    if (IS_UPDATE_DATA) {
                        dataBarangDanJasaViewModel.updateBarangJasa(
                            receiveId,
                            categoryId,
                            name,
                            description,
                            satuan
                        )
                    } else {
                        dataBarangDanJasaViewModel.createProduct(
                            categoryId,
                            name,
                            description,
                            satuan
                        )
                    }
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        observerView()
    }

    private fun observerView() {
        dataBarangDanJasaViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            isCreatedProduct.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Log.e("barang error", "error view 500 ${it?.message.toString()}")
                Toast.makeText(requireContext(), it?.status, Toast.LENGTH_SHORT).show()
            }
            getProductEmitter.observe(viewLifecycleOwner) { data ->
                with(binding) {
                    binding.appBarLayout.topAppBar.title = "Edit Barang Jasa"
                    edtNamaBarang.setText(data?.productName)
                    spinnerProductCategory.selected { data?.categoryId }
                    edtSatuan.setText(data?.unitMeasurement)
//                    edtDescription.setText(data?.unitMeasurement)
                }
            }
            isUpdatedEmitter.observe(viewLifecycleOwner) { data ->
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireActivity(), data?.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
            isError().observe(viewLifecycleOwner) {
                Log.e("barang error", "error view")
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        categoriesViewModel.apply {
            getAllCategories()
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            categoriesEmitter.observe(viewLifecycleOwner) { data ->
                for (item in data) {
                    spinnerItems.add(SpinnerCategoriesItem(item.id, item.name))
                }
                spinnerCategoriesAdapter(binding.spinnerProductCategory)
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun spinnerCategoriesAdapter(spinner: Spinner) {
        val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.e("LOG_TAG", "${it.key} = ${it.value}")
            }
        }

    private fun hasReadStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionsIfNeeded() {
        val readStoragePermissionGranted = hasReadStoragePermission()
        val cameraPermissionGranted = hasCameraPermission()

        if (!readStoragePermissionGranted || !cameraPermissionGranted) {
            val permissions = mutableListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

            requestPermissions.launch(permissions.toTypedArray())
        } else {
            showBottomSheetDialog()
        }
    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(requireActivity())
        dialog.setContentView(R.layout.bottom_sheet_open_camera_gallery)
        val btnCamera = dialog.findViewById<RelativeLayout>(R.id.rl_camera)
        val btnGallery = dialog.findViewById<RelativeLayout>(R.id.rl_gallery)
        dialog.show()
        btnCamera?.setOnClickListener {
            //startCamera()
            dialog.dismiss()
        }
        btnGallery?.setOnClickListener {
            //startGallery()
            dialog.dismiss()
        }
    }

    private fun showLoading(state: Boolean) {
        with(binding) {
            btnSimpan.isEnabled = !state
            btnBatal.isEnabled = !state

            btnSimpan.setBackgroundColor(
                if (state) Color.GRAY
                else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
            )

            loading.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        }
    }
}