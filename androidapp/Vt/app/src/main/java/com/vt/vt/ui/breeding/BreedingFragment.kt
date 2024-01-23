package com.vt.vt.ui.breeding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.vt.vt.R
import com.vt.vt.databinding.FragmentBreedingBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.fattening.dialog.FatteningBottomDialogFragment
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreedingFragment : Fragment() {

    private var _binding: FragmentBreedingBinding? = null
    private val binding get() = _binding!!

    private val livestockViewModel by viewModels<LivestockViewModel>()
    private val optionLivestockBottomDialog by lazy { FatteningBottomDialogFragment() }
    private lateinit var mBundle: Bundle
    private var lineChart: LineChart? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreedingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = binding.linechartBreeding
        mBundle = Bundle()
        livestockViewModel.getLivestocks()
        setupLineChart()
        with(binding) {
            ivDatePicker.setOnClickListener {
                PickDatesUtils.setupDatePicker(requireContext(), tvBreedingDate)
            }
            contentBreedingCategoryRecordWeight.setOnClickListener {
                val navigationToWeightRecord =
                    R.id.action_breedingFragment_to_rekamBeratBadanFragment
                navigateToRecord(navigationToWeightRecord)
            }
            contentBreedingCategoryRecordMilk.setOnClickListener {
                val navigationToMilkRecord =
                    R.id.action_breedingFragment_to_recordMilkProductionFragment
                navigateToRecord(navigationToMilkRecord)
            }
            contentBreedingCategoryRecordHealth.setOnClickListener {
                val navigationToHealthRecord =
                    R.id.action_breedingFragment_to_rekamKesehatanFragment
                navigateToRecord(navigationToHealthRecord)
            }
            contentBreedingCategoryRecordBcs.setOnClickListener {
                val navigationToBcs = R.id.action_breedingFragment_to_rekamBCSFragment
                navigateToRecord(navigationToBcs)
            }
            contentBreedingCategoryRecordAnimalMating.setOnClickListener {
                findNavController().navigate(R.id.action_breedingFragment_to_listAnimalMatingsFragment)
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


    private fun navigateToRecord(destinationId: Int) {
        mBundle.putInt("navigate", destinationId)
        optionLivestockBottomDialog.arguments = mBundle
        optionLivestockBottomDialog.also {
            it.show(childFragmentManager, it::class.java.simpleName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var livestockId: Int = 0
    }
}