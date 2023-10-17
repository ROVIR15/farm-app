package com.vt.vt.ui.pemberian_ternak.vitamin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.databinding.FragmentVitaminBinding
import com.vt.vt.ui.barang_dan_jasa.ListBarangDanJasaViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import com.vt.vt.ui.pemberian_ternak.PemberianTernakViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VitaminFragment : Fragment() {
    private var _binding: FragmentVitaminBinding? = null
    private val binding get() = _binding!!

    private val listBarangDanJasaViewModel by viewModels<ListBarangDanJasaViewModel>()

    private val pemberianTernakViewModel by viewModels<PemberianTernakViewModel>()
    private val dataAreaBlockViewModel by viewModels<DataAreaViewModel>()

    private var skuId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVitaminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("blockId")
        dataAreaBlockViewModel.getBlockAreaInfoById(receiveBlockId.toString())
        val receiveVitaminId = arguments?.getInt("feedCategoryVitaminId")

        with(binding) {
            appBarVitamin.topAppBar.apply {
                title = "Vitamin"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvShowDate)
            }
            btnSimpanVitamin.setOnClickListener {
                val score = editTextRekamPemberianVitamin.text.toString().trim()
                val currentDate = formatterDateFromCalendar(tvShowDate.text.toString().trim())

                if (score.isNotEmpty() && receiveVitaminId != null && receiveBlockId != null && currentDate.isNotEmpty()) {
                    val feedItem = ConsumptionRecordItem(
                        currentDate,
                        score.toDouble(),
                        receiveVitaminId,
                        0,
                        skuId,
                        receiveBlockId,
                        "None"
                    )
                    pemberianTernakViewModel.createFeedingRecord(listOf(feedItem))
                }
            }
            btnBatalVitamin.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        observerView()
    }

    private fun observerView() {
        pemberianTernakViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.loading.progressBar.isVisible = isLoading
            }
            feedingEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(
                    requireContext(),
                    it.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        dataAreaBlockViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.loading.progressBar.isVisible = it
        }
        dataAreaBlockViewModel.blockAreaInfoByIdEmitter.observe(viewLifecycleOwner) { data ->
            binding.tvBlockName.text = data.name
            binding.tvBlockInfo.text = data.info
        }
        dataAreaBlockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        listBarangDanJasaViewModel.apply {
            getAllProducts()
            observeLoading().observe(viewLifecycleOwner) {
                binding.loading.progressBar.isVisible = it
            }
            productsEmitter.observe(viewLifecycleOwner) { product ->
                val namesArray = product.map {
                    it.productName
                }.toTypedArray()
                binding.spinner.selected { position ->
                    skuId = product[position].skuId!!
                }
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, namesArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
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
}