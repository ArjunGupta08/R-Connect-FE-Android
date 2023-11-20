package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.SelectedAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.CreateAmenityDialog
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeFragment
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.rightInAnimation
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRoomTypeFragment : Fragment(),
    SelectedAmenitiesAdapter.OnSelectedAmenityRemove,
    AddBedTypeAdapter.BedTypeIdInterface,
    AddAmenitiesDialog.OnAmenityAdd {

    private lateinit var binding:FragmentAddRoomTypeBinding

    private lateinit var selectedAmenitiesAdapter : SelectedAmenitiesAdapter
    private var selectedAmenitiesListFinal = ArrayList<GetAmenityData>()

    private var bedTypeIds = ArrayList<String>()
    val bedTypeList = ArrayList<GetBedTypeDataClass>()
    val bedSuggestionList = ArrayList<String>()

    val amenityIdsList = ArrayList<String>()

    private var page = 1

    private var roomTypeInventoryCount = 1
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

    private lateinit var progressDialog : Dialog
    private var mLastClickTime : Long = 0


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

        onTabClick()

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        binding.continueBtnRoom.setOnClickListener {

            if (page == 1){

                if (binding.roomTypeNameEt.text!!.isEmpty()){
                    shakeAnimation(binding.roomTypeNameLayout, requireContext())
                } else if (binding.shortCodeET.text!!.isEmpty()){
                    shakeAnimation(binding.shortCodeLayout, requireContext())
//                } else if (binding.bed1TypeET.text!!.isEmpty()){
//                    shakeAnimation(binding.bed1TypeLayout, requireContext())
                } else if (amenityIdsList.isEmpty()){
                    shakeAnimation(binding.add, requireContext())
                } else {
                    progressDialog = showProgressDialog(requireContext())
                    sendData()
                    Const.addedRoomTypeName = binding.roomTypeNameEt.text.toString()
                    Const.addedRoomTypeShortCode = binding.shortCodeET.text.toString()
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

//        binding.bed1TypeET.setOnClickListener {
//            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, bedSuggestionList)
//            showDropdownBedTypeMenu(adapter, it, binding.bed1TypeET)
//        }
        binding.bedTypeRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.add.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = AddAmenitiesDialog(selectedAmenitiesListFinal, true)
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, AddAmenitiesDialog.TAG)}
            openDialog.setOnAddAmenityDialogListener(this)
        }

        getBedType()
    }

    private fun sendData(){
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
                roomTypeInventoryCount,
                binding.bedCount.text.toString(),
                bedTypeIds.joinToString(", ").removeSurrounding("[", "]"),
               amenityIdsList.joinToString(", ").removeSurrounding("[", "]"),
                "Android"
                )
        )
        send.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {

                Log.d("sent", response.message())

                progressDialog.dismiss()

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
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun bedType(){
        bedCountList.add("$bedCount")
        binding.addBeds.setOnClickListener {
                bedCount++
                binding.bedCount.setText("$bedCount")
                bedCountList.add("$bedCount")
                addBedTypeAdapter.notifyDataSetChanged()
                Toast.makeText(requireContext(), bedTypeIds.size.toString(), Toast.LENGTH_SHORT).show()
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
                    try {
                        val data = response.body()!!.data
                        bedTypeList.addAll(data)
                        data.forEach {
                            bedSuggestionList.add(it.bedType)
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
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

        /* --------------- Handle Room Type Inventory -------------------*/
        binding.addRoomTypeInventory.setOnClickListener {
            roomTypeInventoryCount++
            binding.roomTypeInventoryCountText.text = ("$roomTypeInventoryCount")
        }
        binding.removeRoomTypeInventory.setOnClickListener {
            if (roomTypeInventoryCount>1) {
                roomTypeInventoryCount--
                binding.roomTypeInventoryCountText.text = ("$roomTypeInventoryCount")
            }
        }

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

    private fun onTabClick(){

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

            shakeAnimation(binding.continueBtnRoom, requireContext())

        //            page = 2
//
//            binding.roomImages.textSize = 20.0f
//            binding.roomImages.typeface = robotoMedium
//            binding.roomImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
//
//            binding.roomProfile.textSize = 16.0f
//            binding.roomProfile.typeface = roboto
//
//            binding.chargePlans.textSize = 16.0f
//            binding.chargePlans.typeface = roboto
//
//            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
//            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
//            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
//
//            val childFragment: Fragment = AddImagesFragment()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.createRoomFragContainer,childFragment)
//            transaction.commit()
//
//            binding.frame.visibility = View.GONE
//            binding.createRoomFragContainer.visibility = View.VISIBLE
//            rightInAnimation(binding.createRoomFragContainer, requireContext())
        }

        binding.chargePlans.setOnClickListener {
            shakeAnimation(binding.continueBtnRoom, requireContext())

//            page = 3
//
//            binding.chargePlans.textSize = 20.0f
//            binding.chargePlans.typeface = robotoMedium
//            binding.chargePlans.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
//
//            binding.roomImages.textSize = 16.0f
//            binding.roomImages.typeface = roboto
//
//            binding.roomProfile.textSize = 16.0f
//            binding.roomProfile.typeface = roboto
//
//            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
//            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
//            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
//
//            val childFragment: Fragment = ChargesAndRatesFragment()
//            val transaction = requireActivity().supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.createRoomFragContainer,childFragment)
//            transaction.commit()
//
//            binding.frame.visibility = View.GONE
//            binding.createRoomFragContainer.visibility = View.VISIBLE
//            rightInAnimation(binding.createRoomFragContainer, requireContext())

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

    override fun onAmenityAdd(selectedAmenitiesList: ArrayList<GetAmenityData>) {
        binding.selectedAmenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 4)
        selectedAmenitiesList.forEach {
            if (!selectedAmenitiesListFinal.contains(it)) {
                selectedAmenitiesListFinal.add(it)
            }
        }
        selectedAmenitiesAdapter = SelectedAmenitiesAdapter(requireContext(), selectedAmenitiesListFinal)
        binding.selectedAmenitiesRecycler.adapter = selectedAmenitiesAdapter
        selectedAmenitiesAdapter.setOnAmenityRemoveListener(this)
        selectedAmenitiesAdapter.notifyDataSetChanged()

        selectedAmenitiesListFinal.forEach {
            if (!amenityIdsList.contains(it.amenityId)) {
                amenityIdsList.add(it.amenityId)
            } else {
                amenityIdsList.remove(it.amenityId)
            }
        }
    }

    override fun onSelectedAmenityRemove(currentItem : GetAmenityData) {
        selectedAmenitiesListFinal.remove(currentItem)
        selectedAmenitiesAdapter.notifyDataSetChanged()
    }

}