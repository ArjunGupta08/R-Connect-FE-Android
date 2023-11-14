package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.databinding.FragmentAmenitiesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AmenitiesFragment : Fragment(), AmenitiesIconAdapter.OnIconClick {
    private lateinit var binding: FragmentAmenitiesBinding

    val amenities = ArrayList<AmenitiesIconDataClass>()
    val amenitiesType = ArrayList<String>()
    var amenityIconLink = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAmenitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNewBtn.setOnClickListener {
            openCreateNewAmenityDialog()
        }

        getAmenityType()
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
    private fun openCreateNewAmenityDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_amenities)

        val amenityNameLayout = dialog.findViewById<TextInputLayout>(R.id.amenityNameLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)
        val amenityTypeLayout = dialog.findViewById<TextInputLayout>(R.id.amenityTypeLayout)

        val amenityNameET = dialog.findViewById<TextInputEditText>(R.id.amenityNameET)
        val shortCodeET = dialog.findViewById<TextInputEditText>(R.id.shortCodeET)
        val amenityTypeET = dialog.findViewById<TextInputEditText>(R.id.amenityTypeET)
        val switchBtn = dialog.findViewById<Switch>(R.id.switchBtn)

        val amenitiesIconRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesIconRecycler)
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 8)

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, amenitiesType)
        // Set a click listener for the end icon
        amenityTypeET.setOnClickListener {
            showDropdownMenu(adapter, it, amenityTypeET)
        }

        val amenityI = RetrofitObject.getGeneralsAPI.getAmenityIconApi()
        amenityI.enqueue(object : Callback<GetAmenityIcon?> {
            override fun onResponse(
                call: Call<GetAmenityIcon?>,
                response: Response<GetAmenityIcon?>
            ) {
                if (response.isSuccessful) {
                    Log.d(this@AmenitiesFragment.activity?.localClassName, response.message())
                    val amenitiesIconAdapter = AmenitiesIconAdapter(requireContext(), response.body()!!.data)
                    amenitiesIconRecycler.adapter = amenitiesIconAdapter
                    amenitiesIconAdapter.setOnIconClick(this@AmenitiesFragment)
                } else {
                    Log.d("error", response.code().toString())
                }
            }

            override fun onFailure(call: Call<GetAmenityIcon?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })

        val saveBtn = dialog.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            if (amenityNameET.text!!.isEmpty()) {
                shakeAnimation(amenityNameLayout, requireContext())
            } else if (shortCodeET.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, requireContext())
            } else if (amenityTypeET.text!!.isEmpty()) {
                shakeAnimation(amenityTypeLayout, requireContext())
            } else if (amenityIconLink == "") {
                Toast.makeText(requireContext(), "Please Select Icon", Toast.LENGTH_SHORT).show()
            } else {
                saveData(
                    amenityNameET.text.toString(),
                    shortCodeET.text.toString(),
                    amenityTypeET.text.toString(),
                    switchBtn.isChecked,
                    dialog
                )
            }
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun saveData(amenityName: String, shortCode: String, amenityType: String, amenityIconCheck: Boolean, dialog: Dialog) {
        val send = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).postAmenityApi(
            PostAmenityData(UserSessionManager(requireContext()).getUserId().toString(), shortCode, amenityName, UserSessionManager(requireContext()).getPropertyId().toString(), amenityType, "$amenityIconCheck", amenityIconLink)
        )
        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (response.isSuccessful){
                            Toast.makeText(requireContext(), "$amenityIconCheck", Toast.LENGTH_SHORT).show()
                    Log.d("response", response.body()!!.message)
                    dialog.dismiss()
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun showDropdownMenu(adapter: ArrayAdapter<String>, anchorView: View, et : TextInputEditText) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            et.setText(selectedItem)
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
    }

    override fun getIconLinkOnClick(amenityIconLinkUrl: String) {
        amenityIconLink = amenityIconLinkUrl
//        Toast.makeText(requireContext(), "$amenityIconLink", Toast.LENGTH_SHORT).show()
    }
}