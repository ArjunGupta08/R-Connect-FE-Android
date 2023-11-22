package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAmenityDialog(
    val currentItem : FetchAmenitiesData ?= FetchAmenitiesData(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
) : DialogFragment(), AmenitiesIconAdapter.OnIconClick {

    private var mListener: OnAmenitySave? = null
    fun setOnAmenityDialogListener(listener: OnAmenitySave) {
        mListener = listener
    }

    interface OnAmenitySave {
        fun onAmenitySave()
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    val amenitiesType = ArrayList<String>()
    var amenityIconLink = ""

    lateinit var progressDialog: Dialog

    private lateinit var search : EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_create_amenities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amenityNameLayout = view.findViewById<TextInputLayout>(R.id.amenityNameLayout)
        val shortCodeLayout = view.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val amenityTypeLayout = view.findViewById<TextInputLayout>(R.id.amenityTypeLayout)

        val amenityNameET = view.findViewById<TextInputEditText>(R.id.amenityNameET)

        search = view.findViewById<EditText>(R.id.search)

        val shortCodeET = view.findViewById<TextInputEditText>(R.id.shortCodeET)
        val amenityTypeET = view.findViewById<TextInputEditText>(R.id.amenityTypeET)
        val switchBtn = view.findViewById<Switch>(R.id.switchBtn)

        getAmenityType()

        val amenitiesIconRecycler = view.findViewById<RecyclerView>(R.id.amenitiesIconRecycler)
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 8)

        amenityNameET.doAfterTextChanged {
            if (amenityNameET.text!!.length > 3) {
                shortCodeET.setText(generateShortCode(amenityNameET.text.toString()))
            }
        }

        amenityTypeET.setOnClickListener {
            showDropdownMenu(requireContext(), amenityTypeET, it, amenitiesType)
        }

        progressDialog = showProgressDialog(requireContext())

        getAmenityIcon(amenitiesIconRecycler)

        if (currentItem!!.amenityId != "") {
            shortCodeET.setText(currentItem.shortCode)
            amenityNameET.setText(currentItem.amenityName)
            amenityTypeET.setText(currentItem.amenityType)
            switchBtn.isChecked = currentItem.amenityIcon == "true"
            Toast.makeText(requireContext(), currentItem.amenityIconLink.toString(), Toast.LENGTH_SHORT).show()
            amenityIconLink = currentItem.amenityIconLink
        }

        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            if (amenityNameET.text!!.isEmpty()) {
                shakeAnimation(amenityNameLayout, requireContext())
            } else if (shortCodeET.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, requireContext())
            } else if (amenityTypeET.text!!.isEmpty()) {
                shakeAnimation(amenityTypeLayout, requireContext())
            } else if (amenityIconLink == "") {
                shakeAnimation(amenitiesIconRecycler, requireContext())
//                Toast.makeText(requireContext(), "Please Select Icon", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show()
                if (currentItem.amenityId != "") {
                    updateData(
                        amenityNameET.text.toString(),
                        shortCodeET.text.toString(),
                        amenityTypeET.text.toString(),
                        switchBtn.isChecked
                    )
                } else {
                    saveData(
                        amenityNameET.text.toString(),
                        shortCodeET.text.toString(),
                        amenityTypeET.text.toString(),
                        switchBtn.isChecked
                    )
                }
            }
        }
        val cancel = view.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dismiss()
        }
 }

    private fun getAmenityIcon(amenitiesIconRecycler : RecyclerView) {
        val amenityI = RetrofitObject.getGeneralsAPI.getAmenityIconApi()
        amenityI.enqueue(object : Callback<GetAmenityIcon?> {
            override fun onResponse(
                call: Call<GetAmenityIcon?>,
                response: Response<GetAmenityIcon?>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    if (isAdded) {
                        try {
                            val data = response.body()!!.data
                            Log.d("error", response.message())
                            Log.d("error", data.toString())
                            val amenitiesIconAdapter =
                                AmenitiesIconAdapter(requireContext(), data)
                            amenitiesIconRecycler.adapter = amenitiesIconAdapter
                            amenitiesIconAdapter.setOnIconClick(this@CreateAmenityDialog)

                            search.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {
                                    val filteredData = data.filter {
                                        it.amenityIconTags.any { tags ->
                                            tags.contains(s.toString(), true)
                                        }
                                    }
                                    amenitiesIconAdapter.filterList(filteredData as ArrayList<GetAmenityIconData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Log.d("error", response.code().toString())
                }
            }

            override fun onFailure(call: Call<GetAmenityIcon?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun saveData(amenityName: String, shortCode: String, amenityType: String, amenityIconCheck: Boolean) {
        val send = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).postAmenityApi(
            PostAmenityData(UserSessionManager(requireContext()).getUserId().toString(), shortCode, amenityName, UserSessionManager(requireContext()).getPropertyId().toString(), amenityType, "$amenityIconCheck", amenityIconLink)
        )
        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                dismiss()
                progressDialog.dismiss()
                if (response.isSuccessful){
                    mListener?.onAmenitySave()
                    Log.d("response", response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                dismiss()
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun updateData(amenityName: String, shortCode: String, amenityType: String, amenityIconCheck: Boolean) {
        val send = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).patchAmenityApi(
            UserSessionManager(requireContext()).getUserId().toString(),
            currentItem!!.amenityId,
            PatchAmenityData(
                amenityName,
                amenityType,
                "$amenityIconCheck",
                amenityIconLink
            )
        )
        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                dismiss()
                progressDialog.dismiss()
                if (response.isSuccessful){
                    mListener?.onAmenitySave()
                    Log.d("response", response.body()!!.message)
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                dismiss()
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun getAmenityType() {

        val getAmenityType = RetrofitObject.dropDownApis.getAmenityType()
        getAmenityType.enqueue(object : Callback<GetAmenityType?> {
            override fun onResponse(
                call: Call<GetAmenityType?>,
                response: Response<GetAmenityType?>
            ) {
                if (response.isSuccessful){
                    val data = response.body()!!.data
                    data.forEach {
                        amenitiesType.add(it.amenityType)
                    }
                } else {
                    Log.d("error", response.code().toString())
                }
            }
            override fun onFailure(call: Call<GetAmenityType?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun getIconLinkOnClick(amenityIconLinkUrl: String) {
        amenityIconLink = amenityIconLinkUrl
    }
}