package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.CorporatesApi
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RateType
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.AccountType
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.AccountTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.CompanyResponse
import rconnect.retvens.technologies.databinding.FragmentCompanyDetailsChild2Binding
import rconnect.retvens.technologies.databinding.FragmentContractDetailsChildBinding
import rconnect.retvens.technologies.databinding.FragmentViewCompanyBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fadeInAnimation
import rconnect.retvens.technologies.utils.fadeOutAnimation
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.rightInAnimation
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException


class CompanyDetailsUpdateFragment(val companyId:String) : Fragment() {

    private lateinit var binding:FragmentCompanyDetailsChild2Binding
    private  var accountTypeList:ArrayList<AccountType> = ArrayList()
    private var isImageSelected = false
    private var isImageEdited = false
    private var isPropertyLogo = true
    private var imageUri: Uri?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0
    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface
    private lateinit var progressDialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompanyDetailsChild2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = showProgressDialog(requireContext())
        getAccountType()
        getCompanyDetails()

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        val accountAdapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, accountTypeList)

        binding.companyTypeText.setOnClickListener {
            showAccountType(accountAdapter,it)
        }

        pickImageWork()
        placesAPI()

        binding.companyNameText.doAfterTextChanged {
            if (binding.companyNameText.text!!.length > 2){
                binding.shortCodeText.setText(generateShortCode(binding.companyNameText.text.toString()))
            }
        }

        val save = requireActivity().findViewById<CardView>(R.id.continueBtn)
        save.setOnClickListener {
            if (binding.companyNameText.text!!.isEmpty()){
                shakeAnimation(binding.companyNameLayout,requireContext())
            }else if (binding.companyTypeText.text!!.isEmpty()){
                shakeAnimation(binding.companyTypeLayout,requireContext())
            }else if (binding.shortCodeText.text!!.isEmpty()){
                shakeAnimation(binding.shortCodeLayout,requireContext())
            }else if (binding.registrationNumText.text!!.isEmpty()){
                shakeAnimation(binding.registrationNumLayout,requireContext())
            }else if (binding.taxIdText.text!!.isEmpty()){
                shakeAnimation(binding.taxIdLayout,requireContext())
            }else if (binding.companyWebText.text!!.isEmpty()){
                shakeAnimation(binding.companyWebLayout,requireContext())
            }else if (binding.companyMailText.text!!.isEmpty()){
                shakeAnimation(binding.companyMailLayout,requireContext())
            }else if (binding.openingBalanceText.text!!.isEmpty()){
                shakeAnimation(binding.openingBalanceLayout,requireContext())
            }else if (binding.creditLimitText.text!!.isEmpty()){
                shakeAnimation(binding.creditLimitLayout,requireContext())
            }else if (binding.countMonths.text.isEmpty()){
                shakeAnimation(binding.countMonths,requireContext())
            }else if (binding.countDays.text.isEmpty()){
                shakeAnimation(binding.countDays,requireContext())
            }else if (binding.contactPersonText.text!!.isEmpty()){
                shakeAnimation(binding.contactPersonLayout,requireContext())
            }else if (binding.personDesignationText.text!!.isEmpty()){
                shakeAnimation(binding.personDesignationLayout,requireContext())
            }else if (binding.phoneText.text!!.isEmpty()){
                shakeAnimation(binding.phoneLayout,requireContext())
            }else if (binding.emailText.text!!.isEmpty()){
                shakeAnimation(binding.emailLayout,requireContext())
            }else if (binding.addressLine1ET.text.isEmpty()){
                shakeAnimation(binding.etAddressLine,requireContext())
            }else if (binding.countryText.text.isEmpty()){
                shakeAnimation(binding.countryLayout,requireContext())
            }else if (binding.stateText.text.isEmpty()){
                shakeAnimation(binding.stateLayout,requireContext())
            }else if (binding.cityText.text.isEmpty()){
                shakeAnimation(binding.cityLayout,requireContext())
            }else if (binding.pincodeText.text!!.isEmpty()){
                shakeAnimation(binding.pinCodeLayout,requireContext())
            }else if (!isEmailValid(binding.emailText.text.toString())){
                shakeAnimation(binding.emailLayout,requireContext())
            }else if (!isEmailValid(binding.companyMailText.text.toString())){
                shakeAnimation(binding.companyMailLayout,requireContext())
            } else if (binding.phoneText.text!!.length < 10) {
                shakeAnimation(binding.phoneLayout, requireContext())
            } else{
                progressDialog = showProgressDialog(requireContext())
                sendData()
            }
        }

    }

    private fun getCompanyDetails() {

        val userId = UserSessionManager(requireContext()).getUserId().toString()
        val propertyId = UserSessionManager(requireContext()).getPropertyId().toString()

        val getCompanyDetails = OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java).getCompanyDetails(companyId,propertyId,userId)

        getCompanyDetails.enqueue(object : Callback<CompanyDetailsData?> {
            override fun onResponse(
                call: Call<CompanyDetailsData?>,
                response: Response<CompanyDetailsData?>
            ) {
                if (response.isSuccessful){
                    progressDialog.dismiss()
                    val response = response.body()!!
                    response.data.forEach { companyData ->
                        if (companyData.companyLogo.isNotEmpty()){
                            binding.propertyLogoImage.setImageURI(imageUri)
                            binding.propertyLogoImage.isVisible = true
                            binding.propertyLogoLayout.isVisible = false
                            isImageSelected = true
                            Glide.with(requireContext()).load(companyData.companyLogo).into(binding.propertyLogoImage)
                        }
                        binding.companyNameText.setText(companyData.companyName)
                        binding.companyTypeText.setText(companyData.accountType)
                        binding.shortCodeText.setText(companyData.shortCode)
                        binding.registrationNumText.setText(companyData.registrationNumber)
                        binding.taxIdText.setText(companyData.taxId)
                        binding.companyWebText.setText(companyData.companyWebsite)
                        binding.companyMailText.setText(companyData.companyEmail)
                        binding.openingBalanceText.setText(companyData.openingBalance)
                        binding.creditLimitText.setText(companyData.creditLimit)
                        binding.countMonths.setText(companyData.month)
                        binding.countDays.setText(companyData.days)
                        binding.contactPersonText.setText(companyData.contactPerson)
                        binding.personDesignationText.setText(companyData.personDesignation)
                        binding.phoneText.setText(companyData.phoneNumber)
                        binding.emailText.setText(companyData.personEmail)
                        binding.addressLine1ET.setText(companyData.addressLine1)
                        binding.textAddressLine2.setText(companyData.addressLine2)
                        binding.countryText.setText(companyData.country)
                        binding.stateText.setText(companyData.state)
                        binding.cityText.setText(companyData.city)
                        binding.pincodeText.setText(companyData.zipCode)
                    }
                }else{
                    progressDialog.dismiss()
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<CompanyDetailsData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun sendData() {

        val userId = UserSessionManager(requireContext()).getUserId().toString()
        val propertyId = UserSessionManager(requireContext()).getPropertyId().toString()


        Log.e("error",companyId.toString())

        if (imageUri != null){
            val propertyLogo = prepareFilePart(imageUri!!,"companyLogo",requireContext())
            val sendData = OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java).updateCompany(
                userId,
                companyId,
                propertyLogo!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyNameText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyTypeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyMailText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyWebText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.shortCodeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.registrationNumText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.taxIdText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.openingBalanceText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countMonths.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countDays.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.contactPersonText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.phoneText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.personDesignationText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.emailText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.addressLine1ET.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.textAddressLine2.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countryText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.stateText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.cityText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.pincodeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.creditLimitText.text.toString())
            )

            sendData.enqueue(object : Callback<CompanyResponse?> {
                override fun onResponse(
                    call: Call<CompanyResponse?>,
                    response: Response<CompanyResponse?>
                ) {
                    if (response.isSuccessful){
                        progressDialog.dismiss()
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Log.e("error",response.code().toString())
                        Log.e("errorMessage",response.message().toString())
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<CompanyResponse?>, t: Throwable) {
                    Log.e("error",t.message.toString())
                    progressDialog.dismiss()
                }
            })
        }else{
            val sendData = OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java).updateCompanyWithoutImage(
                userId,
                companyId,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyNameText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyTypeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyMailText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.companyWebText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.shortCodeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.registrationNumText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.taxIdText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.openingBalanceText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countMonths.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countDays.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.contactPersonText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.phoneText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.personDesignationText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.emailText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.addressLine1ET.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.textAddressLine2.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.countryText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.stateText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.cityText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.pincodeText.text.toString()),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), binding.creditLimitText.text.toString())
            )

            sendData.enqueue(object : Callback<CompanyResponse?> {
                override fun onResponse(
                    call: Call<CompanyResponse?>,
                    response: Response<CompanyResponse?>
                ) {
                    if (response.isSuccessful){
                        progressDialog.dismiss()
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    }else{
                        Log.e("error",response.code().toString())
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<CompanyResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e("error",t.message.toString())
                }
            })
        }


    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun getAccountType() {
        val getAccountType = RetrofitObject.corporatesApi.getAccountType()

        getAccountType.enqueue(object : Callback<AccountTypeDataClass?> {
            override fun onResponse(
                call: Call<AccountTypeDataClass?>,
                response: Response<AccountTypeDataClass?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    accountTypeList.addAll(response.data)
                }else{
                    Log.e("error",response.code().toString())
                }
            }

            override fun onFailure(call: Call<AccountTypeDataClass?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })
    }

    private fun showAccountType(adapter: ArrayAdapter<AccountType>, it: View?) {
        val listPopupWindow = ListPopupWindow(requireContext())

        // Create a custom adapter to display only the rateType property
        val customAdapter = object : ArrayAdapter<AccountType>(requireContext(), R.layout.simple_dropdown_item_1line, accountTypeList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val rateType = getItem(position)?.accountType
                (view as TextView).text = rateType
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val rateType = getItem(position)?.accountType
                (view as TextView).text = rateType
                return view
            }
        }

        listPopupWindow.setAdapter(customAdapter)

        listPopupWindow.anchorView = it
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = customAdapter.getItem(position)
            binding.companyTypeLayout.editText?.setText(selectedItem?.accountType)
            listPopupWindow.dismiss()
        }

        listPopupWindow.show()
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

                if (lastThreeWords.size >= 3) {
                    binding.cityText.setText(lastThreeWords[0])
                    binding.stateText.setText(lastThreeWords[1])
                    binding.countryText.setText(lastThreeWords[2])
                } else {
                    // Handle the case when lastThreeWords doesn't have enough elements
                    // You might want to log a message or take appropriate action
                }

            } catch (e: IndexOutOfBoundsException) {
                println("IndexOutOfBoundsException: ${e.message}")
                // Handle the exception as needed
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

    fun changeChildFragment( fragment: Fragment){
        val childFragment: Fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.addCompanyFragContainer,childFragment)
        transaction.commit()
    }

}