package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.SelectedAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityIcon
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.PostAmenityData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeFragment
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.rightInAnimation
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRoomTypeFragment : Fragment(),
    AddBedTypeAdapter.BedTypeIdInterface,
    AmenitiesIconAdapter.OnIconClick, AddAmenitiesAdapter.OnItemClick{

    private lateinit var binding:FragmentAddRoomTypeBinding

    private var bedTypeIds = ArrayList<String>()
    val bedTypeList = ArrayList<GetBedTypeDataClass>()
    val bedSuggestionList = ArrayList<String>()

    val amenityIdsList = ArrayList<String>()

    val amenities = ArrayList<AmenitiesIconDataClass>()
    val amenitiesType = ArrayList<String>()
    var amenityIconLink = ""

    private var page = 1
    private var bedCount = 1
    private var bedCountList = ArrayList<String>()
    private lateinit var addBedTypeAdapter: AddBedTypeAdapter

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    // Occupancy
    private var baseAdultCount = 1
    private var baseChildCount = 0
    private var maxAdultCount = 1
    private var maxChildCount = 1
    private var maxOccupancyCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddRoomTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        binding.continueBtn.setOnClickListener {

            if (page == 1){

                if (binding.roomTypeNameEt.text!!.isEmpty()){
                    shakeAnimation(binding.roomTypeNameLayout, requireContext())
                } else if (binding.shortCodeET.text!!.isEmpty()){
                    shakeAnimation(binding.shortCodeLayout, requireContext())
                } else if (binding.bed1TypeET.text!!.isEmpty()){
                    shakeAnimation(binding.bed1TypeLayout, requireContext())
                } else if (amenityIdsList.isEmpty()){
                    shakeAnimation(binding.add, requireContext())
                } else {
                    sendData()
                }

            } else if (page == 2){

                page = 3

                binding.chargePlans.textSize = 18.0f
                binding.chargePlans.typeface = robotoMedium
                binding.chargePlans.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.roomImages.textSize = 16.0f
                binding.roomImages.typeface = roboto

                binding.roomProfile.textSize = 16.0f
                binding.roomProfile.typeface = roboto

                binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

                val childFragment: Fragment = ChargesAndRatesFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.createRoomFragContainer,childFragment)
                transaction.commit()

                binding.frame.visibility = View.GONE
                binding.createRoomFragContainer.visibility = View.VISIBLE
                rightInAnimation(binding.createRoomFragContainer, requireContext())

                binding.continueTxt.text = "Save"
            } else {
//                sendData()
            }
        }

        binding.cancel.setOnClickListener {

            if (Const.isAddingNewRoom){

                Const.isAddingNewRoom = false
                val childFragment: Fragment = RoomTypeFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                transaction.commit()

            } else {

                Const.isAddingNewRoom = true

                val welcomeLayout = requireActivity().findViewById<LinearLayout>(R.id.welcomeLayout)
                welcomeLayout.visibility = View.VISIBLE

                val dashboardFragmentContainer =
                    requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)
                dashboardFragmentContainer.visibility = View.GONE
            }
        }

        handleCounts()

        binding.bed1TypeET.setOnClickListener {
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, bedSuggestionList)
            showDropdownBedTypeMenu(adapter, it, binding.bed1TypeET)
        }
        binding.bedTypeRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.add.setOnClickListener { openAddAmenitiesDialog() }

        getBedType()
    }

    fun sendData(){
        val send = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).createRoomApi(
            CreateRoomData(
                UserSessionManager(requireContext()).getUserId().toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                binding.baseAdultText.text.toString(),
                binding.baseChildText.text.toString(),
                binding.shortCodeET.text.toString(),
                binding.roomDescriptionEt.text.toString(),
                binding.roomTypeNameEt.text.toString(),
                binding.maxAdultText.text.toString(),
                binding.maxChildText.text.toString(),
                binding.maxOccupancyText.text.toString(),
                binding.bedCount.text.toString(),
                bedTypeIds.toString(),
                amenityIdsList.toString(),
                "Android"
                )
        )
        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {

                Log.d("sent", response.message())

                if (response.isSuccessful){

                    page = 2

                    binding.roomImages.textSize = 18.0f
                    binding.roomImages.typeface = robotoMedium
                    binding.roomImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                    binding.roomProfile.textSize = 16.0f
                    binding.roomProfile.typeface = roboto

                    binding.chargePlans.textSize = 16.0f
                    binding.chargePlans.typeface = roboto

                    binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
                    binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                    binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

                    val childFragment: Fragment = AddImagesFragment()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.createRoomFragContainer,childFragment)
                    transaction.commit()

                    binding.frame.visibility = View.GONE
                    binding.createRoomFragContainer.visibility = View.VISIBLE
                    rightInAnimation(binding.createRoomFragContainer, requireContext())

                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun bedType(){
        binding.addBeds.setOnClickListener {
            if (!binding.bed1TypeET.text!!.isEmpty()) {
                bedCount++
                binding.bedCount.setText("$bedCount")
                bedCountList.add("$bedCount")
                addBedTypeAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), bedTypeIds.size.toString(), Toast.LENGTH_SHORT).show()
            } else {
                shakeAnimation(binding.bed1TypeLayout, requireContext())
            }
        }
        binding.removeBeds.setOnClickListener {
            if (bedCount>1) {
                bedCountList.remove("$bedCount")
                bedCount--
                binding.bedCount.setText("$bedCount")
                addBedTypeAdapter.notifyDataSetChanged()
            }
        }

        addBedTypeAdapter = AddBedTypeAdapter(requireContext(), bedCountList, bedSuggestionList)
        addBedTypeAdapter.setOnBedSelection(this@AddRoomTypeFragment)
        binding.bedTypeRecycler.adapter = addBedTypeAdapter
    }
    private fun getBedType() {
        val getBed = RetrofitObject.dropDownApis.getBedType()
        getBed.enqueue(object : Callback<GetBedTypeData?> {
            override fun onResponse(
                call: Call<GetBedTypeData?>,
                response: Response<GetBedTypeData?>
            ) {
                if (response.isSuccessful){
                    val data = response.body()!!.data
                    bedTypeList.addAll(data)
                    data.forEach {
                        bedSuggestionList.add(it.bedType)
                    }
                    bedType()
                } else {
                    Toast.makeText(requireContext(), response.body()!!.copy().toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<GetBedTypeData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }


    private fun handleCounts(){
        /* --------------- Handle Base Adult -------------------*/
        binding.addBaseAdult.setOnClickListener {
            baseAdultCount++
            binding.baseAdultText.text = ("$baseAdultCount")
        }
        binding.removeBaseAdult.setOnClickListener {
            if (baseAdultCount>1) {
                baseAdultCount--
                binding.baseAdultText.text = ("$baseAdultCount")
            }
        }

        /* --------------- Handle Base Child -------------------*/
        binding.addBaseChild.setOnClickListener {
            baseChildCount++
            binding.baseChildText.text = ("$baseChildCount")
        }
        binding.removeBaseChild.setOnClickListener {
            if (baseChildCount>1) {
                baseChildCount--
                binding.baseChildText.text = ("$baseChildCount")
            }
        }

        /* --------------- Handle Max Adult -------------------*/
        binding.addMaxAdult.setOnClickListener {
            maxAdultCount++
            binding.maxAdultText.text = ("$maxAdultCount")
        }
        binding.removeMaxAdult.setOnClickListener {
            if (maxAdultCount>1) {
                maxAdultCount--
                binding.maxAdultText.text = ("$maxAdultCount")
            }
        }

        /* --------------- Handle Max Child -------------------*/
        binding.addMaxChild.setOnClickListener {
            maxChildCount++
            binding.maxChildText.text = ("$maxChildCount")
        }
        binding.removeMaxChild.setOnClickListener {
            if (maxChildCount>1) {
                maxChildCount--
                binding.maxChildText.text = ("$maxChildCount")
            }
        }

        /* --------------- Handle Max Occupancy -------------------*/
        binding.addBaseOccupancy.setOnClickListener {
            maxOccupancyCount++
            binding.maxOccupancyText.text = ("$maxOccupancyCount")
        }
        binding.removeBaseOccupancy.setOnClickListener {
            if (maxOccupancyCount>1) {
                maxOccupancyCount--
                binding.maxOccupancyText.text = ("$maxOccupancyCount")
            }
        }

    }

    private fun openAddAmenitiesDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_amenities)

        val createNewAmenityBtn = dialog.findViewById<TextView>(R.id.createNewAmenityBtn)
        createNewAmenityBtn.setOnClickListener {
            openCreateNewAmenityDialog()
        }
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val amenitiesRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesRecycler)
        amenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 8)

        val getAmenity = RetrofitObject.getGeneralsAPI.getAmenityApi()
        getAmenity.enqueue(object : Callback<AmenityDataClass?> {
            override fun onResponse(
                call: Call<AmenityDataClass?>,
                response: Response<AmenityDataClass?>
            ) {

                if (response.isSuccessful) {
                    val addAmenitiesAdapter = AddAmenitiesAdapter(requireContext(), response.body()!!.data)
                    amenitiesRecycler.adapter = addAmenitiesAdapter
                    addAmenitiesAdapter.notifyDataSetChanged()
                    addAmenitiesAdapter.setOnClickListener(this@AddRoomTypeFragment)
                }
            }

            override fun onFailure(call: Call<AmenityDataClass?>, t: Throwable) {
                Log.d("error" , t.localizedMessage)
            }
        })

        val saveBtn = dialog.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
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
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

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
                    Log.d(requireContext().javaClass.name, response.message())
                    Log.d(this@AddRoomTypeFragment.activity?.localClassName, response.message())
                    val amenitiesIconAdapter = AmenitiesIconAdapter(requireContext(), response.body()!!.data)
                    amenitiesIconRecycler.adapter = amenitiesIconAdapter
                    amenitiesIconAdapter.setOnIconClick(this@AddRoomTypeFragment)
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
    }

    fun onTabClick(){

        binding.roomProfile.setOnClickListener {

            page = 1

            binding.roomProfile.textSize = 20.0f
            binding.roomProfile.typeface = robotoMedium
            binding.roomProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomImages.textSize = 16.0f
            binding.roomImages.typeface = roboto

            binding.chargePlans.textSize = 16.0f
            binding.chargePlans.typeface = roboto

            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            binding.frame.visibility = View.VISIBLE
            binding.createRoomFragContainer.visibility = View.GONE
            rightInAnimation(binding.createRoomFragContainer, requireContext())
        }

        binding.roomImages.setOnClickListener {

            page = 2

            binding.roomImages.textSize = 20.0f
            binding.roomImages.typeface = robotoMedium
            binding.roomImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomProfile.textSize = 16.0f
            binding.roomProfile.typeface = roboto

            binding.chargePlans.textSize = 16.0f
            binding.chargePlans.typeface = roboto

            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            val childFragment: Fragment = AddImagesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.createRoomFragContainer,childFragment)
            transaction.commit()

            binding.frame.visibility = View.GONE
            binding.createRoomFragContainer.visibility = View.VISIBLE
            rightInAnimation(binding.createRoomFragContainer, requireContext())
        }

        binding.chargePlans.setOnClickListener {

            page = 3

            binding.chargePlans.textSize = 20.0f
            binding.chargePlans.typeface = robotoMedium
            binding.chargePlans.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomImages.textSize = 16.0f
            binding.roomImages.typeface = roboto

            binding.roomProfile.textSize = 16.0f
            binding.roomProfile.typeface = roboto

            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

            val childFragment: Fragment = ChargesAndRatesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.createRoomFragContainer,childFragment)
            transaction.commit()

            binding.frame.visibility = View.GONE
            binding.createRoomFragContainer.visibility = View.VISIBLE
            rightInAnimation(binding.createRoomFragContainer, requireContext())

        }

    }

    override fun bedSelected(bedTypeNameList: ArrayList<String>) {
        bedTypeList.forEach {bedType->
            bedTypeNameList.forEach {
                if (bedType.bedType.toString() == it.toString()) {
                    if (!bedTypeIds.contains(bedType.bedTypeId)) {
//                        Toast.makeText(requireContext(), bedType.bedTypeId, Toast.LENGTH_SHORT).show()
                        bedTypeIds.add(bedType.bedTypeId)
                    }
                }
            }
        }
    }

    override fun onItemListUpdate(selectedAmenitiesList: ArrayList<GetAmenityData>) {
        binding.selectedAmenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
        val selectedAmenitiesAdapter = SelectedAmenitiesAdapter(requireContext(), selectedAmenitiesList)
        binding.selectedAmenitiesRecycler.adapter = selectedAmenitiesAdapter
        selectedAmenitiesAdapter.notifyDataSetChanged()

        selectedAmenitiesList.forEach {
            if (!amenityIdsList.contains(it.amenityId)) {
                amenityIdsList.add(it.amenityId)
            } else {
                amenityIdsList.remove(it.amenityId)
            }
        }
    }

    private fun showDropdownBedTypeMenu(adapter: ArrayAdapter<String>, anchorView: View, et : TextInputEditText) {
        val listPopupWindow = ListPopupWindow(requireContext())
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            et.setText(selectedItem)
            bedTypeList.forEach {
                Log.d("bedTypeForE", it.bedTypeId)
                Log.d("bedTypeForE", it.bedType)
                if (it.bedType.toString() == selectedItem.toString()) {
                    Log.d("cond", it.bedTypeId)
                    bedTypeIds.add(it.bedTypeId)
                }
            }
            listPopupWindow.dismiss()
        }
        listPopupWindow.show()
    }

}