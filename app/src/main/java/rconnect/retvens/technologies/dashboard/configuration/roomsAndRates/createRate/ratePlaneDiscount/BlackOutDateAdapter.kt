package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

import android.app.Dialog
import android.app.FragmentManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.DescendantOffsetUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddInclusionsAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.AddBarsRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.CreateInclusionDialogSheet
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetChargeRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleArray
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlackOutDateAdapter(val applicationContext:Context, val list:ArrayList<String>):RecyclerView.Adapter<BlackOutDateAdapter.ViewHolder>(){

    private var onBlackOutDateListChangeListener : OnBlackOutDateListChangeListener ?= null
    fun setOnBlackOutDateListUpdateListener (listener : OnBlackOutDateListChangeListener) {
        onBlackOutDateListChangeListener = listener
    }
    interface OnBlackOutDateListChangeListener {
        fun onBlackOutDateDelete(position: Int)
    }

    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {

        val date = itemView.findViewById<TextView>(R.id.date)
        val remove = itemView.findViewById<ImageView>(R.id.remove)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_date,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val currentData = list[position]
        holder.date.text = currentData

        holder.remove.setOnClickListener {
            onBlackOutDateListChangeListener?.onBlackOutDateDelete(position)
            notifyDataSetChanged()
        }
    }
}