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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.onboarding.authentication.DesignationDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddBedTypeAdapter(val context: Context, private val itemList: List<String>) : RecyclerView.Adapter<AddBedTypeAdapter.ViewHolder>() {

    val bedIdList = ArrayList<AddBedTypeData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_input_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        selectBedType(context, holder.textInputEditText)

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
        val textInputEditText = itemView.findViewById<AutoCompleteTextView>(R.id.textInputEditText)
    }

    private fun selectBedType(context: Context, textV : AutoCompleteTextView) {

        val suggestionList = ArrayList<String>()
        suggestionList.add("King")
        suggestionList.add("Queen")
        suggestionList.add("Single")
        suggestionList.add("Double")

        // Initialize the AutoCompleteTextView and adapter
        val autoCompleteTextView = textV
        val adapter = ArrayAdapter<String>(context, R.layout.simple_dropdown_item_1line)
        autoCompleteTextView.setAdapter(adapter)

        // Set up the Autocomplete request
        autoCompleteTextView.threshold = 1  // Minimum characters to start autocomplete
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedText = adapter.getItem(position).toString()
            bedIdList.add(AddBedTypeData(selectedText))
            // Handle the selected address as needed
            autoCompleteTextView.setText(selectedText)
        }

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Perform the autocomplete request

                // Update the adapter with the new suggestions
                adapter.clear()
                adapter.addAll(suggestionList)
                autoCompleteTextView.setAdapter(adapter)
                adapter.notifyDataSetChanged()

           }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

}