package hr.foi.academiclifestyle.ui.myclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.databinding.FragmentMyclassesAttendanceBinding

class AttendanceFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMyclassesAttendanceBinding>(inflater, R.layout.fragment_myclasses_attendance, container, false)

        binding.btnRightSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "1") {
                txtSemester.text = "2"
            }
        }
        binding.btnLeftSem.setOnClickListener() {
            val txtSemester: TextView = binding.txtSemNumber
            if (txtSemester.text == "2") {
                txtSemester.text = "1"
            }
        }
        return binding.root
    }
}