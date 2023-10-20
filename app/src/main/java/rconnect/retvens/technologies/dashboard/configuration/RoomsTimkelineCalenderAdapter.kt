package rconnect.retvens.technologies.dashboard.configuration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomsTimkelineCalenderAdapter :
    RecyclerView.Adapter<RoomsTimkelineCalenderAdapter.MyViewHolder>() {
    private val calendarList = ArrayList<Calendar>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(calendar: Calendar) {
            val calendarDay = itemView.findViewById<TextView>(R.id.tv_calendar_day)
            val calendarMonth = itemView.findViewById<TextView>(R.id.tv_calendar_month)

            val dayAbbreviation = SimpleDateFormat("EEE", Locale.ENGLISH).format(calendar.time)
            val date = SimpleDateFormat("dd", Locale.ENGLISH).format(calendar.time)
            val month = SimpleDateFormat("MMM", Locale.ENGLISH).format(calendar.time)

            calendarDay.text = "$date $month"
            calendarMonth.text = dayAbbreviation


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calender_view_timeline, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(calendarList[position])
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    fun setData(calendarList: List<Calendar>) {
        this.calendarList.clear()
        this.calendarList.addAll(calendarList)
        notifyDataSetChanged()
    }
}