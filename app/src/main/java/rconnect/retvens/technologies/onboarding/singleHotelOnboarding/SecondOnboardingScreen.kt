package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen
import rconnect.retvens.technologies.utils.shakeAnimation
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class SecondOnboardingScreen : AppCompatActivity() {

    private lateinit var binding : ActivitySecondOnboardingScreenBinding

    private lateinit var imageUri: Uri
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

        fetchCountryName()

        autoFillLocationSuggetion()

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
                val intent = Intent(this,FinalOnboardingScreen::class.java)
                intent.putExtra("isSingle", true)

                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    android.util.Pair(binding.logo,"logo_img"),
                    android.util.Pair(binding.onBoardingImg,"onBoardingImg"),
                    android.util.Pair(binding.demoBackbtn,"backBtn")).toBundle()

                startActivity(intent, options)
            }
        }

    }

    private fun autoFillLocationSuggetion() {

        // Sample list of country names
        val countries = arrayOf(
            "United States", "United Kingdom", "Canada", "Australia", "Germany", "France", "Japan", "India"
            // Add more country names as needed
        )
        // Sample list of state names
        val states = arrayOf(
            "Madhya Pradesh", "Maharastra", "Utter Pradesh", "Kannada", "Tamil Nadu"
            // Add more states names as needed
        )
        // Sample list of city names
        val cities = arrayOf(
            "Indore", "outdoor", "Kanpur Nagar", "Kanpur Dehat", "Lucknow", "Banglore"
            // Add more states names as needed
        )

        // Create an ArrayAdapter with the country names and set it to the AutoCompleteTextView
        val adapterCountries = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, countries)
        val adapterStates = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, states)
        val adapterCities = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, cities)
        binding.countryText.setAdapter(adapterCountries)
        binding.stateText.setAdapter(adapterStates)
        binding.cityText.setAdapter(adapterCities)

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

    private fun fetchCountryName() {
//        Get the default time zone
        val timeZone = TimeZone.getDefault()

        // Get the ID of the time zone
        val timeZoneId = timeZone.id

        // Get the country (locale) associated with the time zone
        val country = getCountryForTimeZone(timeZoneId)

        // Display the country name
        if (country != "Unknown") {
            binding.countryText.setText(country)
        }
        Log.d("TimeZone", "Time Zone ID: $timeZoneId")
        Log.d("TimeZone", "Country: $country")
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

    private fun getCountryForTimeZone(timeZoneId: String): String {
        // Define a mapping of common time zones to countries
        val timeZoneToCountry = mapOf(
            "America/New_York" to "United States",
            "America/Los_Angeles" to "United States",
            "America/Chicago" to "United States",
            "America/Denver" to "United States",
            "America/Phoenix" to "United States",
            "America/Anchorage" to "United States",
            "America/Honolulu" to "United States (Hawaii)",
            "Europe/London" to "United Kingdom",
            "Europe/Paris" to "France",
            "Europe/Berlin" to "Germany",
            "Asia/Tokyo" to "Japan",
            "Asia/Shanghai" to "China",
            "Asia/Dubai" to "United Arab Emirates",
            "Asia/Kolkata" to "India",
            "Australia/Sydney" to "Australia",
            "Pacific/Auckland" to "New Zealand",
            "Africa/Cairo" to "Egypt",
            "Africa/Johannesburg" to "South Africa",
            "Asia/Singapore" to "Singapore",
            "Asia/Hong_Kong" to "Hong Kong",
            "America/Toronto" to "Canada",
            "Europe/Moscow" to "Russia",
            "America/Mexico_City" to "Mexico",
            "America/Buenos_Aires" to "Argentina",
            "Europe/Istanbul" to "Turkey"
            // Add more mappings as needed
        )

        // Look up the country for the given time zone
        val country = timeZoneToCountry[timeZoneId]

        // Return the country if found, or "Unknown" if not found
        return country ?: "Unknown"
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
}