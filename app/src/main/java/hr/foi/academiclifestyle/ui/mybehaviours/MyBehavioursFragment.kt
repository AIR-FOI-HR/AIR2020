package hr.foi.academiclifestyle.ui.mybehaviours

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMybehavioursBinding
import hr.foi.academiclifestyle.dimens.DescriptionsEnum

class MyBehavioursFragment : Fragment() {

    private val viewModel: MyBehavioursViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, MyBehavioursViewModel.Factory(activity.application)).get(MyBehavioursViewModel::class.java)
    }

    private lateinit var binding: FragmentMybehavioursBinding

    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation
    private lateinit var description: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMybehavioursBinding>(inflater, R.layout.fragment_mybehaviours, container, false)

        binding.lifecycleOwner = this
        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)
        description = binding.tardinessDescription

        // fix toggle animation for navView
        val drawerLayout : DrawerLayout = (activity as MainActivity).findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle((activity as MainActivity), (activity as MainActivity).findViewById(R.id.drawerLayout), R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        startAnimation()
        setThemeOptions()
        setupObservers()
        return binding.root
    }

    fun setupObservers(){
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if( it != null){
                viewModel.getTardiness()
            }
        })

        viewModel.tardiness?.observe(viewLifecycleOwner, Observer {
            if (it != null){
                setupPieChart(it.late/it.currentAttendance.toFloat(),it.early/it.currentAttendance.toFloat(),it.inTime/it.currentAttendance.toFloat())

                if((it.late.toFloat() >= (it.early).toFloat()) && ((it.late).toFloat() >= (it.inTime).toFloat())) {
                    description.text = DescriptionsEnum.TARDINESS_LATE.description
                }
                else if((it.early.toFloat() >= (it.late).toFloat()) && ((it.early).toFloat() >= (it.inTime).toFloat())) {
                    description.text = DescriptionsEnum.TARDINESS_EARLY.description
                }
                else{
                    description.text = DescriptionsEnum.TARDINESS_NORMAL.description
                }
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


    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_bicycle)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary))
        (activity as MainActivity?)?.window?.statusBarColor = ContextCompat.getColor(activity as MainActivity, R.color.red_primary)
        navHeader?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary))

        setNavigationColors()
    }

    fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf())

        val colors = intArrayOf(
                (activity as MainActivity).getColor(R.color.grey_80), //unchecked
                (activity as MainActivity).getColor(R.color.red_primary), //checked
                (activity as MainActivity).getColor(R.color.grey_80)) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.itemTextColor = navigationViewColorStateList
        navView?.itemIconTintList = navigationViewColorStateList
    }

    //Setup charts
    private fun setupPieChart(late: Float, early: Float, inTime: Float) {
        val pieChart: PieChart = binding.pieChartMyBehaviors
        val pieEntries: MutableList<PieEntry> = mutableListOf()

        pieEntries.add(PieEntry(late*100, "Late"))
        pieEntries.add(PieEntry(early*100, "Early"))
        pieEntries.add(PieEntry(inTime*100, "In time"))

        val dataset: PieDataSet = PieDataSet(pieEntries, "")
        dataset.colors = mutableListOf(ContextCompat.getColor((activity as MainActivity), R.color.yellow_acc)
                , ContextCompat.getColor((activity as MainActivity), R.color.teal_acc), ContextCompat.getColor((activity as MainActivity), R.color.red_acc))
        //dataset.sliceSpace = 3f
        dataset.selectionShift = 4f

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
        legend.yEntrySpace = 12f
        legend.formSize = 13f
        legend.textSize = 15f

        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun startAnimation() {
        inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        progressBarHolder.animation = inAnimation
        progressBarHolder.visibility = View.VISIBLE
    }

    private fun finishAnimation() {
        outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200
        progressBarHolder.animation = outAnimation
        progressBarHolder.visibility = View.GONE
    }
}