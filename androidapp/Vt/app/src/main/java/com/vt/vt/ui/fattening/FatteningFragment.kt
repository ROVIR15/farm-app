package com.vt.vt.ui.fattening

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.vt.vt.R
import com.vt.vt.databinding.FragmentFatteningBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.selected
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FatteningFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFatteningBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<LivestockViewModel>()

    private lateinit var mBundle: Bundle
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFatteningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = binding.linechartFattening
        setupLineChart()
        with(binding) {
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireActivity(), binding.tvFatteningDate)
            }
            contentFatteningCategoryWeightRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryBcsRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryHealthRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryFoodRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryHeightRecord.setOnClickListener(this@FatteningFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observerView(spinner: Spinner) {
        livestockViewModel.apply {
            getLivestocks()
            livestockItems.observe(viewLifecycleOwner) { livestock ->
                val nameArrays = livestock.map { item ->
                    item.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArrays)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.selected { position ->
                    livestockId = livestock[position].id

                }
            }
            isError().observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLineChart() {
        val values = ArrayList<Entry>()
        values.add(Entry(1f, 10f))
        values.add(Entry(2f, 20f))
        values.add(Entry(3f, 15f))
        values.add(Entry(4f, 30f))
        values.add(Entry(5f, 25f))

        val dataSet = LineDataSet(values, "DataSet 1")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 6f
        dataSet.setDrawCircleHole(false)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet)

        val lineData = LineData(dataSets)

        lineChart.data = lineData

        // Customize X-axis
        val xAxis = lineChart.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM

        // Customize Y-axis
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft?.axisMinimum = 0f

        // Hide right Y-axis
        lineChart.axisRight?.isEnabled = false

        lineChart.invalidate()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.content_fattening_category_weight_record -> {
                showBottomSheet(1)
            }

            R.id.content_fattening_category_bcs_record -> {
                showBottomSheet(2)
            }

            R.id.content_fattening_category_health_record -> {
                showBottomSheet(3)
            }

            R.id.content_fattening_category_height_record -> {
                showBottomSheet(4)
            }

            R.id.content_fattening_category_food_record -> {
                v.findNavController()
                    .navigate(R.id.action_fatteningFragment_to_penyimpanTernakFragment)
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(buttonId: Int) {
        val dialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_farm, null)
        dialog.setContentView(view)
        val spinner = view.findViewById<Spinner>(R.id.sp_select_farm)
        val btnSave = view.findViewById<MaterialButton>(R.id.btn_save)
        val btnCancel = view.findViewById<MaterialButton>(R.id.btn_cancel)
        observerView(spinner)
        dialog.show()

        btnSave?.setOnClickListener {
            mBundle = Bundle()
            livestockId?.let { mBundle.putInt("livestockId", it) }
            navDirectionPage(buttonId, 1)
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            navDirectionPage(buttonId, 2)
            dialog.dismiss()
        }
    }

    private fun navDirectionPage(buttonId: Int, option: Int?) {
        when (buttonId) {
            1 -> {
                when (option) {
                    1 -> {
                        if (!mBundle.isEmpty) {
                            view?.findNavController()?.navigate(
                                R.id.action_fatteningFragment_to_rekamBeratBadanFragment,
                                mBundle
                            )
                        }
                    }
                }
            }

            2 -> {
                when (option) {
                    1 -> {
                        if (!mBundle.isEmpty)
                            view?.findNavController()
                                ?.navigate(
                                    R.id.action_fatteningFragment_to_rekamBCSFragment,
                                    mBundle
                                )
                    }
                }
            }

            3 -> {
                when (option) {
                    1 -> {
                        if (!mBundle.isEmpty)
                            view?.findNavController()
                                ?.navigate(
                                    R.id.action_fatteningFragment_to_rekamKesehatanFragment,
                                    mBundle
                                )
                    }
                }
            }

            4 -> {
                when (option) {
                    1 -> {
                        if (!mBundle.isEmpty)
                            view?.findNavController()
                                ?.navigate(
                                    R.id.action_fatteningFragment_to_rekamTinggiBadanFragment,
                                    mBundle
                                )
                    }
                }
            }
        }
    }

    companion object {
        var livestockId: Int? = null
    }
}