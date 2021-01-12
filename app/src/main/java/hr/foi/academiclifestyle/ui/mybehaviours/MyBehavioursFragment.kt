package hr.foi.academiclifestyle.ui.mybehaviours

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentAmbienceBinding
import hr.foi.academiclifestyle.databinding.FragmentMybehavioursBinding
import hr.foi.academiclifestyle.ui.ambience.AmbienceViewModel
import kotlin.math.absoluteValue

class MyBehavioursFragment : Fragment() {


    private val viewModel: MyBehavioursViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, MyBehavioursViewModel.Factory(activity.application)).get(MyBehavioursViewModel::class.java)
    }

    private lateinit var binding: FragmentMybehavioursBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMybehavioursBinding>(inflater, R.layout.fragment_mybehaviours, container, false)

        // fix toggle animation for navView
        var drawerLayout : DrawerLayout = (activity as MainActivity).findViewById(R.id.drawerLayout)
        var toggle = ActionBarDrawerToggle((activity as MainActivity), (activity as MainActivity).findViewById(R.id.drawerLayout), R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setThemeOptions()
        setupPieChart()
        return binding.root
    }


    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_bicycle)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary))
        (activity as MainActivity?)?.window?.setStatusBarColor(ContextCompat.getColor(activity as MainActivity, R.color.red_primary));
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

        navView?.setItemTextColor(navigationViewColorStateList)
        navView?.setItemIconTintList(navigationViewColorStateList)
    }

    //Setup charts
    private fun setupPieChart() {
        val pieChart: PieChart = binding.pieChartMyBehaviors
        val pieEntries: MutableList<PieEntry> = mutableListOf()

        pieEntries.add(PieEntry(45F, "Late"))
        pieEntries.add(PieEntry(20F, "Early"))
        pieEntries.add(PieEntry(35F, "In time"))

        val dataset: PieDataSet = PieDataSet(pieEntries, "")
        dataset.setColors(mutableListOf(ContextCompat.getColor((activity as MainActivity), R.color.yellow_acc)
            , ContextCompat.getColor((activity as MainActivity), R.color.teal_acc), ContextCompat.getColor((activity as MainActivity), R.color.red_acc)))
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
}