package hr.foi.academiclifestyle.ui.myclasses

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.database.model.Event
import hr.foi.academiclifestyle.databinding.FragmentMyclassesScheduleBinding
import hr.foi.academiclifestyle.dimens.ScheduleEvent
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.ui.myclasses.adapters.ScheduleRecyclerAdapter
import hr.foi.academiclifestyle.util.CustomDialog
import org.threeten.bp.LocalDate


class ScheduleFragment : Fragment(), ScheduleRecyclerAdapter.OnItemClickListener {

    private val viewModel: ScheduleViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, ScheduleViewModel.Factory(activity.application)).get(ScheduleViewModel::class.java)
    }

    private lateinit var binding: FragmentMyclassesScheduleBinding
    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMyclassesScheduleBinding>(inflater, R.layout.fragment_myclasses_schedule, container, false)

        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)
        startAnimation()

        // Call getEvents each time the user picks a date
        // A mask is used here to prevent rapid calls
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            startAnimation()
            val currentDate : LocalDate = LocalDate.of(year, month+1, dayOfMonth)
            viewModel.getEvents(currentDate, 0)
        }

        // Setup the recycler
        val scheduleList = createScheduleList(listOf())
        binding.scheduleRecycler.adapter = ScheduleRecyclerAdapter(scheduleList, this)
        binding.scheduleRecycler.layoutManager = LinearLayoutManager((activity as MainActivity))
        binding.scheduleRecycler.setHasFixedSize(true)

        setupObservers()
        return binding.root
    }

    override fun onItemClick(position: Int, event: ScheduleEvent) {
        val dialog: CustomDialog = CustomDialog()
        dialog.showDialog((activity as MainActivity), event)
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it?.program != null && it.program != 0) {
                viewModel.getEvents(null, it.program)
            }
        })
        viewModel.eventsUpdated.observe(viewLifecycleOwner, Observer {
            // used only for updating the animation
            if (it != null) {
                finishAnimation()
                viewModel.resetEvents()
            }
        })
        viewModel.events?.observe(viewLifecycleOwner, Observer {
            if (it != null && viewModel.firstCall) {
                if (it.isNotEmpty()) {
                    val scheduleList = createScheduleList(it)
                    binding.scheduleRecycler.adapter = ScheduleRecyclerAdapter(scheduleList, this)
                } else {
                    val scheduleList = createScheduleList(listOf())
                    binding.scheduleRecycler.adapter = ScheduleRecyclerAdapter(scheduleList, this)
                }
            } else
                // used to prevent double refresh on first fragment show
                viewModel.firstCall = true
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

    // this method will always return a list with a fixed amount of elements
    private fun createScheduleList(eventList: List<Event>) : List<ScheduleEvent>{
        val scheduleList: MutableList<ScheduleEvent> = mutableListOf()
        for (i in 7..20) {
            var numStart: String = i.toString()
            var numEnd: String = (i + 1).toString()
            if (i < 10)
                numStart = "0$numStart"
            if (i+1 < 10)
                numEnd = "0$numEnd"

            // variables
            var time = "$numStart-$numEnd"
            var name = "-"
            var status = 0
            var userLogTime = ""

            val event = eventList.find {
                var found = false
                if (it.startTime != null && it.endTime != null) {
                    val start = it.startTime.split(":")[0].toInt()
                    val end = it.endTime.split(":")[0].toInt()
                    if (i in start until end)
                        found = true
                }
                found
            }
            if (event != null) {
                name = event.name!!
                status = event.status!!
                userLogTime = event.date!!
            }
            scheduleList.add(ScheduleEvent(time, name, status, userLogTime))
        }
        return scheduleList
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