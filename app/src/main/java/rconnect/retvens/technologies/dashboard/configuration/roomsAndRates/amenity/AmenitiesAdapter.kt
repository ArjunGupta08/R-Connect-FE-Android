package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.AlertDialog

class AmenitiesAdapter(val list:ArrayList<FetchAmenitiesData>, val applicationContext: Context):RecyclerView.Adapter<AmenitiesAdapter.NotificationHolder>() {
    lateinit var loader:Dialog

    var mListener : OnItemUpdate ?= null
    fun setOnItemUpdateListener(listener : OnItemUpdate){
        mListener = listener
    }
    interface OnItemUpdate {
        fun onUpdate(currentItem : FetchAmenitiesData)
    }

    class NotificationHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val shortCode = itemView.findViewById<TextView>(R.id.shortCode)
        val text2 = itemView.findViewById<TextView>(R.id.text2)
        val text3 = itemView.findViewById<TextView>(R.id.text3)
        val text4 = itemView.findViewById<TextView>(R.id.text4)
        val text5 = itemView.findViewById<TextView>(R.id.text5)
        val lastModified = itemView.findViewById<TextView>(R.id.text6)

        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)
        val info = itemView.findViewById<ImageView>(R.id.info)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_holidays,parent,false)
        return NotificationHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        val item = list[position]

        holder.shortCode.text = item.shortCode
        holder.text2.text = item.amenityName
        holder.text3.text = item.amenityType
        holder.text4.isVisible = false
        holder.text5.text = "${item.createdBy} ${item.createdOn}"
        holder.lastModified.text = "${item.modifiedBy} ${item.modifiedOn}"

        holder.edit.setOnClickListener {
            //show alert dialog here...
            mListener?.onUpdate(item)
        }
        holder.delete.setOnClickListener {
            showDeleteConfirmationDialog(applicationContext){
//                onDeleteItem.invoke(position)
                deleteAmenity(item)
            }
        }
    }
    private fun deleteAmenity(fetchAmenities: FetchAmenitiesData) {

        loader = showProgressDialog(this.applicationContext)

        val get = OAuthClient<GeneralsAPI>(this.applicationContext).create(GeneralsAPI::class.java).deleteAmenityApi(
            UserSessionManager(this.applicationContext).getUserId().toString(),
            fetchAmenities.amenityId,
            DisplayStatusData("0")
        )
        get.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loader.dismiss()
                if(response.isSuccessful){
                    Log.e("list",response.body().toString())
                    Log.e("response",response.message().toString())
                    Toast.makeText(applicationContext, response.message().toString(), Toast.LENGTH_SHORT).show()
                    list.remove(fetchAmenities)
                    notifyDataSetChanged()
                }
                else{
                    Toast.makeText(applicationContext, response.message().toString(), Toast.LENGTH_SHORT).show()
                    Log.e("else response",response.message().toString())
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.e("failure",t.message.toString())
                loader.dismiss()
            }
        })
    }

    fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Confirm Deletion")
        alertDialogBuilder.setMessage("Do you really want to delete this item?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // User clicked "Yes"
            onDeleteConfirmed.invoke()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // User clicked "No"
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }




}