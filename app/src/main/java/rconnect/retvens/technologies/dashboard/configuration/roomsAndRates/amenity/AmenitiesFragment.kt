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
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.databinding.FragmentAmenitiesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AmenitiesFragment : Fragment() {
    private lateinit var binding: FragmentAmenitiesBinding

    val amenities = ArrayList<AmenitiesIconDataClass>()
    val amenitiesType = ArrayList<String>()

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

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val amenityTypeET = dialog.findViewById<TextInputEditText>(R.id.amenityTypeET)

        val amenitiesIconRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesIconRecycler)
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, amenitiesType)
        // Set a click listener for the end icon
        amenityTypeET.setOnClickListener {
            showDropdownMenu(adapter, it, amenityTypeET)
        }

        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))

        val amenitiesIconAdapter = AmenitiesIconAdapter(requireContext(), amenities)
        amenitiesIconRecycler.adapter = amenitiesIconAdapter

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
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
}