package hr.foi.academiclifestyle.ui.myclasses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import hr.foi.academiclifestyle.ui.MainActivity
import hr.foi.academiclifestyle.ui.myclasses.adapters.AttendanceRecyclerAdapter
import hr.foi.academiclifestyle.ui.myclasses.adapters.ScheduleRecyclerAdapter

class AttendanceFragment : Fragment(), AttendanceRecyclerAdapter.OnItemClickListener {

    private val viewModel: AttendanceViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(this, AttendanceViewModel.Factory(activity.application)).get(AttendanceViewModel::class.java)
    }

    private lateinit var binding: FragmentMyclassesAttendanceBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMyclassesAttendanceBinding>(inflater, R.layout.fragment_myclasses_attendance, container, false)

        binding.btnRightSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "1") {
                txtSemester.text = "2"
                viewModel.getSubjects(0, 2)
            }
        }
        binding.btnLeftSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "2") {
                txtSemester.text = "1"
                viewModel.getSubjects(0, 1)
            }
        }

        // Setup the recycler
        val emptySubjectList: MutableList<Subject> = mutableListOf()
        emptySubjectList.add(Subject(1, "-", "-", 1, 1, "-"))
        binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(emptySubjectList, this)
        binding.attendanceRecycler.layoutManager = LinearLayoutManager((activity as MainActivity))

        setupObservers()
        return binding.root
    }

    override fun onItemClick(position: Int) {
        Toast.makeText((activity as MainActivity), "Position: $position", Toast.LENGTH_SHORT).show()
    }

    private fun setupObservers() {
        viewModel.user?.observe(viewLifecycleOwner, Observer {
            if (it?.program != null && it.program != 0) {
                viewModel.getSubjects(it.program, 1)
            }
        })
        viewModel.subjects?.observe(viewLifecycleOwner, Observer {
            if (it != null && viewModel.firstCall) {
                if (it.isNotEmpty()) {
                    binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(it, this)
                } else {
                    val emptySubjectList: MutableList<Subject> = mutableListOf()
                    emptySubjectList.add(Subject(1, "-", "-", 1, 1, "-"))
                    binding.attendanceRecycler.adapter = AttendanceRecyclerAdapter(emptySubjectList, this)
                }
            } else
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
            }
        })
    }
}