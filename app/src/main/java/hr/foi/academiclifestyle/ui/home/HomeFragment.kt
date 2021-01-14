package hr.foi.academiclifestyle.ui.home

import android.content.res.ColorStateList
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentHomeBinding
import hr.foi.academiclifestyle.dimens.CoursesEnum

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(activity).get(HomeViewModel::class.java)
    }

    private lateinit var binding: FragmentHomeBinding

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

        setupObservers()
        setThemeOptions()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it != null && it.jwtToken != "") {
                binding.txtYearValue.text = it.year
                if (it.program != 0)
                    binding.txtCourseValue.text = CoursesEnum.fromId(it.program!!)?.programName
                viewModel.fetchEnrolledSubjectCount()
                if(it.semester != 0 && it.semester != null)
                    binding.txtSemesterValue.text = it.semester.toString()
            }
        })
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
}