package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class AddReservationAdapter(val applicationContext: Context,val reservationList:ArrayList<String>):RecyclerView.Adapter<AddReservationAdapter.ReservationHolder>() {
    var cc = 1
    var ac = 1
    var isCharge = false
    class ReservationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val x:TextView = itemView.findViewById(R.id.total_price)
        val delete:ImageView = itemView.findViewById(R.id.img_del)
        val room_count:TextView = itemView.findViewById(R.id.item_room_count)
        val addChild:ImageView = itemView.findViewById(R.id.addChild)
        val removeChild:ImageView = itemView.findViewById(R.id.removeChild)
        val countChild:TextView = itemView.findViewById(R.id.item_child_count)
        val addAdult:ImageView = itemView.findViewById(R.id.addAdult)
        val removeAdult:ImageView = itemView.findViewById(R.id.removeAdult)
        val countAdult:TextView = itemView.findViewById(R.id.item_adult_count)
        val drop:ImageView = itemView.findViewById(R.id.drop)
        val chargesDetails:ConstraintLayout = itemView.findViewById(R.id.charges_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationHolder {
        return ReservationHolder(LayoutInflater.from(applicationContext).inflate(R.layout.item_recycler_room_details,parent,false))
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    override fun onBindViewHolder(holder: ReservationHolder, position: Int) {
        holder.room_count.text = "Room ${reservationList.size}"
        holder.delete.setOnClickListener {
            reservationList.remove(reservationList[position])
            notifyDataSetChanged()
        }
        holder.addChild.setOnClickListener {
            cc++
            holder.countChild.text = "$cc"
        }
        holder.removeChild.setOnClickListener {
            if (cc>1){
                cc--
                holder.countChild.text = "$cc"
            }
        }
        holder.addAdult.setOnClickListener {
            ac++
            holder.countAdult.text = "$ac"
        }
        holder.removeAdult.setOnClickListener {
            if (ac>1) {
                ac--
                holder.countAdult.text = "$ac"
            }
        }
        holder.drop.setOnClickListener {
            if (isCharge){
                holder.chargesDetails.isVisible = false
                holder.drop.rotation = 0f
                isCharge = false
            }
            else{
                holder.chargesDetails.isVisible = true
                holder.drop.rotation = 180f
                isCharge= true
            }
        }
    }
}