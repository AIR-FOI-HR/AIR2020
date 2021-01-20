package hr.foi.academiclifestyle.ui.myclasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMyclassesStatsgoalsBinding
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.ui.myclasses.helpers.SubjectItemSelectedListener

class StatsGoalsFragment : Fragment() {

    private val viewModel: StatsGoalsViewModel by lazy {
        ViewModelProvider(this).get(StatsGoalsViewModel::class.java)
    }

    private lateinit var binding: FragmentMyclassesStatsgoalsBinding
    private var spinnerSubjects: Spinner? = null
    private var adapterSubjects: ArrayAdapter<String>? = null
    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMyclassesStatsgoalsBinding>(inflater, R.layout.fragment_myclasses_statsgoals, container, false)
        binding.lifecycleOwner= this

        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)

        spinnerSubjects = binding.spinnerSubjects
        spinnerSubjects?.onItemSelectedListener = SubjectItemSelectedListener(viewModel, ::startAnimation)

        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.program != 0 && it.semester != 0) {
                    viewModel.getSubjectsForSpinner(it.program!!, it.semester!!)
                }
            }
        })
        viewModel.subjectList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapterSubjects = ArrayAdapter((activity as MainActivity), R.layout.spinner_programs, it)
                adapterSubjects?.setDropDownViewResource(R.layout.spinner_drop_down)
                spinnerSubjects?.adapter = adapterSubjects

                //this triggers if the dropdown is empty - onItemSelected doesn't work on empty lists
                if (it.isEmpty()) {
                    viewModel.getGraphData("")
                }
            }
        })
        viewModel.graphData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setupBarChart(it.userData, it.avgData, it.maxData)
                if (viewModel.statsFirstCall) {
                    finishAnimation()
                } else {
                    viewModel.statsFirstCall = true
                }
            }
        })
        viewModel.responseType.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it) {
                    2 -> Toast.makeText(
                            activity as MainActivity?,
                            "Bad request!",
                            Toast.LENGTH_SHORT
                    ).show()
                    3 -> Toast.makeText(
                            activity as MainActivity?,
                            "Server Error, please try again!",
                            Toast.LENGTH_SHORT
                    ).show()
                    4 -> Toast.makeText(
                            activity as MainActivity?,
                            "Unknown Error!",
                            Toast.LENGTH_SHORT
                    ).show()
                    5 -> Toast.makeText(
                            activity as MainActivity?,
                            "No Subjects found!",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                finishAnimation()
            }
        })
    }

    private fun setupBarChart(userAtt: Int, avgAtt: Int, maxAtt: Int) {
        val barChart: BarChart = binding.barChart
        val barData: MutableList<BarEntry> = mutableListOf()

        barData.add(BarEntry(0F, userAtt.toFloat()))
        barData.add(BarEntry(1F, avgAtt.toFloat()))

        val colors: MutableList<Int> = mutableListOf()
        colors.add(ContextCompat.getColor((activity as MainActivity), R.color.teal_acc))
        colors.add(ContextCompat.getColor((activity as MainActivity), R.color.yellow_primary))

        val dataSet: BarDataSet = BarDataSet(barData, "")
        dataSet.setColors(colors)
        barChart.description.text = ""
        barChart.setDrawGridBackground(false)
        barChart.setDrawBorders(false)
        barChart.setTouchEnabled(false)
        barChart.animateX(800)

        val rightAxis = barChart.axisRight
        rightAxis.axisMinimum = 0F
        rightAxis.axisMaximum = 100F
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawLabels(false)
        rightAxis.setLabelCount(6, true)
        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0F
        leftAxis.axisMaximum = 100F
        leftAxis.setLabelCount(6, true)
        val topAxis = barChart.xAxis
        topAxis.setDrawLabels(false)
        topAxis.setDrawGridLines(false)

        val data: BarData = BarData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColors(colors)
        data.setBarWidth(0.5f)

        val legend: Legend = barChart.legend
        val legend1: LegendEntry = LegendEntry()
        val legend2: LegendEntry = LegendEntry()
        legend1.form = Legend.LegendForm.CIRCLE
        legend1.formColor = colors[0]
        legend1.label = "You"
        legend1.formSize = 12f
        legend2.form = Legend.LegendForm.CIRCLE
        legend2.formColor = colors[1]
        legend2.label = "Avg"
        legend2.formSize = 12f
        var legendList: MutableList<LegendEntry> = mutableListOf()
        legendList.add(legend1)
        legendList.add(legend2)
        legend.setCustom(legendList)
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.yEntrySpace = 30f
        legend.textSize = 11f
        legend.isWordWrapEnabled = true

        barChart.setData(data)
        barChart.setFitBars(true)
        barChart.invalidate()
    }

    private fun startAnimation() {
        inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.setDuration(200)
        progressBarHolder.animation = inAnimation
        progressBarHolder.visibility = View.VISIBLE
    }

    private fun finishAnimation() {
        outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.setDuration(200)
        progressBarHolder.animation = outAnimation
        progressBarHolder.visibility = View.GONE
    }
}