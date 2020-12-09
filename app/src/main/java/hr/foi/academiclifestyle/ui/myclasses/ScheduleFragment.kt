package hr.foi.academiclifestyle.ui.myclasses

import android.R.attr.outAnimation
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMyclassesScheduleBinding
import hr.foi.academiclifestyle.ui.MainActivity
import org.threeten.bp.LocalDate


class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, ScheduleViewModel.Factory(activity.application)).get(ScheduleViewModel::class.java)
    }

    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMyclassesScheduleBinding>(inflater, R.layout.fragment_myclasses_schedule, container, false)

        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)

        // Call getEvents each time the user picks a date
        // A mask will be used here to prevent rapid calls
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            inAnimation = AlphaAnimation(0f, 1f)
            inAnimation.setDuration(200)
            progressBarHolder.animation = inAnimation
            progressBarHolder.visibility = View.VISIBLE

            var date : LocalDate = LocalDate.of(year, month, dayOfMonth)
            val currentDay : String = date.dayOfWeek.plus(2L).toString().toLowerCase()
            viewModel.getEvents(currentDay, 0)
        }
        setupObservers()
        return binding.root
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it?.program != null && it.program != 0) {
                viewModel.getEvents("", it.program)
            }
        })
        viewModel.eventsUpdated.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                outAnimation = AlphaAnimation(1f, 0f)
                outAnimation.setDuration(200)
                progressBarHolder.animation = outAnimation
                progressBarHolder.visibility = View.GONE
                viewModel.resetEvents()
                //TODO add call for a function assigning values to view
            }
        })
    }
}