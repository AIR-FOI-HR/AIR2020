package hr.foi.academiclifestyle.ui.ambience

import android.content.res.ColorStateList
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentAmbienceBinding
import hr.foi.academiclifestyle.ui.ambience.adapters.TabsPagerAdapter


class AmbienceFragment : Fragment() {

    private val viewModel: AmbienceViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, AmbienceViewModel.Factory(activity.application)).get(AmbienceViewModel::class.java)
    }
    private lateinit var binding: FragmentAmbienceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentAmbienceBinding>(inflater, R.layout.fragment_ambience, container, false)
        binding.lifecycleOwner = this

        // fix toggle animation for navView
        val drawerLayout : DrawerLayout = (activity as MainActivity).findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle((activity as MainActivity), (activity as MainActivity).findViewById(R.id.drawerLayout), R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setThemeOptions()
        createTabLayout(binding.tabLayout, binding.viewPager)
        return binding.root
    }

    fun setThemeOptions() {
        val imageView = (activity as MainActivity?)?.findViewById<ImageView>(R.id.toolbarlogo)
        val toolbar = (activity as MainActivity?)?.findViewById<Toolbar>(R.id.toolbar)
        val navHeader = (activity as MainActivity?)?.findViewById<ConstraintLayout>(R.id.navHeader)
        imageView?.setImageResource(R.drawable.ic_leaf)
        toolbar?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.green_primary))
        (activity as MainActivity?)?.window?.statusBarColor = ContextCompat.getColor(activity as MainActivity, R.color.green_primary)
        navHeader?.setBackgroundColor(ContextCompat.getColor(activity as MainActivity, R.color.green_primary))

        setNavigationColors()
    }

    fun setNavigationColors() {
        val navView = (activity as MainActivity?)?.findViewById<NavigationView>(R.id.navView)
        val states = arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked), intArrayOf())

        val colors = intArrayOf(
                (activity as MainActivity).getColor(R.color.grey_80), //unchecked
                (activity as MainActivity).getColor(R.color.green_primary), //checked
                (activity as MainActivity).getColor(R.color.grey_80)) //default

        val navigationViewColorStateList = ColorStateList(states, colors)

        navView?.itemTextColor = navigationViewColorStateList
        navView?.itemIconTintList = navigationViewColorStateList
    }

    fun createTabLayout(tabLayout: TabLayout, tabsViewpager: ViewPager2) {
        // Tabs Customization
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor((activity as MainActivity), R.color.green_primary))
        tabLayout.setBackgroundColor(ContextCompat.getColor((activity as MainActivity), R.color.green_secondary))
        tabLayout.tabTextColors = ContextCompat.getColorStateList((activity as MainActivity), android.R.color.white)

        // Number Of Tabs
        val numberOfTabs = 2

        // Show all Tabs in screen
        tabLayout.tabMode = TabLayout.MODE_FIXED

        // Set the ViewPager Adapter
        val adapter = TabsPagerAdapter(childFragmentManager, lifecycle, numberOfTabs)
        tabsViewpager.adapter = adapter

        // Set to not remove the cached fragments
        tabsViewpager.offscreenPageLimit = 2

        // This is necessary to fix content height between fragments
        tabsViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val view : View? = when (position) {
                    0 -> adapter.fragments[0].view
                    1 -> adapter.fragments[1].view
                    else -> adapter.fragments[0].view
                }
                view?.let {
                    updatePagerHeightForChild(view, tabsViewpager)
                }
            }

            private fun updatePagerHeightForChild(view: View, pager: ViewPager2) {
                view.post {
                    val wMeasureSpec =
                            View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                    val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    view.measure(wMeasureSpec, hMeasureSpec)

                    if (pager.layoutParams.height != view.measuredHeight) {
                        pager.layoutParams = (pager.layoutParams)
                                .also { lp ->
                                    lp.height = view.measuredHeight
                                }
                    }
                }
            }
        })

        // Link the TabLayout and the ViewPager2 together and set titles
        TabLayoutMediator(tabLayout, tabsViewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "My Location"
                }
                1 -> {
                    tab.text = "Other Rooms"
                }
            }
        }.attach()
    }
}