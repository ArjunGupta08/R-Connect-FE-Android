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
import androidx.core.view.isVisible
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen
import rconnect.retvens.technologies.utils.autoFillLocationSuggestion
import rconnect.retvens.technologies.utils.fetchCountryName
import rconnect.retvens.technologies.utils.shakeAnimation

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

        // ------------ Display the country name using timezone---------
        val countryName = fetchCountryName()
        if (countryName != "Unknown"){
            binding.countryText.setText(countryName)
        }

        // ------------ Display the Location Suggestions ---------
        autoFillLocationSuggestion(this, binding.countryText, binding.stateText, binding.cityText)

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