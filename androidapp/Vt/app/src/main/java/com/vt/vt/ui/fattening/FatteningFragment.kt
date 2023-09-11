package com.vt.vt.ui.fattening

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.vt.vt.R
import com.vt.vt.databinding.FragmentFatteningBinding
import com.vt.vt.utils.PickDatesUtils

class FatteningFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFatteningBinding? = null
    private val binding get() = _binding!!

    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            contentFatteningCategoryHeightRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryBcsRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryHealthRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryFoodRecord.setOnClickListener(this@FatteningFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        lineChart?.data = lineData

        // Customize X-axis
        val xAxis = lineChart?.xAxis
        xAxis?.position = XAxis.XAxisPosition.BOTTOM

        // Customize Y-axis
        val yAxisLeft = lineChart?.axisLeft
        yAxisLeft?.axisMinimum = 0f

        // Hide right Y-axis
        lineChart?.axisRight?.isEnabled = false

        lineChart?.invalidate()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.content_fattening_category_height_record -> {
                v.findNavController()
                    .navigate(R.id.action_fatteningFragment_to_rekamBeratBadanFragment)
            }

            R.id.content_fattening_category_bcs_record -> {
                v.findNavController().navigate(R.id.action_fatteningFragment_to_rekamBCSFragment)
            }

            R.id.content_fattening_category_health_record -> {
                v.findNavController()
                    .navigate(R.id.action_fatteningFragment_to_rekamKesehatanFragment)
            }

            R.id.content_fattening_category_food_record -> {
                v.findNavController()
                    .navigate(R.id.action_fatteningFragment_to_penyimpanTernakFragment)
            }
        }
    }

}