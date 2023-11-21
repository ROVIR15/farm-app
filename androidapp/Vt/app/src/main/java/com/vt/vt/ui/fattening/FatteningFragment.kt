package com.vt.vt.ui.fattening

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.vt.vt.R
import com.vt.vt.core.data.source.remote.fattening.model.BcsResults
import com.vt.vt.core.data.source.remote.fattening.model.FatteningResponse
import com.vt.vt.core.data.source.remote.fattening.model.WeightResults
import com.vt.vt.databinding.FragmentFatteningBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.fattening.dialog.FatteningBottomDialogFragment
import com.vt.vt.utils.PickDatesUtils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class FatteningFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentFatteningBinding? = null
    private val binding get() = _binding!!
    private val optionLivestockBottomDialog by lazy { FatteningBottomDialogFragment() }
    private val livestockViewModel by viewModels<LivestockViewModel>()
    private val fatteningViewModel by viewModels<FatteningViewModel>()

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
        mBundle = Bundle()
        with(binding) {
            ivDatePicker.setOnClickListener {
                PickDatesUtils.pickMonthAndYear(
                    requireActivity(),
                    tvFatteningDate
                ) { selectedDate ->
                    fatteningViewModel.updateSelectedDate(selectedDate)
                }
            }
            contentFatteningCategoryWeightRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryBcsRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryHealthRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryFoodRecord.setOnClickListener(this@FatteningFragment)
            contentFatteningCategoryHeightRecord.setOnClickListener(this@FatteningFragment)
        }
        observerFatteningView()
    }

    private fun observerFatteningView() {
        fatteningViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) { isLoading ->
                binding.homeRefreshLayout.isRefreshing = isLoading
            }
            currentDate.observe(viewLifecycleOwner) { selectedDate ->
                val inputDateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())
                val outputDateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                val date = inputDateFormat.parse(selectedDate)
                binding.tvFatteningDate.text = date?.let { outputDateFormat.format(it) }
                getFatteningGraph(selectedDate.toString())
                binding.homeRefreshLayout.setOnRefreshListener {
                    getFatteningGraph(selectedDate.toString())
                    binding.homeRefreshLayout.isRefreshing = false
                }

            }
            getFatteningGraphEmitter.observe(viewLifecycleOwner) { fattening ->
                if (fattening != null) setupLineChart(fattening)
            }
            isError().observe(viewLifecycleOwner) { errorMessage ->
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLineChart(apiData: FatteningResponse) {
        if (apiData.bcsResults?.date.isNullOrEmpty() and apiData.bcsResults?.score.isNullOrEmpty() or apiData.weightResults?.date.isNullOrEmpty() and apiData.weightResults?.score.isNullOrEmpty()) {
            lineChart.setNoDataText("Data bulan ini belum tersedia.")
            lineChart.clear()
            return
        }
        val (bcsEntries, weightEntries) = generateEntries(apiData.bcsResults, apiData.weightResults)
        setupDataSet(
            bcsEntries, weightEntries, apiData.bcsResults?.label, apiData.weightResults?.label
        )
        val generateBarLabel = apiData.weightResults?.date?.let { generateBarLabel(it) }
        lineChart.xAxis?.valueFormatter = IndexAxisValueFormatter(generateBarLabel)
        lineChart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis?.granularity = 2f
        lineChart.axisLeft?.axisMinimum = 0f

        lineChart.axisRight?.isEnabled = false
        lineChart.animateXY(3000, 3000)
        lineChart.invalidate()
    }

    private fun generateBarLabel(dataVals: List<String>?): ArrayList<String> {
        val barLabels = ArrayList<String>()
        if (dataVals != null) {
            for (i in dataVals.indices) {
                barLabels.add(dataVals[i])
            }
        }
        return barLabels
    }

    private fun generateEntries(
        resultBcs: BcsResults?, resultWeight: WeightResults?
    ): Pair<List<Entry>, List<Entry>> {
        val entriesBcs = ArrayList<Entry>()
        val entriesWeightResults = ArrayList<Entry>()

        resultBcs?.date?.forEachIndexed { index, _ ->
            resultBcs.score?.getOrNull(index)?.toFloat()?.let {
                entriesBcs.add(Entry(index.toFloat(), it))
            }
        }
        resultWeight?.date?.forEachIndexed { index, _ ->
            resultWeight.score?.getOrNull(index)?.toFloat()?.let {
                entriesWeightResults.add(Entry(index.toFloat(), it))
            }
        }

        return Pair(entriesBcs, entriesWeightResults)
    }

    private fun setupDataSet(
        entries1: List<Entry>, entries2: List<Entry>, label1: String?, label2: String?
    ) {
        val dataSet1 = LineDataSet(entries1, label1)
        val dataSet2 = LineDataSet(entries2, label2)

        customizeDataSet(dataSet1, Color.BLUE)
        customizeDataSet(dataSet2, Color.RED)

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(dataSet1)
        dataSets.add(dataSet2)

        val lineData = LineData(dataSets)
        lineChart.data = lineData
    }


    private fun customizeDataSet(dataSet: LineDataSet, color: Int) {
        dataSet.color = color
        dataSet.setCircleColor(color)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 6f
        dataSet.setDrawCircleHole(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.content_fattening_category_weight_record -> {
                val navigationToWeightRecord =
                    R.id.action_fatteningFragment_to_rekamBeratBadanFragment
                mBundle.putInt("navigate", navigationToWeightRecord)
                optionLivestockBottomDialog.arguments = mBundle
                optionLivestockBottomDialog.show(
                    childFragmentManager, optionLivestockBottomDialog::class.java.simpleName
                )
            }

            R.id.content_fattening_category_bcs_record -> {
                val navigationToBcsRecord =
                    R.id.action_fatteningFragment_to_rekamBCSFragment
                mBundle.putInt("navigate", navigationToBcsRecord)
                optionLivestockBottomDialog.arguments = mBundle
                optionLivestockBottomDialog.show(
                    childFragmentManager, optionLivestockBottomDialog::class.java.simpleName
                )
            }

            R.id.content_fattening_category_health_record -> {
                val navigationToHealthRecord =
                    R.id.action_fatteningFragment_to_rekamKesehatanFragment
                mBundle.putInt("navigate", navigationToHealthRecord)
                optionLivestockBottomDialog.arguments = mBundle
                optionLivestockBottomDialog.show(
                    childFragmentManager, optionLivestockBottomDialog::class.java.simpleName
                )
            }

            R.id.content_fattening_category_height_record -> {
                val navigationToHeightRecord =
                    R.id.action_fatteningFragment_to_rekamTinggiBadanFragment
                mBundle.putInt("navigate", navigationToHeightRecord)
                optionLivestockBottomDialog.arguments = mBundle
                optionLivestockBottomDialog.show(
                    childFragmentManager, optionLivestockBottomDialog::class.java.simpleName
                )
            }

            R.id.content_fattening_category_food_record -> {
                v.findNavController()
                    .navigate(R.id.action_fatteningFragment_to_penyimpanTernakFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
