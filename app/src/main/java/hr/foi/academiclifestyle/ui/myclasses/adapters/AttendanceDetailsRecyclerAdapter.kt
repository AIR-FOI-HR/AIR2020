package hr.foi.academiclifestyle.ui.myclasses.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.dimens.AttendanceDetails

class AttendanceDetailsRecyclerAdapter(private val detailsList: List<AttendanceDetails>) : RecyclerView.Adapter<AttendanceDetailsRecyclerAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtEventType: TextView = itemView.findViewById(R.id.txtEventType)
        val txtMyAttend: TextView = itemView.findViewById(R.id.txtMyAttend)
        val txtMinAttend: TextView = itemView.findViewById(R.id.txtMinAttend)
        val attendanceDetailsRow: TableRow = itemView.findViewById(R.id.attendanceDetailsRow)

        init { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.attendance_details_item, parent, false)

        return AttendanceViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val currentItem = detailsList[position]

        holder.txtEventType.text = currentItem.eventType

        var userAttendPercentage = 0
        if (currentItem.userAttend > 0 && currentItem.maxAttend > 0) {
            userAttendPercentage = ((currentItem.userAttend.toDouble()/currentItem.maxAttend)*100).toInt()
        }
        holder.txtMyAttend.text = currentItem.userAttend.toString() + "/" + currentItem.maxAttend.toString() + " (" + userAttendPercentage + "%)"

        var minAttendPercentage = 0
        if (currentItem.minAttend > 0 && currentItem.maxAttend > 0) {
            minAttendPercentage = ((currentItem.minAttend.toDouble()/currentItem.maxAttend)*100).toInt()
        }
        holder.txtMinAttend.text = "$minAttendPercentage%"

        if (position == 0 || position % 2 == 0) {
            holder.attendanceDetailsRow.setBackgroundColor(ContextCompat.getColor(holder.txtEventType.context, R.color.grey_10))
        } else {
            holder.attendanceDetailsRow.setBackgroundColor(ContextCompat.getColor(holder.txtEventType.context, R.color.grey_20))
        }

        if (userAttendPercentage < minAttendPercentage || minAttendPercentage == 0) {
            holder.txtEventType.setTextColor(ContextCompat.getColor(holder.txtEventType.context, R.color.red_acc))
            holder.txtMyAttend.setTextColor(ContextCompat.getColor(holder.txtEventType.context, R.color.red_acc))
            holder.txtMinAttend.setTextColor(ContextCompat.getColor(holder.txtEventType.context, R.color.red_acc))
        }
    }

    override fun getItemCount() = detailsList.size
}