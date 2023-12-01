package rconnect.retvens.technologies.dashboard.configuration.CorporateRates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.Company
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.ViewCompanyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan

class CorporatePartnersAdapter(val list:ArrayList<Company>, val applicationContext: Context):RecyclerView.Adapter<CorporatePartnersAdapter.NotificationHolder>() {

    private var mListener : OnInclusionChange ?= null
    fun setOnInclusionChangeListener(listener : OnInclusionChange){
        mListener = listener
    }
    interface OnInclusionChange{
        fun onClick(companyId:String)

    }
    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val company:TextView = itemView.findViewById(R.id.company)
        val contactPerson:TextView = itemView.findViewById(R.id.personName)
        val ratePlan:TextView = itemView.findViewById(R.id.ratePlanCount)
        val expiration:TextView = itemView.findViewById(R.id.expiration)
        val ledgerBalance:TextView = itemView.findViewById(R.id.balance)
        val view:TextView = itemView.findViewById(R.id.plans)
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
        val currentData = list[position]

        holder.company.text = currentData.companyName
        holder.contactPerson.text = currentData.contactPerson
        holder.ratePlan.text = currentData.ratePlans.toString()
        holder.expiration.text = currentData.expiration

        holder.view.setOnClickListener {
            mListener?.onClick(currentData.companyId)
        }


    }
}