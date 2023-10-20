package rconnect.retvens.technologies.dashboard.configuration.billings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class PaymentTypeAdapter(val list:ArrayList<String>, val applicationContext: Context):RecyclerView.Adapter<PaymentTypeAdapter.NotificationHolder>() {


    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

//        val recyclerView = itemView.findViewById<RecyclerView>(R.id.nestedroomDetails_recycler);
//        val add = itemView.findViewById<ImageView>(R.id.addBaseAdult);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_payment_type,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {

    }
}