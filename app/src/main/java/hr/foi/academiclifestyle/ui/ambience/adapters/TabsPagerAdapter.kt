package hr.foi.academiclifestyle.ui.ambience.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.foi.academiclifestyle.ui.ambience.MyLocationFragment
import hr.foi.academiclifestyle.ui.ambience.OtherRoomsFragment

class TabsPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    // Keep a mutable list of fragments for referencing
    val fragments: MutableList<Fragment> = mutableListOf()

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // Schedule
                val fragment = MyLocationFragment()
                fragments.add(fragment)
                return fragment
            }
            1 -> {
                val fragment = OtherRoomsFragment()
                fragments.add(fragment)
                return fragment
            }
            else -> {
                val fragment = MyLocationFragment()
                fragments.add(fragment)
                return fragment
            }
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}