package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.SharedPreference
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchCountryName
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException

class SecondOnboardingScreen : AppCompatActivity() {

    val countryList = ArrayList<String>()
    val fullAddressList = ArrayList<String>()
    private lateinit var binding : ActivitySecondOnboardingScreenBinding

    private var imageUri: Uri ?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var isImageSelected = false
    private var isImageEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        imageSelection()

        placesAPI()

        // ------------ Display the country name using timezone---------
        val countryName = fetchCountryName()
        if (countryName != "Unknown"){
            binding.countryText.setText(countryName)
        }

        // ------------ Display the Location Suggestions ---------
//        autoFillLocationSuggestion(this, fullAddressList, binding.addressText, binding.stateText, binding.cityText)

        binding.cardSingleNext.setOnClickListener {

            if (binding.propertyText.text!!.isEmpty()) {
                shakeAnimation(binding.propertyLayout,applicationContext)
                binding.propertyLayout.error = "Please enter property name"

            }
//            else if (binding.propertyTypeText.text!!.isEmpty()) {
//                binding.propertyTypeLayout.error = "Please enter property type"
//                shakeAnimation(binding.propertyTypeLayout,applicationContext)
//                binding.propertyTypeLayout.isErrorEnabled = false
//
//            }
            else if (binding.addressText.text!!.isEmpty()) {
                binding.addressLayout.error = "Please enter property address"
                shakeAnimation(binding.addressLayout,applicationContext)
                binding.propertyLayout.isErrorEnabled = false
            }
            else if (binding.pincodeText.text!!.isEmpty()) {
                binding.pincodeLayout.error = "Please enter pin code"
                shakeAnimation(binding.pincodeLayout,applicationContext)
                binding.addressLayout.isErrorEnabled = false
            }
//            else if (binding.cityText.text!!.isEmpty()) {
//                binding.cityLayout.error = "Please enter city"
//                shakeAnimation(binding.cityLayout,applicationContext)
////                binding.cityLayout.isErrorEnabled = false
//            }
//            else if (binding.stateText.text!!.isEmpty()) {
//                binding.stateLayout.error = "Please enter state"
//                shakeAnimation(binding.stateLayout,applicationContext)
////                binding.stateLayout.isErrorEnabled = false
//            }
//            else if (binding.currencyText.text!!.isEmpty()) {
//                binding.currencyLayout.error = "Please enter currency"
//                shakeAnimation(binding.currencyLayout,applicationContext)
////                binding.currencyLayout.isErrorEnabled = false
//            }
            else {
                sendData()
            }
        }
    }

    private fun sendData() {
        val userId = UserSessionManager(this).getUserId().toString()
        val propertyTypeSOC = "Single"
        val propertyName = binding.propertyText.text.toString()
        val propertyType = binding.propertyTypeText.text.toString()
        val websiteUrl = binding.websiteText.text.toString()
        val propertyAddress1 = binding.addressText.text.toString()
        val country = binding.countryText.text.toString()
        val state = binding.stateText.text.toString()
        val city = binding.cityText.text.toString()
        val postCode = binding.pincodeText.text.toString()

        if (imageUri != null) {
            val hotelLogo = prepareFilePart(imageUri!!, "hotelLogo", this)
            val firstOnboardingApi = RetrofitObject.retrofit.firstOnboarding(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyTypeSOC),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                hotelLogo!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyAddress1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postCode),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), state),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), city),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), country)
            )

            firstOnboardingApi.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!

                        SharedPreference(applicationContext).saveFirstFlagValue(true)

                        Toast.makeText(
                            applicationContext,
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent =
                            Intent(applicationContext, ThirdSingleOnboardingScreen::class.java)
                        intent.putExtra("isSingle", true)

                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@SecondOnboardingScreen,
                            android.util.Pair(binding.logo, "logo_img"),
                            android.util.Pair(binding.onBoardingImg, "onBoardingImg"),
                            android.util.Pair(binding.demoBackbtn, "backBtn")
                        ).toBundle()
                        startActivity(intent, options)
                        finish()

                    } else {
                        Log.d("Error Onboarding", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    Log.d("Error Onboarding", t.localizedMessage.toString())
                }
            })
        }else {
            val firstOnboardingApi = RetrofitObject.retrofit.firstOnboardingWithoutImage(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyTypeSOC),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyAddress1),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), postCode),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), state),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), city),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), country)
            )

            firstOnboardingApi.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!
                        Toast.makeText(
                            applicationContext,
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(applicationContext, ThirdSingleOnboardingScreen::class.java)

                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        intent.putExtra("isSingle", true)

                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@SecondOnboardingScreen,
                            android.util.Pair(binding.logo, "logo_img"),
                            android.util.Pair(binding.onBoardingImg, "onBoardingImg"),
                            android.util.Pair(binding.demoBackbtn, "backBtn")
                        ).toBundle()
                        startActivity(intent, options)

                    } else {
                        Log.d("Error Onboarding", response.code().toString())
                    }
                }
                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    Log.d("Error Onboarding", t.localizedMessage.toString())
                }
            })
        }

    }

    private fun placesAPI() {

        // Initialize Places API
        Places.initialize(applicationContext, "AIzaSyBRAnfSXzM-fQXpa751GkbMQDEuavUSDP0")

        // Create a PlacesClient
        val placesClient: PlacesClient = Places.createClient(this)

        // Initialize the AutoCompleteTextView and adapter
        val autoCompleteTextView = binding.addressText
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)
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
            } catch (e : IndexOutOfBoundsException){
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
                        Log.d("suggetion", suggestionList.toString())
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

            override fun afterTextChanged(s: Editable?) {

            }
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

    private fun imageSelection() {

        binding.replaceImage.setOnClickListener {
            openGallery()
            rightToLeftEditImageAnimation(binding.imageEditLayout)
        }

        binding.removeImage.setOnClickListener {
            imageUri = Uri.EMPTY
            binding.galleryImg.setImageResource(R.drawable.svg_gallery)
            rightToLeftEditImageAnimation(binding.imageEditLayout)
            isImageSelected = false
            setMargins(binding.galleryImg, 15, 15, 15, 15)
        }

        binding.cardGallery.setOnClickListener {
            if (!isImageSelected){
                openGallery()
            } else {
                if (!isImageEdit) {
                    binding.imageEditLayout.isVisible = true
                    // load the animation
                    val animFadein: Animation = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.l_to_r_in_animation
                    )
                    // start the animation
                    binding.imageEditLayout.startAnimation(animFadein)
                    isImageEdit = true
                } else {
                    rightToLeftEditImageAnimation(binding.imageEditLayout)
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

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri = data.data!!
            if (imageUri != null) {
                try {
                    isImageSelected = true
                    setMargins(binding.galleryImg, 0, 0, 0, 0)
                    binding.galleryImg.setImageURI(imageUri)
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }

    private fun rightToLeftEditImageAnimation(view: View) {
        isImageEdit = false
        // load the animation
        val animSlideIn: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.l_to_r_out_animation
        )
        // start the animation
        view.startAnimation(animSlideIn)
        binding.imageEditLayout.isVisible = false
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
}