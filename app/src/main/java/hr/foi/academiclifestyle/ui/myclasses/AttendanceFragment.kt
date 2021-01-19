package hr.foi.academiclifestyle.ui.myclasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.database.model.Subject
import hr.foi.academiclifestyle.databinding.FragmentMyclassesAttendanceBinding
import hr.foi.academiclifestyle.dimens.AttendanceDetails
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.ui.myclasses.adapters.AttendanceDetailsRecyclerAdapter
import hr.foi.academiclifestyle.ui.myclasses.adapters.AttendanceRecyclerAdapter

class AttendanceFragment : Fragment(), AttendanceRecyclerAdapter.OnItemClickListener {

    private val viewModel: AttendanceViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, AttendanceViewModel.Factory(activity.application)).get(AttendanceViewModel::class.java)
    }

    private lateinit var subjectListHolder: MutableList<Subject>
    private lateinit var detailsListHolder: MutableList<AttendanceDetails>
    private lateinit var binding: FragmentMyclassesAttendanceBinding
    private lateinit var progressBarHolder: FrameLayout
    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMyclassesAttendanceBinding>(inflater, R.layout.fragment_myclasses_attendance, container, false)

        progressBarHolder = (activity as MainActivity).findViewById(R.id.progressBarHolder)

        binding.btnRightSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "1") {
                startAnimation()
                txtSemester.text = "2"
                viewModel.getSubjects(0, 2, 0L)
            }
        }
        binding.btnLeftSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "2") {
                startAnimation()
                txtSemester.text = "1"
                viewModel.getSubjects(0, 1, 0L)
            }
        }

        // Setup the recycler
        subjectListHolder = mutableListOf()
        detailsListHolder = mutableListOf()
        subjectListHolder.add(Subject(0, "-", "-", 0, 0, "-"))
        detailsListHolder.add(AttendanceDetails("", "", 0, 0, 0))
        binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(subjectListHolder, this)
        binding.attendanceRecycler.layoutManager = LinearLayoutManager((activity as MainActivity))
        binding.attendanceDetailsRecycler.adapter = AttendanceDetailsRecyclerAdapter(detailsListHolder)
        binding.attendanceDetailsRecycler.layoutManager = LinearLayoutManager((activity as MainActivity))

        binding.btnBack.setOnClickListener(View.OnClickListener {
            binding.detailsView.visibility = View.GONE
            binding.mainView.visibility = View.VISIBLE
        })

        setupObservers()
        return binding.root
    }

    override fun onItemClick(position: Int) {
        startAnimation()
        val subject = subjectListHolder[position]
        viewModel.getSubjectDetails(subject)
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it?.program != null && it.program != 0 && it.semester != 0 && it.program != null) {
                viewModel.getSubjects(it.program, 1, it.userId)
            }
        })
        viewModel.subjects?.observe(viewLifecycleOwner, Observer {
            if (it != null && viewModel.firstCall) {
                if (it.isNotEmpty()) {
                    subjectListHolder = it.toMutableList()
                    binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(subjectListHolder, this)
                } else {
                    subjectListHolder = mutableListOf()
                    subjectListHolder.add(Subject(0, "-", "-", 0, 0, "-"))
                    binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(subjectListHolder, this)
                }
                finishAnimation()
            } else
                viewModel.firstCall = true
        })
        viewModel.details.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isNotEmpty()) {
                    binding.txtSubjectTitle.text = it[0].name

                    detailsListHolder = it.toMutableList()
                    binding.attendanceDetailsRecycler.adapter = AttendanceDetailsRecyclerAdapter(detailsListHolder)

                    binding.mainView.visibility = View.GONE
                    binding.detailsView.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                            activity as MainActivity?,
                            "No subject selected!",
                            Toast.LENGTH_SHORT
                    ).show()
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
            }
        })
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