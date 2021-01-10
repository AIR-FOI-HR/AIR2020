package hr.foi.academiclifestyle.ui.ambience

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentAmbienceOtherroomsBinding
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.ui.ambience.helpers.BuildingItemSelectedListener
import hr.foi.academiclifestyle.ui.ambience.helpers.RoomItemSelectedListener

class OtherRoomsFragment : Fragment() {

    private val viewModel: OtherRoomsViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(activity).get(OtherRoomsViewModel::class.java)
    }

    private lateinit var binding: FragmentAmbienceOtherroomsBinding
    private var spinnerBuildings: Spinner? = null
    private var spinnerRooms: Spinner? = null
    private var adapterBuildings: ArrayAdapter<String>? = null
    private var adapterRooms: ArrayAdapter<String>? = null

    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    private var buildingList = arrayOf("FOI 1", "FOI 2")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentAmbienceOtherroomsBinding>(inflater, R.layout.fragment_ambience_otherrooms, container, false)

        //setup the spinners
        spinnerBuildings = binding.spinnerBuilding
        spinnerRooms = binding.spinnerRoom

        adapterBuildings = ArrayAdapter((activity as MainActivity), R.layout.spinner_programs, buildingList)
        adapterBuildings?.setDropDownViewResource(R.layout.spinner_drop_down)
        spinnerBuildings?.adapter = adapterBuildings
        spinnerBuildings?.onItemSelectedListener = BuildingItemSelectedListener(viewModel, ::startAnimation)
        spinnerRooms?.onItemSelectedListener = RoomItemSelectedListener(viewModel, ::startAnimation)

        //TODO get data from database (currently hardcoded)
        setupHBarCharts()
        setupPieChart()

        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)
        startAnimation()

        //initially get list for the first building in the list
        viewModel.fetchRoomList(buildingList[0])
        setupObservers()

        //TODO set window position in accordance to data
        return binding.root
    }

    private fun setupObservers() {
        viewModel.roomList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapterRooms = ArrayAdapter((activity as MainActivity), R.layout.spinner_programs, it)
                adapterRooms?.setDropDownViewResource(R.layout.spinner_drop_down)
                spinnerRooms?.adapter = adapterRooms
                finishAnimation()
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
                }
                finishAnimation()
            }
        })
    }

    private fun setupHBarCharts() {
        //define charts and mutable lists of data
        val hBarChart: HorizontalBarChart = binding.hBarChart
        val hBarChart2: HorizontalBarChart = binding.hBarChart2
        val hBarChart3: HorizontalBarChart = binding.hBarChart3
        val hBarData: MutableList<BarEntry> = mutableListOf()
        val hBarData2: MutableList<BarEntry> = mutableListOf()
        val hBarData3: MutableList<BarEntry> = mutableListOf()

        //add data to lists
        hBarData.add(BarEntry(0F, 22F))
        hBarData2.add(BarEntry(0F, 1013F))
        hBarData3.add(BarEntry(0F, 20F))

        //define a data set with the given data list and label
        val dataSet: BarDataSet = BarDataSet(hBarData, "(C)")
        val dataSet2: BarDataSet = BarDataSet(hBarData2, "(hPa)")
        val dataSet3: BarDataSet = BarDataSet(hBarData3, "(%)")

        //--set styling--
        //hbar1
        dataSet.setColor(ContextCompat.getColor((activity as MainActivity), R.color.red_acc))
        hBarChart.description.text = ""
        hBarChart.setDrawGridBackground(false)
        hBarChart.setDrawBorders(false)
        hBarChart.setTouchEnabled(false)
        hBarChart.animateY(800)

        var topAxis: YAxis = hBarChart.axisLeft
        topAxis.axisMinimum = 0F
        topAxis.axisMaximum = 60F
        topAxis.setDrawAxisLine(false)
        topAxis.setLabelCount(4, true)
        var bottomAxis: YAxis = hBarChart.axisRight
        bottomAxis.isEnabled = false

        var legend: Legend = hBarChart.legend
        legend.isEnabled = false

        //hBarChart.xAxis.axisMinimum = 0F
        //hBarChart.xAxis.axisMaximum = 0.4F
        hBarChart.xAxis.isEnabled = false

        //hbar2
        dataSet2.setColor(ContextCompat.getColor((activity as MainActivity), R.color.blue_acc))
        hBarChart2.description.text = ""
        hBarChart2.setDrawGridBackground(false)
        hBarChart2.setDrawBorders(false)
        hBarChart2.setTouchEnabled(false)
        hBarChart2.animateY(800)

        topAxis = hBarChart2.axisLeft
        topAxis.axisMinimum = 0F
        topAxis.axisMaximum = 2000F
        topAxis.setDrawAxisLine(false)
        topAxis.setLabelCount(4, true)
        bottomAxis = hBarChart2.axisRight
        bottomAxis.isEnabled = false
        hBarChart2.xAxis.isEnabled = false

        legend = hBarChart2.legend
        legend.isEnabled = false

        //hbar3
        dataSet3.setColor(ContextCompat.getColor((activity as MainActivity), R.color.teal_acc))
        hBarChart3.description.text = ""
        hBarChart3.setDrawGridBackground(false)
        hBarChart3.setDrawBorders(false)
        hBarChart3.setTouchEnabled(false)
        hBarChart3.animateY(800)

        topAxis = hBarChart3.axisLeft
        topAxis.axisMinimum = 0F
        topAxis.axisMaximum = 100F
        topAxis.setDrawAxisLine(false)
        topAxis.setLabelCount(4, true)
        bottomAxis = hBarChart3.axisRight
        bottomAxis.isEnabled = false
        hBarChart3.xAxis.isEnabled = false

        legend = hBarChart3.legend
        legend.isEnabled = false

        //define the actual data with the styled data set
        val data: BarData = BarData(dataSet)
        val data2: BarData = BarData(dataSet2)
        val data3: BarData = BarData(dataSet3)
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor((activity as MainActivity), R.color.red_acc))
        data2.setValueTextSize(11f)
        data2.setValueTextColor(ContextCompat.getColor((activity as MainActivity), R.color.blue_acc))
        data3.setValueTextSize(11f)
        data3.setValueTextColor(ContextCompat.getColor((activity as MainActivity), R.color.teal_acc))

        //set data on the chart and refresh - further styling possible
        hBarChart.data = data
        hBarChart2.data = data2
        hBarChart3.data = data3
        hBarChart.invalidate()
        hBarChart2.invalidate()
        hBarChart3.invalidate()
    }

    private fun setupPieChart() {
        val pieChart: PieChart = binding.pieChart
        val pieEntries: MutableList<PieEntry> = mutableListOf()

        pieEntries.add(PieEntry(81F, "Nitrogen"))
        pieEntries.add(PieEntry(18F, "Oxygen"))
        pieEntries.add(PieEntry(1F, "Argon, other gases..."))

        val dataset: PieDataSet = PieDataSet(pieEntries, "")
        dataset.setColors(mutableListOf(ContextCompat.getColor((activity as MainActivity), R.color.yellow_acc)
                , ContextCompat.getColor((activity as MainActivity), R.color.teal_acc), ContextCompat.getColor((activity as MainActivity), R.color.red_acc)))
        //dataset.sliceSpace = 3f
        dataset.selectionShift = 8f

        val pieData: PieData = PieData(dataset)
        pieData.setValueTextSize(11f)

        pieChart.description.text = ""
        pieChart.animateY(800)
        pieChart.holeRadius = 60f
        pieChart.setDrawEntryLabels(false)
        pieChart.transparentCircleRadius = 65f

        //custom legend
        val legend: Legend = pieChart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.yEntrySpace = 20f
        legend.formSize = 12f
        legend.textSize = 11f

        pieChart.data = pieData
        pieChart.invalidate()
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