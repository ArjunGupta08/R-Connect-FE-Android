package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass
import rconnect.retvens.technologies.databinding.FragmentAddPropertyBinding
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fadeOutAnimation
import rconnect.retvens.technologies.utils.fadeInAnimation
import rconnect.retvens.technologies.utils.leftInAnimation
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.rightInAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPropertyFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding : FragmentAddPropertyBinding
    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface
    private var page = 1

    private var imageUri: Uri ?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var isPropertyLogo = true

    private var isImageSelected = false
    private var isImageEdited = false

    val itemsPropertyType = ArrayList<String>()
    var propertyType = "Property Type"

    val itemsPropertyTypeRating = ArrayList<String>()
    var propertyTypeRating = "Property Rating"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        leftInAnimation(binding.propertyProfileLayout, requireContext())

        getPropertyType()

        getPropertyTypeRating()

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFrag = childFragmentManager.findFragmentById(R.id.mapFragContainer) as SupportMapFragment
        mapFrag.getMapAsync(this)

        binding.continueBtn.setOnClickListener {

            if (page == 1){

                page = 2

                binding.addressAndContacts.textSize = 20.0f
                binding.addressAndContacts.typeface = robotoMedium
                binding.addressAndContacts.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.propertyProfile.textSize = 16.0f
                binding.propertyProfile.typeface = roboto

                binding.propertyImages.textSize = 16.0f
                binding.propertyImages.typeface = roboto

                binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
                binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

                binding.propertyProfileLayout.visibility = View.GONE
                binding.propertyImagesFrameLayout.visibility = View.GONE
                binding.addressLayout.visibility = View.VISIBLE
                rightInAnimation(binding.addressLayout, requireContext())

            } else if (page == 2){

                page = 3

                binding.propertyImages.textSize = 20.0f
                binding.propertyImages.typeface = robotoMedium
                binding.propertyImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.propertyProfile.textSize = 16.0f
                binding.propertyProfile.typeface = roboto

                binding.addressAndContacts.textSize = 16.0f
                binding.addressAndContacts.typeface = roboto

                binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

                sendData()

            } else {
                // Call Send Photos API from Another Fragment
            }
        }

        binding.cancel.setOnClickListener {

            if (Const.isAddingNewProperty){
                Const.isAddingNewProperty = false

                val childFragment: Fragment = ViewPropertiesFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                transaction.commit()

            } else {
                val welcomeLayout = requireActivity().findViewById<LinearLayout>(R.id.welcomeLayout)
                welcomeLayout.visibility = View.VISIBLE

                val dashboardFragmentContainer =
                    requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)
                dashboardFragmentContainer.visibility = View.GONE
            }
        }

        binding.propertyProfile.setOnClickListener {
            page = 1

            binding.propertyProfile.textSize = 20.0f
            binding.propertyProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
            binding.propertyProfile.typeface = robotoMedium

            binding.addressAndContacts.textSize = 16.0f
            binding.addressAndContacts.typeface = roboto

            binding.propertyImages.textSize = 16.0f
            binding.propertyImages.typeface = roboto

            binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            binding.propertyProfileLayout.visibility = View.VISIBLE
            binding.propertyImagesFrameLayout.visibility = View.VISIBLE
            leftInAnimation(binding.propertyProfileLayout, requireContext())
            binding.addressLayout.visibility = View.GONE
        }

        binding.addressAndContacts.setOnClickListener {
            page = 2

            binding.addressAndContacts.textSize = 20.0f
            binding.addressAndContacts.typeface = robotoMedium
            binding.addressAndContacts.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.propertyProfile.textSize = 16.0f
            binding.propertyProfile.typeface = roboto

            binding.propertyImages.textSize = 16.0f
            binding.propertyImages.typeface = roboto

            binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            binding.propertyProfileLayout.visibility = View.GONE
            binding.propertyImagesFrameLayout.visibility = View.GONE
            binding.addressLayout.visibility = View.VISIBLE
            rightInAnimation(binding.addressLayout, requireContext())
        }

        binding.propertyImages.setOnClickListener {
            page = 3

            binding.propertyImages.textSize = 20.0f
            binding.propertyImages.typeface = robotoMedium
            binding.propertyImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.propertyProfile.textSize = 16.0f
            binding.propertyProfile.typeface = roboto

            binding.addressAndContacts.textSize = 16.0f
            binding.addressAndContacts.typeface = roboto

            binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

            binding.propertyProfileLayout.visibility = View.GONE
            binding.propertyImagesFrameLayout.visibility = View.VISIBLE
            binding.addressLayout.visibility = View.GONE
            rightInAnimation(binding.addressLayout, requireContext())
        }

        binding.add.setOnClickListener { openAddAmenitiesDialog() }

        binding.replaceImage.setOnClickListener {
            openGallery()
            fadeOutAnimation(binding.imageEditLayout, requireContext())
            isImageEdited = false
            binding.imageEditLayout.isVisible = false
        }

        binding.removeImage.setOnClickListener {
            imageUri = null
            isImageSelected = false
            binding.propertyLogoImage.setImageURI(imageUri)
            binding.propertyLogoImage.isVisible = false
            binding.propertyLogoLayout.isVisible = true
            fadeOutAnimation(binding.imageEditLayout, requireContext())
            isImageEdited = false
            binding.imageEditLayout.isVisible = false
        }

        binding.propertyLogoCard.setOnClickListener {
            if (!isImageSelected){
                isPropertyLogo = true
                openGallery()
            } else {
                if (!isImageEdited) {
                    binding.imageEditLayout.isVisible = true
                    // load the animation
                    fadeInAnimation(binding.imageEditLayout, requireContext())
                    isImageEdited = true
                } else {
                    fadeOutAnimation(binding.imageEditLayout, requireContext())
                    binding.imageEditLayout.isVisible = false
                    isImageEdited = false
                }
            }
        }
    }

    private fun getPropertyType() {
        itemsPropertyType.add(0,"Property Type")
        val getPropType = RetrofitObject.dropDownApis.getPropertyType()
        getPropType.enqueue(object : Callback<GetPropertyTypeData?> {
            override fun onResponse(
                call: Call<GetPropertyTypeData?>,
                response: Response<GetPropertyTypeData?>
            ) {
                if (isAdded) {
//                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful) {

                        val data = response.body()!!
                        data.data.forEach{
                            itemsPropertyType.add(it.propertyType)
                        }
                        val spinner: Spinner = binding.spinner
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            itemsPropertyType
                        )
                        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
                        spinner.adapter = adapter
                        spinner.setSelection(0) // Set the default selection to the first item
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val selectedItem = itemsPropertyType[position]
                                    // Handle the selected item here
                                    if (selectedItem == "Property Type") {

                                    } else {
                                        propertyType = selectedItem.toString()
                                        Toast.makeText(
                                            requireContext(),
                                            "Selected: $selectedItem",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Handle the case when nothing is selected (optional)
                                }
                            }
                    }
                }
            }
            override fun onFailure(call: Call<GetPropertyTypeData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun getPropertyTypeRating() {
        itemsPropertyTypeRating.add(0,"Property Ratting")
        val getPropType = RetrofitObject.dropDownApis.getPropertyTypeRating()
        getPropType.enqueue(object : Callback<GetPropertyTypeRatingData?> {
            override fun onResponse(
                call: Call<GetPropertyTypeRatingData?>,
                response: Response<GetPropertyTypeRatingData?>
            ) {
                if (isAdded) {
//                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful) {

                        val data = response.body()!!
                        data.data.forEach{
                            itemsPropertyTypeRating.add(it.propertyRating)
                        }
                        val spinner: Spinner = binding.propertyRattingSpinner
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            itemsPropertyTypeRating
                        )
                        adapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
                        spinner.adapter = adapter
                        spinner.setSelection(0) // Set the default selection to the first item
                        spinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val selectedItem = itemsPropertyTypeRating[position]
                                    // Handle the selected item here
                                    if (selectedItem == "Property Type") {

                                    } else {
                                        propertyTypeRating = selectedItem.toString()
                                    }
                                }
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Handle the case when nothing is selected (optional)
                                }
                            }
                    }
                }
            }

            override fun onFailure(call: Call<GetPropertyTypeRatingData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    private fun sendData(){
        val propertyName = binding.propertyNameET.text.toString()
        val propertyType = propertyType
        val propertyRating = propertyTypeRating
        val websiteUrl = binding.websiteText.text.toString()
        val description = binding.descriptionET.text.toString()
        val amenityIds = "igig,ihinm"
        val address1 = binding.addressLine1ET.text.toString()
        val address2 = binding.addressLine2ET.text.toString()
        val postCode = binding.pincodeText.text.toString()
        val city = binding.cityText.text.toString()
        val state = binding.stateText.text.toString()
        val country = binding.countryText.text.toString()
        val phone = binding.phoneText.text.toString()
        val reservationPhoneText = binding.reservationPhoneText.text.toString()
        val email = binding.emailText.text.toString()
        val lat = binding.emailText.text.toString()
        val long = binding.emailText.text.toString()
        val userId = UserSessionManager(requireContext()).getUserId().toString()

        if (imageUri != null) {
            val hotelLogo = prepareFilePart(imageUri!!, "hotelLogo", requireContext())
            val addProperty = OAuthClient<ChainConfiguration>(requireContext()).create(ChainConfiguration::class.java).addPropertyApi(
                hotelLogo!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyRating),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), amenityIds),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address2),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postCode),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), city),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), state),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), country),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), reservationPhoneText),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lat),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), long),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
            )

            addProperty.enqueue(object : Callback<AddPropertyResponseDataClass?> {
                override fun onResponse(
                    call: Call<AddPropertyResponseDataClass?>,
                    response: Response<AddPropertyResponseDataClass?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!

                        UserSessionManager(requireContext()).savePropertyId(response.body()?.propertyId.toString())

                        Toast.makeText(
                            requireContext(),
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val childFragment: Fragment = AddImagesFragment()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.propertyImagesFrameLayout,childFragment)
                        transaction.commit()

                        binding.propertyProfileLayout.visibility = View.GONE
                        binding.addressLayout.visibility = View.GONE
                        binding.propertyImagesFrameLayout.visibility = View.VISIBLE
                        rightInAnimation(binding.propertyImagesFrameLayout, requireContext())
                    } else {
                        Log.d("Error Onboarding", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<AddPropertyResponseDataClass?>, t: Throwable) {
                    Log.d("Error Onboarding", t.localizedMessage.toString())
                }
            })
        } else {
            val addProperty = OAuthClient<ChainConfiguration>(requireContext()).create(ChainConfiguration::class.java).addPropertyWithoutLogoApi(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyRating),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), description),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), amenityIds),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), address2),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postCode),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), city),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), state),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), country),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phone),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), reservationPhoneText),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), lat),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), long),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
            )

            addProperty.enqueue(object : Callback<AddPropertyResponseDataClass?> {
                override fun onResponse(
                    call: Call<AddPropertyResponseDataClass?>,
                    response: Response<AddPropertyResponseDataClass?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!

                        UserSessionManager(requireContext()).savePropertyId(response.body()?.propertyId.toString())

                        Toast.makeText(
                            requireContext(),
                            respons.propertyId.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val childFragment: Fragment = AddImagesFragment()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.propertyImagesFrameLayout,childFragment)
                        transaction.commit()

                        binding.propertyProfileLayout.visibility = View.GONE
                        binding.addressLayout.visibility = View.GONE
                        binding.propertyImagesFrameLayout.visibility = View.VISIBLE
                        rightInAnimation(binding.propertyImagesFrameLayout, requireContext())
                    } else {
                        Log.d("Error Onboarding", "${response.code().toString()} ${response.message()} ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<AddPropertyResponseDataClass?>, t: Throwable) {
                    Log.d("Error Onboarding", t.localizedMessage.toString())
                }
            })
        }
    }

    // This method is called when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap) {

        // Add a marker at the specified location and move the camera to that location.
        val target = LatLng(28.679079, 77.069710)
        googleMap.addMarker(MarkerOptions().position(target).title("Marker at Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 15f))

    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            imageUri = data.data!!
            if (imageUri != null) {
                try {
                    if (isPropertyLogo) {
                        binding.propertyLogoImage.setImageURI(imageUri)
                        binding.propertyLogoImage.isVisible = true
                        binding.propertyLogoLayout.isVisible = false
                        isImageSelected = true
                    }
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
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
        amenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        val amenities = ArrayList<AddAmenitiesDataClass>()

        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))

        val addAmenitiesAdapter = AddAmenitiesAdapter(requireContext(), amenities)
        amenitiesRecycler.adapter = addAmenitiesAdapter

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

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val amenitiesIconRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesIconRecycler)
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        val amenities = ArrayList<AmenitiesIconDataClass>()

        amenities.add(AmenitiesIconDataClass(R.drawable.check))
        amenities.add(AmenitiesIconDataClass(R.drawable.check))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_keys))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_keys))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_add))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_add))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
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

}