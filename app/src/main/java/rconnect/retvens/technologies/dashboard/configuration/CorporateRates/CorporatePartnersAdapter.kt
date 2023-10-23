package rconnect.retvens.technologies.dashboard.configuration.CorporateRates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class CorporatePartnersAdapter(val list:ArrayList<CorporatesData>, val applicationContext: Context):RecyclerView.Adapter<CorporatePartnersAdapter.NotificationHolder>() {

    private  var list2:ArrayList<String> = ArrayList()

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val company:TextView = itemView.findViewById(R.id.company)

//        val recyclerView = itemView.findViewById<RecyclerView>(R.id.nestedroomDetails_recycler);
//        val add = itemView.findViewById<ImageView>(R.id.addBaseAdult);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_corporates_partners,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.company.text = list[position].company
    }
}