package com.vt.vt.ui.breeding

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.button.MaterialButton
import com.vt.vt.R
import com.vt.vt.databinding.FragmentBreedingBinding
import com.vt.vt.ui.bottom_navigation.livestock.LivestockViewModel
import com.vt.vt.ui.fattening.dialog.FatteningBottomDialogFragment
import com.vt.vt.utils.PickDatesUtils
import com.vt.vt.utils.selected
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
            contentBreedingCategoryRecordHealth.setOnClickListener {
                val navigationToHealthRecord =
                    R.id.action_breedingFragment_to_rekamKesehatanFragment
                navigateToRecord(navigationToHealthRecord)
            }
            contentBreedingCategoryRecordAnimalMating.setOnClickListener {
                findNavController().navigate(R.id.action_breedingFragment_to_listAnimalMatingsFragment)
            }
        }
    }

    private fun observerView(spinner: Spinner) {
        livestockViewModel.apply {
            observeLoading().observe(viewLifecycleOwner) {
                showLoading(it)
            }
            livestockItems.observe(viewLifecycleOwner) { livestock ->
                val nameArrays = livestock.map { item ->
                    item.name
                }.toTypedArray()
                val adapter = ArrayAdapter(requireActivity(), R.layout.item_spinner, nameArrays)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.selected { position ->
                    livestockId = livestock[position].id!!
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

    private fun showLoading(state: Boolean) {
        val btnSave = view?.findViewById<MaterialButton>(R.id.btn_save)
        val btnCancel = view?.findViewById<MaterialButton>(R.id.btn_cancel)
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBarLivestock)

        btnSave?.isEnabled = !state
        btnCancel?.isEnabled = !state

        btnSave?.setBackgroundColor(
            if (state) Color.GRAY
            else ContextCompat.getColor(requireActivity(), R.color.btn_blue_icon)
        )

        progressBar?.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var livestockId: Int = 0
    }
}