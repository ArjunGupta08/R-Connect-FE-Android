package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.content.Context
import android.location.GnssAntennaInfo.Listener
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class ContractPDFAdapter(val list:ArrayList<ContractPDFData>, val applicationContext: Context):RecyclerView.Adapter<ContractPDFAdapter.NotificationHolder>() {

    var mListener : OnViewListener? = null

    fun setOnClickListener(listener : OnViewListener){
        mListener = listener
    }
    interface OnViewListener {
        fun onViewPdf(uri: Uri)
        fun onDeletePdf()

    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val pdfName = itemView.findViewById<TextView>(R.id.pdfName);
        val deletePdf = itemView.findViewById<LinearLayout>(R.id.deletePdf);
        val view = itemView.findViewById<LinearLayout>(R.id.view);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_selected_pdf,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val data = list[position]
        holder.pdfName.text = data.pdfName

        holder.view.setOnClickListener {
            mListener?.onViewPdf(data.pdfUri)
        }

        holder.deletePdf.setOnClickListener {
            list.remove(data)
            mListener?.onDeletePdf()
        }
    }
}