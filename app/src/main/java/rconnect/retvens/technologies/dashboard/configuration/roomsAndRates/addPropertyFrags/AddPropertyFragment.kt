package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListPopupWindow
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.internal.notify
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityData
import rconnect.retvens.technologies.databinding.FragmentAddPropertyBinding
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fadeOutAnimation
import rconnect.retvens.technologies.utils.fadeInAnimation
import rconnect.retvens.technologies.utils.leftInAnimation
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.rightInAnimation
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException
import java.lang.NullPointerException

class AddPropertyFragment : Fragment(), OnMapReadyCallback, AddAmenitiesAdapter.OnItemClick {

    private lateinit var binding : FragmentAddPropertyBinding
    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface
    private var page = 1

    private var imageUri: Uri ?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var isPropertyLogo = true

    private var isImageSelected = false
    private var isImageEdited = false

    val amenityIdsList = ArrayList<String>()

    val itemsPropertyType = ArrayList<String>()
    var propertyType = "Property Type"

    val itemsPropertyTypeRating = ArrayList<String>()
    var propertyTypeRating = "Property Rating"

    var latitude:Double = 0.0
    var longitude:Double = 0.0
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
        placesAPI()

        getPropertyTypeRating()


        binding.continueBtn.setOnClickListener {

            if (page == 1){

                if (binding.propertyNameET.text!!.isEmpty()) {
                    shakeAnimation(binding.propertyNameLayout, requireContext())
                } else if (binding.propertyTypeET.text!!.isEmpty()) {
                    shakeAnimation(binding.propertyTypeLayout, requireContext())
                } else if (binding.propertyRatingET.text!!.isEmpty()) {
                    shakeAnimation(binding.propertyRatingLayout, requireContext())
                } else if (amenityIdsList.isEmpty()) {
                    shakeAnimation(binding.add, requireContext())
                } else {
                    page = 2

                    binding.addressAndContacts.textSize = 18.0f
                    binding.addressAndContacts.typeface = robotoMedium
                    binding.addressAndContacts.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.secondary
                        )
                    )

                    binding.propertyProfile.textSize = 16.0f
                    binding.propertyProfile.typeface = roboto

                    binding.propertyImages.textSize = 16.0f
                    binding.propertyImages.typeface = roboto

                    binding.propertyProfile.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.corner_top_grey_background
                        )
                    )
                    binding.addressAndContacts.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.corner_top_white_background
                        )
                    )
                    binding.propertyImages.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.corner_top_grey_background
                        )
                    )

                    binding.propertyProfileLayout.visibility = View.GONE
                    binding.propertyImagesFrameLayout.visibility = View.GONE
                    binding.addressLayout.visibility = View.VISIBLE
                    rightInAnimation(binding.addressLayout, requireContext())
                }
            } else if (page == 2){
                if (binding.addressLine1ET.text!!.isEmpty()) {
                    shakeAnimation(binding.addressLine1Layout, requireContext())
                } else if (binding.countryText.text!!.isEmpty()) {
                    shakeAnimation(binding.countryLayout, requireContext())
                } else if (binding.stateText.text!!.isEmpty()) {
                    shakeAnimation(binding.stateLayout, requireContext())
                }  else if (binding.cityText.text!!.isEmpty()) {
                    shakeAnimation(binding.cityLayout, requireContext())
                } else if (binding.pincodeText.text!!.isEmpty()) {
                    shakeAnimation(binding.pincodeLayout, requireContext())
                } else if (binding.phoneText.text!!.isEmpty()) {
                    shakeAnimation(binding.phoneLayout, requireContext())
                } else if (binding.reservationPhoneText.text!!.isEmpty()) {
                    shakeAnimation(binding.reservationPhoneLayout, requireContext())
                }  else if (binding.emailText.text!!.isEmpty()) {
                    shakeAnimation(binding.emailLayout, requireContext())
                }  else {
                    sendData()
                }
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

        binding.add.setOnClickListener { openAddAmenitiesDialog() }

        pickImageWork()
    }

    private fun getPropertyType() {
        val getPropType = RetrofitObject.dropDownApis.getPropertyType()
        getPropType.enqueue(object : Callback<GetPropertyTypeData?> {
            override fun onResponse(
                call: Call<GetPropertyTypeData?>,
                response: Response<GetPropertyTypeData?>
            ) {
                if (isAdded) {
                    if (response.isSuccessful) {
                        val data = response.body()!!.data
                        data.forEach{
                            itemsPropertyType.add(it.propertyType)
                        }
                        binding.propertyTypeET.setOnClickListener {
                            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, itemsPropertyType)
                            showDropdownMenu(adapter, it, binding.propertyTypeET)
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
        val getPropType = RetrofitObject.dropDownApis.getPropertyTypeRating()
        getPropType.enqueue(object : Callback<GetPropertyTypeRatingData?> {
            override fun onResponse(
                call: Call<GetPropertyTypeRatingData?>,
                response: Response<GetPropertyTypeRatingData?>
            ) {
                if (isAdded) {
//                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                    if (response.isSuccessful) {

                        val data = response.body()!!.data
                        data.forEach{
                            itemsPropertyTypeRating.add(it.propertyRating)
                        }
                        binding.propertyRatingET.setOnClickListener {
                            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item1, itemsPropertyTypeRating)
                            showDropdownMenu(adapter, it, binding.propertyRatingET)
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
        val amenityIds = amenityIdsList.joinToString(", ").removeSurrounding("[", "]")
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

                        page = 3

                        binding.propertyImages.textSize = 18.0f
                        binding.propertyImages.typeface = robotoMedium
                        binding.propertyImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                        binding.propertyProfile.textSize = 16.0f
                        binding.propertyProfile.typeface = roboto

                        binding.addressAndContacts.textSize = 16.0f
                        binding.addressAndContacts.typeface = roboto

                        binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                        binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                        binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

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

                        page = 3

                        binding.propertyImages.textSize = 18.0f
                        binding.propertyImages.typeface = robotoMedium
                        binding.propertyImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                        binding.propertyProfile.textSize = 16.0f
                        binding.propertyProfile.typeface = roboto

                        binding.addressAndContacts.textSize = 16.0f
                        binding.addressAndContacts.typeface = roboto

                        binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                        binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                        binding.propertyImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

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
        val target = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(target).title("Marker at Your Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, 15f))

    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    private fun pickImageWork(){

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
        createNewAmenityBtn.visibility = View.GONE
        createNewAmenityBtn.setOnClickListener {
//            openCreateNewAmenityDialog()
        }
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val saveBtn = dialog.findViewById<CardView>(R.id.saveBtn)
        saveBtn.setOnClickListener {
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
                    addAmenitiesAdapter.setOnClickListener(this@AddPropertyFragment)
                }
            }

            override fun onFailure(call: Call<AmenityDataClass?>, t: Throwable) {
                Log.d("error" , t.localizedMessage)
            }
        })

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun placesAPI() {
        // Initialize Places API
        Places.initialize(requireContext(), "AIzaSyBRAnfSXzM-fQXpa751GkbMQDEuavUSDP0")

        // Create a PlacesClient
        val placesClient: PlacesClient = Places.createClient(requireContext())

        // Initialize the AutoCompleteTextView and adapter
        val autoCompleteTextView = binding.addressLine1ET
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        autoCompleteTextView.setAdapter(adapter)

        // Set up the Autocomplete request
        autoCompleteTextView.threshold = 1  // Minimum characters to start autocomplete

        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedAddress = adapter.getItem(position).toString()

            // Handle the selected address as needed
            val lastThreeWords = extractLastThreeWords(selectedAddress)
            println("Last Three Words: $lastThreeWords")
            try {
                autoCompleteTextView.setText(removeLastThreeWords(selectedAddress))
                binding.cityText.setText(lastThreeWords.get(0))
                binding.stateText.setText(lastThreeWords.get(1))
                binding.countryText.setText(lastThreeWords.get(2))

                val mapFrag = childFragmentManager.findFragmentById(R.id.mapFragContainer) as SupportMapFragment
                mapFrag.getMapAsync { googleMap ->
                    // Fetch place details and update the map
                    fetchPlaceDetails(selectedAddress, placesClient, googleMap)
                }
            } catch (e: IndexOutOfBoundsException) {
                println(e.toString())
            }
        }

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Create a FindAutocompletePredictionsRequest
                val request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery(s.toString())
                    .build()

                // Perform the autocomplete request
                placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                    val predictions = response.autocompletePredictions
                    val suggestionList = mutableListOf<String>()

                    for (prediction in predictions) {
                        suggestionList.add(prediction.getFullText(null).toString())
                    }

                    // Update the adapter with the new suggestions
                    adapter.clear()
                    adapter.addAll(suggestionList)
                    autoCompleteTextView.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener { exception ->
                    println(exception.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun fetchPlaceDetails(selectedAddress: String, placesClient: PlacesClient, googleMap: GoogleMap) {
        // Create a FindAutocompletePredictionsRequest
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(AutocompleteSessionToken.newInstance())
            .setQuery(selectedAddress)
            .build()

        // Perform the autocomplete request
        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions

            if (predictions.isNotEmpty()) {
                val placeId = predictions[0].placeId
                val placeFields = listOf(Place.Field.LAT_LNG)
                val placeRequest = FetchPlaceRequest.builder(placeId, placeFields).build()

                placesClient.fetchPlace(placeRequest).addOnSuccessListener { placeResponse ->
                    val place = placeResponse.place
                    val latLng = place.latLng

                    if (latLng != null) {
                        val latitude = latLng.latitude
                        val longitude = latLng.longitude

                        // Create a LatLng object for the location
                        val location = LatLng(latitude, longitude)

                        // Move the camera to the location
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
                        googleMap.moveCamera(cameraUpdate)

                        // Add a marker to the location
                        googleMap.addMarker(MarkerOptions().position(location).title("Selected Location"))
                    }
                }.addOnFailureListener { exception ->
                    println(exception.toString())
                }
            }
        }.addOnFailureListener { exception ->
            println(exception.toString())
        }
    }



    private fun extractLastThreeWords(input: String): List<String> {
        val words = input.split(",").map { it.trim() }
        return if (words.size >= 3) {
            words.subList(words.size - 3, words.size)
        } else {
            words
        }
    }
    fun removeLastThreeWords(input: String): String {
        val words = input.split(",").map { it.trim() }
        if (words.size >= 3) {
            val remainingWords = words.subList(0, words.size - 3).joinToString(", ")
            return remainingWords
        } else {
            return ""
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

    fun onTabsClick() {

        binding.propertyProfile.setOnClickListener {
            page = 1

            binding.propertyProfile.textSize = 18.0f
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
            binding.addressLayout.visibility = View.GONE
            binding.propertyImagesFrameLayout.visibility = View.GONE
            leftInAnimation(binding.propertyProfileLayout, requireContext())

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

            val childFragment: Fragment = AddImagesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.propertyImagesFrameLayout,childFragment)
            transaction.commit()

            binding.propertyProfileLayout.visibility = View.GONE
            binding.propertyImagesFrameLayout.visibility = View.VISIBLE
            binding.addressLayout.visibility = View.GONE
            rightInAnimation(binding.addressLayout, requireContext())
        }
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