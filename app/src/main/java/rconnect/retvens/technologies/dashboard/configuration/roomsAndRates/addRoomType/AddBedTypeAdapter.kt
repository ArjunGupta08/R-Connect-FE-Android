package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.authentication.DesignationDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBedTypeAdapter(val context: Context, private val itemList: List<String>, private val bedSuggestionList: List<String>) : RecyclerView.Adapter<AddBedTypeAdapter.ViewHolder>() {

    val bedTypeList = ArrayList<String>()

    var mListioner : BedTypeIdInterface ?= null
    fun setOnBedSelection(listener : BedTypeIdInterface){
        mListioner = listener
    }
    interface BedTypeIdInterface {
        fun bedSelected(bedTypeNameList : ArrayList<String>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_input_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.textInputEditText.setOnClickListener {
            val adapter = ArrayAdapter(context, R.layout.simple_spinner_item1, bedSuggestionList)
            showDropdownBedTypeMenu(adapter, it, holder.textInputEditText)
        }
        holder.textInputLayout.hint = "Bed $item Type"
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textInputLayout = itemView.findViewById<TextInputLayout>(R.id.textInputLayout)
        val textInputEditText = itemView.findViewById<TextInputEditText>(R.id.textInputEditText)
    }

    private fun showDropdownBedTypeMenu(adapter: ArrayAdapter<String>, anchorView: View, et : TextInputEditText) {
        val listPopupWindow = ListPopupWindow(context)
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            et.setText(selectedItem)
            if (!bedTypeList.contains(selectedItem)){
                bedTypeList.add(selectedItem.toString())
            }
            mListioner?.bedSelected(bedTypeList)
            listPopupWindow.dismiss()
        }
        listPopupWindow.show()
    }

}