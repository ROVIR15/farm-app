package com.vt.vt.ui.pemberian_ternak.hijauan

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
import com.vt.vt.databinding.FragmentHijauanBinding
import com.vt.vt.ui.barang_dan_jasa.ListBarangDanJasaViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import com.vt.vt.ui.pemberian_ternak.PemberianTernakViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.calculateDelayForNextDay
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class HijauanFragment : Fragment() {

    private var _binding: FragmentHijauanBinding? = null
    private val binding get() = _binding!!

    private val hijauanViewModel by viewModels<HijauanViewModel>()
    private val listBarangDanJasaViewModel by viewModels<ListBarangDanJasaViewModel>()
    private val pemberianTernakViewModel by viewModels<PemberianTernakViewModel>()
    private val dataAreaBlockViewModel by viewModels<DataAreaViewModel>()

    private var value: Int? = 0
    private var skuId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHijauanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiveBlockId = arguments?.getInt("blockId")
        dataAreaBlockViewModel.getBlockAreaInfoById(receiveBlockId.toString())
        val receiveHijauanId = arguments?.getInt("feedCategoryHijauanId")

        with(binding) {
            appBarHijauan.topAppBar.apply {
                title = "Hijauan"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            ivDate.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvShowDate)
            }
            btnSimpanHijauan.setOnClickListener {
                val score = editTextValueHijauan.text.toString().trim()
                val date = formatterDateFromCalendar(tvShowDate.text.toString().trim())

                if (score.isNotEmpty() && date.isNotEmpty() && receiveHijauanId != null && receiveBlockId != null) {
                    btnSimpanHijauan.isEnabled = false
                    hijauanViewModel.setHijauanButtonFilled(blockId = receiveBlockId, value = false)
                    val currentDate = Date()
                    val delay = calculateDelayForNextDay(currentDate)
                    pemberianTernakViewModel.addStack(
                        blockId = receiveBlockId,
                        date = date,
                        score = score.toDouble(),
                        feedCategory = receiveHijauanId,
                        left = 0,
                        skuId = skuId,
                        blockAreaId = receiveBlockId,
                        remarks = "None"
                    )
                    btnSimpanHijauan.postDelayed({
                        btnSimpanHijauan.isEnabled = true
                        hijauanViewModel.setHijauanButtonFilled(
                            blockId = receiveBlockId,
                            value = true
                        )
                    }, delay)
                    view.findNavController().popBackStack()
                } else {
                    Toast.makeText(requireActivity(), "Silahkan Lengkapi Kolom", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            btnBatalHijauan.setOnClickListener {
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
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
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
            binding.tvInfo.text = data.info
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