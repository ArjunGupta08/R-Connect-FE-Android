package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showDropdownMenuWithListener

class CreateRateTypeAdapter(val applicationContext:Context,val rateTypeList:ArrayList<InclusionPlan>,val postingRuleArray:ArrayList<String>,val chargeRuleArray:ArrayList<String>):RecyclerView.Adapter<CreateRateTypeAdapter.ViewHolder>() {

    private var mListener : OnInclusionChange ?= null
    fun setOnInclusionChangeListener(listener : OnInclusionChange){
        mListener = listener
    }
    interface OnInclusionChange{
        fun onInclusionDelete(item : InclusionPlan)
        fun onInclusionUpdate(updatedInclusionList : ArrayList<InclusionPlan>)
        fun onInclusionPriceUpdate(position : Int, price : String)

    }
    class ViewHolder(val itemView:View):RecyclerView.ViewHolder(itemView) {
        val inclusion:TextInputEditText = itemView.findViewById(R.id.inclusion)
        val posting:TextInputEditText = itemView.findViewById(R.id.posting)
        val charge:TextInputEditText = itemView.findViewById(R.id.charge)
        val rate:TextInputEditText = itemView.findViewById(R.id.rate)

        val img_del:ImageView = itemView.findViewById(R.id.img_del)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(applicationContext)
        val view = inflater.inflate(R.layout.item_inclusion_and_charges,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateTypeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = rateTypeList[position]
        holder.rate.setText(currentData.rate)
        holder.charge.setText(currentData.chargeRule)
        holder.inclusion.setText(currentData.inclusionName)
        holder.posting.setText(currentData.postingRule)

        holder.rate.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                // Handle the "Done" action here
                if (holder.rate.text!!.isNotEmpty() && holder.rate.text.toString() != ".") {
                    currentData.rate = holder.rate.text.toString()
                    mListener?.onInclusionPriceUpdate(position, holder.rate.text.toString())

                    val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(holder.rate.windowToken, 0)
                }
                return@OnEditorActionListener true
            }
            false
        })

        holder.posting.setOnClickListener {
            showDropdownMenuWithListener(applicationContext, it, postingRuleArray){selectedItem ->
                holder.posting.setText(selectedItem)
                currentData.postingRule = holder.posting.text.toString()
                mListener?.onInclusionUpdate(rateTypeList)
            }
        }

        holder.charge.setOnClickListener {
            showDropdownMenuWithListener(applicationContext,it, chargeRuleArray) {selectedItem ->
                holder.charge.setText(selectedItem)
                currentData.chargeRule = holder.charge.text.toString()
                mListener?.onInclusionUpdate(rateTypeList)
            }
        }

        holder.img_del.setOnClickListener {
            mListener?.onInclusionDelete(currentData)
            notifyDataSetChanged()
        }
    }
}