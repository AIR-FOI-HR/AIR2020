package hr.foi.academiclifestyle.ui.myclasses.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.database.model.Subject

class AttendanceRecyclerAdapter(private val subjectList: List<Subject>, private val listener: OnItemClickListener) : RecyclerView.Adapter<AttendanceRecyclerAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val txtSubjectName: TextView = itemView.findViewById(R.id.txtSubjectName)
        val txtPercentage: TextView = itemView.findViewById(R.id.txtPercentage)
        val attendanceRow: TableRow = itemView.findViewById(R.id.attendanceRow)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    // this is implemented in the attendance fragment
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendance_item, parent, false)

        return AttendanceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val currentItem = subjectList[position]

        holder.txtSubjectName.text = currentItem.name
        holder.txtPercentage.text = currentItem.percentage
        if (position == 0 || position % 2 == 0) {
            holder.attendanceRow.setBackgroundColor(ContextCompat.getColor(holder.txtSubjectName.context, R.color.grey_10))
        } else {
            holder.attendanceRow.setBackgroundColor(ContextCompat.getColor(holder.txtSubjectName.context, R.color.grey_20))
        }
    }

    override fun getItemCount() = subjectList.size
}