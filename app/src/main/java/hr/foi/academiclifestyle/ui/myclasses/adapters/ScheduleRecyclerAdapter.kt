package hr.foi.academiclifestyle.ui.myclasses.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.foi.academiclifestyle.R
import hr.foi.academiclifestyle.dimens.ScheduleEvent

class ScheduleRecyclerAdapter(private val eventList: List<ScheduleEvent>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder>() {

    inner class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val imgStatus: ImageView = itemView.findViewById(R.id.imgStatus)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, eventList[position])
            }
        }
    }

    // this is implemented in the schedule fragment
    interface OnItemClickListener {
        fun onItemClick(position: Int, event: ScheduleEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item, parent, false)

        return ScheduleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val currentItem = eventList[position]

        holder.txtName.text = currentItem.name
        holder.txtTime.text = currentItem.time
        when (currentItem.status) {
            1 -> holder.imgStatus.setImageResource(R.drawable.ic_check)
            2 -> holder.imgStatus.setImageResource(R.drawable.ic_cross)
            else -> holder.imgStatus.setImageResource(0)
        }
    }

    override fun getItemCount() = eventList.size
}