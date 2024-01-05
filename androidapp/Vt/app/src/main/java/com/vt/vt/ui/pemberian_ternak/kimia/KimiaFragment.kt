package com.vt.vt.ui.pemberian_ternak.kimia

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.feeding_record.dto.ConsumptionRecordItem
import com.vt.vt.databinding.FragmentKimiaBinding
import com.vt.vt.ui.barang_dan_jasa.ListItemsAndServiceViewModel
import com.vt.vt.ui.file_provider.dataarea.DataAreaViewModel
import com.vt.vt.ui.pemberian_ternak.FeedingViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.formatterDateFromCalendar
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class KimiaFragment : Fragment() {
    private var _binding: FragmentKimiaBinding? = null
    private val binding get() = _binding!!

    private val listItemsAndServiceViewModel by viewModels<ListItemsAndServiceViewModel>()
    private val feedingViewModel by viewModels<FeedingViewModel>()
    private val dataAreaBlockViewModel by viewModels<DataAreaViewModel>()

    private var skuId: Int? = null
    private var receiveBlockId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKimiaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveBlockId = arguments?.getInt("blockId")
        dataAreaBlockViewModel.getBlockAreaInfoById(receiveBlockId.toString())
        val receiveKimiaId = arguments?.getInt("feedCategoryKimiaId")

        with(binding) {
            appBarKimia.topAppBar.apply {
                title = "Kimia"
                setNavigationOnClickListener {
                    findNavController().popBackStack()
                }
            }
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), tvShowDate)
            }
            btnSimpanKimia.setOnClickListener {
                val score = editTextRekamPemberianKimia.text.toString().trim()
                val createdAt = formatterDateFromCalendar(tvShowDate.text.toString().trim())
                if (score.isNotEmpty() && createdAt.isNotEmpty() && receiveKimiaId != null && receiveBlockId != null && skuId != null) {
                    val consumptionRecordItem = mutableListOf(
                        ConsumptionRecordItem(
                            date = createdAt,
                            score = score.toDouble(),
                            feedCategory = receiveKimiaId,
                            left = 0,
                            skuId = skuId,
                            blockAreaId = receiveBlockId,
                            remarks = "None"
                        )
                    )
                    val map = mapOf(
                        receiveBlockId!! to consumptionRecordItem
                    )
                    feedingViewModel.push(map)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        R.string.please_fill_all_column,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
            btnBatalKimia.setOnClickListener {
                view.findNavController().popBackStack()
            }
        }
        observerView()
    }

    private fun observerView() {
        feedingViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.loading.progressBar.isVisible = isLoading
            }
            feedingEmitter.observe(viewLifecycleOwner) {
                view?.findNavController()?.popBackStack()
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
            }
            observeException().observe(viewLifecycleOwner) { e ->
                Log.e(ContentValues.TAG, getString(R.string.failed_save_data, e?.message), e)
            }
            pushFeeding.observe(viewLifecycleOwner) { (isCommitSuccessful, _) ->
                if (isCommitSuccessful) {
                    binding.loading.progressBar.isVisible = true
                    lifecycleScope.launch {
                        delay(500)
                        withContext(Dispatchers.Main) {
                            binding.btnSimpanKimia.isEnabled = false
                            view?.findNavController()?.popBackStack()
                        }
                    }
                } else {
                    Toast.makeText(requireActivity(), R.string.failed_save_data, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        dataAreaBlockViewModel.observeLoading().observe(viewLifecycleOwner) {
            binding.loading.progressBar.isVisible = it
        }
        dataAreaBlockViewModel.blockAreaInfoByIdEmitter.observe(viewLifecycleOwner) { data ->
            if (receiveBlockId != null) {
                binding.tvBlockName.text = data.name
                binding.tvBlockInfo.text = data.info
            } else {
                Toast.makeText(requireActivity(), R.string.block_is_missing, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        dataAreaBlockViewModel.isError().observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        listItemsAndServiceViewModel.apply {
            getAllProducts()
            observeLoading().observe(viewLifecycleOwner) {
                binding.loading.progressBar.isVisible = it
            }
            productsEmitter.observe(viewLifecycleOwner) { product ->
                val namesArray = product.map {
                    it.productName
                }.toTypedArray()
                val productsArrayWithPrompt = arrayOf(getString(R.string.prompt_select_item)) + namesArray
                val adapter =
                    ArrayAdapter(requireActivity(), R.layout.item_spinner, productsArrayWithPrompt)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
                binding.spinner.selected { position ->
                    skuId = if (position == 0) {
                        null
                    } else {
                        product[position - 1].skuId
                    }
                }
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