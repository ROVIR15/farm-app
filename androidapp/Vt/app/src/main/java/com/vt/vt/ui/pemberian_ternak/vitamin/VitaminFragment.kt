package com.vt.vt.ui.pemberian_ternak.vitamin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.feeding_record.model.ConsumptionRecordItem
import com.vt.vt.databinding.FragmentVitaminBinding
import com.vt.vt.ui.barang_dan_jasa.ListBarangDanJasaViewModel
import com.vt.vt.ui.pemberian_ternak.PemberianTernakViewModel
import com.vt.vt.utils.getCurrentDate
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VitaminFragment : Fragment() {
    private var _binding: FragmentVitaminBinding? = null
    private val binding get() = _binding!!

    private val listBarangDanJasaViewModel by viewModels<ListBarangDanJasaViewModel>()

    private val pemberianTernakViewModel by viewModels<PemberianTernakViewModel>()

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
        val receiveVitaminId = arguments?.getInt("feedCategoryVitaminId")

        with(binding) {
            appBarVitamin.topAppBar.apply {
                title = "Vitamin"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            btnSimpanVitamin.setOnClickListener {
                val score = editTextRekamPemberianVitamin.text.toString().trim()
                val currentDate = getCurrentDate()

                if (score.isNotEmpty() && receiveVitaminId != null && receiveBlockId != null) {
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
            feedingEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        listBarangDanJasaViewModel.apply {
            getAllProducts()
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}