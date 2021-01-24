package hr.foi.academiclifestyle.ui.home

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentHomeBinding
import hr.foi.academiclifestyle.dimens.CoursesEnum
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.util.RoundedHorizontalBarChartRenderer
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(activity).get(HomeViewModel::class.java)
    }

    private lateinit var binding: FragmentHomeBinding

    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false)

        // fix toggle animation for navView
        val drawerLayout : DrawerLayout = (activity as MainActivity).findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle((activity as MainActivity), (activity as MainActivity).findViewById(R.id.drawerLayout), R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.lifecycleOwner = this
        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)

        startAnimation()
        setupObservers()
        setThemeOptions()

        //enroledSubjects()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {

            if (it != null && it.jwtToken != "") {
                binding.txtYearValue.text = it.year.toString()
                if (it.program != 0)
                    binding.txtCourseValue.text = CoursesEnum.fromId(it.program!!)?.programName
                viewModel.fetchEnrolledSubjectCount()
                if(it.semester != 0 && it.semester != null)
                    binding.txtSemesterValue.text = it.semester.toString()
                viewModel.getAttendance()
            }
        })

        viewModel.subjectsEnroled?.observe(viewLifecycleOwner, Observer{
            if(it != null)
                binding.txtSubjectsEnrolledValue.text =it.toString()
        })

        viewModel.attendance?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                setupHBarCharts(it)
                finishAnimation()
            }
        })

    }

    private fun enroledSubjects(){
        binding.txtSubjectsEnrolledValue.text = viewModel.fetchEnrolledSubjectCount().toString()
    }

    private fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_house)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.foi_red))
        (activity as MainActivity?)?.window?.setStatusBarColor(ContextCompat.getColor(activity as MainActivity, R.color.foi_red));
        navHeader?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.foi_red))

        setNavigationColors()

    }

    private fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf())

        val colors = intArrayOf(
                (activity as MainActivity).getColor(R.color.grey_80), //unchecked
                (activity as MainActivity).getColor(R.color.foi_red), //checked
                (activity as MainActivity).getColor(R.color.grey_80)) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.setItemTextColor(navigationViewColorStateList)
        navView?.setItemIconTintList(navigationViewColorStateList)
    }



    private fun setupHBarCharts(attendance: Float) {
        
        //define charts and mutable lists of data
        val hBarChart: HorizontalBarChart = binding.hBarChart
        val hBarData: MutableList<BarEntry> = mutableListOf()

        val roundedBarChartRenderer = RoundedHorizontalBarChartRenderer(hBarChart, hBarChart.getAnimator(), hBarChart.getViewPortHandler())
        roundedBarChartRenderer.setmRadius(20f)
        hBarChart.setRenderer(roundedBarChartRenderer)

        //add data to lists
        hBarData.add(BarEntry(0F, attendance))

        //define a data set with the given data list and label
        val dataSet: BarDataSet = BarDataSet(hBarData, "(%)")

        //hbar
        dataSet.setColor(ContextCompat.getColor((activity as MainActivity), R.color.red_acc))
        hBarChart.description.text = ""
        hBarChart.setDrawGridBackground(false)
        hBarChart.setDrawBorders(true)
        hBarChart.setTouchEnabled(false)
        hBarChart.animateY(800)
        hBarChart.setBorderWidth(1F)

        var topAxis: YAxis = hBarChart.axisLeft
        topAxis.axisMinimum = 0F
        topAxis.axisMaximum = 100F
        topAxis.setDrawAxisLine(false)
        topAxis.setLabelCount(2, true)
        var bottomAxis: YAxis = hBarChart.axisRight
        bottomAxis.isEnabled = false

        var legend: Legend = hBarChart.legend
        legend.isEnabled = false

        hBarChart.xAxis.isEnabled = false

        //define the actual data with the styled data set
        val data: BarData = BarData(dataSet)
        data.setValueTextSize(11f)
        data.setValueTextColor(ContextCompat.getColor((activity as MainActivity), R.color.red_acc))

        //set data on the chart and refresh - further styling possible
        hBarChart.data = data
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