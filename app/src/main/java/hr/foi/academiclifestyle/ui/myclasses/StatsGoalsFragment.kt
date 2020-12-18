package hr.foi.academiclifestyle.ui.myclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMyclassesStatsgoalsBinding

class StatsGoalsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMyclassesStatsgoalsBinding>(inflater, R.layout.fragment_myclasses_statsgoals, container, false)
        return binding.root
    }
}