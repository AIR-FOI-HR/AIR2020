package hr.foi.academiclifestyle.ui.myclasses.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.foi.academiclifestyle.ui.myclasses.AttendanceFragment
import hr.foi.academiclifestyle.ui.myclasses.ScheduleFragment
import hr.foi.academiclifestyle.ui.myclasses.StatsGoalsFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    // Keep a mutable list of fragments for referencing
    val fragments: MutableList<Fragment> = mutableListOf()

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // Schedule
                val fragment = ScheduleFragment()
                fragments.add(fragment)
                return fragment
            }
            1 -> {
                val fragment = AttendanceFragment()
                fragments.add(fragment)
                return fragment
            }
            2 -> {
                // Stats & Goals
                val fragment = StatsGoalsFragment()
                fragments.add(fragment)
                return fragment
            }
            else -> {
                val fragment = ScheduleFragment()
                fragments.add(fragment)
                return fragment
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}