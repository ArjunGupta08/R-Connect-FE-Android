package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
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
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.CreateAmenityDialog
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddAmenitiesDialog(var selectedAmenitiesList: ArrayList<GetAmenityData>, private var isAddingForRoom : Boolean ?= false) : DialogFragment(), AddAmenitiesAdapter.OnItemClick, CreateAmenityDialog.OnAmenitySave {

    private var mListener: OnAmenityAdd? = null
    fun setOnAddAmenityDialogListener(listener: OnAmenityAdd) {
        mListener = listener
    }

    interface OnAmenityAdd {
        fun onAmenityAdd(selectedAmenitiesList: ArrayList<GetAmenityData>)
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var amenitiesRecycler : RecyclerView

    lateinit var progressDialog: Dialog
    private var mLastClickTime : Long = 0

    private lateinit var search : EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_add_amenities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search = view.findViewById(R.id.search)

        val createNewAmenityBtn = view.findViewById<TextView>(R.id.createNewAmenityBtn)
        createNewAmenityBtn.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = CreateAmenityDialog()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
            openDialog.setOnAmenityDialogListener(this)
        }

        val cancel = view.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dismiss()
        }

        amenitiesRecycler = view.findViewById<RecyclerView>(R.id.amenitiesRecycler)
        amenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 5)

        if (isAddingForRoom!!) {
            createNewAmenityBtn.visibility = View.VISIBLE
            getRoomAmenityRecycler()
        } else {
            createNewAmenityBtn.visibility = View.GONE
            getPropertyAmenityRecycler()
        }

        val saveBtn = view.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            dismiss()
        }

    }

    private fun getPropertyAmenityRecycler() {
        val propertyId =UserSessionManager(requireContext()).getPropertyId().toString()
        Toast.makeText(requireContext(), propertyId, Toast.LENGTH_SHORT).show()
        progressDialog = showProgressDialog(requireContext())
//        "150860"
        val getAmenity = RetrofitObject.getGeneralsAPI.getAmenityApi(propertyId)
        getAmenity.enqueue(object : Callback<AmenityDataClass?> {
            override fun onResponse(
                call: Call<AmenityDataClass?>,
                response: Response<AmenityDataClass?>
            ) {

                if (response.isSuccessful) {
                    if (isAdded) {
                        try {
                            val data = response.body()!!.data
                            selectedAmenitiesList!!.forEach {
                                if (data.contains(it)){
                                    data.remove(it)
                                }
                            }
                            val addAmenitiesAdapter = AddAmenitiesAdapter(requireContext(),data)
                            amenitiesRecycler.adapter = addAmenitiesAdapter
                            addAmenitiesAdapter.notifyDataSetChanged()
                            addAmenitiesAdapter.setOnClickListener(this@AddAmenitiesDialog)

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
                                    val filterData = data.filter {
                                        it.amenityName.contains(s.toString(), true)
                                    }
                                    addAmenitiesAdapter.filterList(filterData as ArrayList<GetAmenityData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<AmenityDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error" , t.localizedMessage)
            }
        })

    }

    private fun getRoomAmenityRecycler() {
        val propertyId =UserSessionManager(requireContext()).getPropertyId().toString()
        Toast.makeText(requireContext(), propertyId, Toast.LENGTH_SHORT).show()
        progressDialog = showProgressDialog(requireContext())
//        "150860"
        val getAmenity = RetrofitObject.getGeneralsAPI.getRoomAmenityApi(propertyId)
        getAmenity.enqueue(object : Callback<AmenityDataClass?> {
            override fun onResponse(
                call: Call<AmenityDataClass?>,
                response: Response<AmenityDataClass?>
            ) {

                if (response.isSuccessful) {
                    if (isAdded) {
                        try {
                            val data = response.body()!!.data
                            selectedAmenitiesList.forEach {
                                if (data.contains(it)){
                                    data.remove(it)
                                }
                            }
                            val addAmenitiesAdapter = AddAmenitiesAdapter(requireContext(),data)
                            amenitiesRecycler.adapter = addAmenitiesAdapter
                            addAmenitiesAdapter.notifyDataSetChanged()
                            addAmenitiesAdapter.setOnClickListener(this@AddAmenitiesDialog)

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
                                    val filterData = data.filter {
                                        it.amenityName.contains(s.toString(), true)
                                    }
                                    addAmenitiesAdapter.filterList(filterData as ArrayList<GetAmenityData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<AmenityDataClass?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error" , t.localizedMessage)
            }
        })

    }

    override fun onItemListUpdate(selectedAmenitiesList: ArrayList<GetAmenityData>) {
        mListener?.onAmenityAdd(selectedAmenitiesList)
    }

    override fun onAmenitySave() {
        if (isAddingForRoom!!) {
            getRoomAmenityRecycler()
        } else {
            getPropertyAmenityRecycler()
        }
    }

}