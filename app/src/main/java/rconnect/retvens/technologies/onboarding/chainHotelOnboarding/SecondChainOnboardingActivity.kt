package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondChainOnboardingBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.SharedPreference
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecondChainOnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySecondChainOnboardingBinding

    var propertyCount : Int = 1

    private var imageUri: Uri ?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var isImageSelected = false
    private var isImageEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondChainOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener { onBackPressed() }

        binding.cardSingleNext.setOnClickListener {

            if (binding.propertyChainText.text!!.isEmpty()){
                shakeAnimation(binding.propertyChainLayout, applicationContext)
                binding.propertyChainLayout.error = ("Please enter your Chain Name")
            }
//            else if (binding .properytyTypeText.text!!.isEmpty()){
//                shakeAnimation(binding.propertyTypeLayout, applicationContext)
//                binding.propertyTypeLayout.error = ("Please enter your password")
//            }
            else {
                sendData()
            }
        }

        binding.replaceImage.setOnClickListener {
            openGallery()
            rightToLeftEditImageAnimation(binding.imageEditLayout)
        }

        binding.removeImage.setOnClickListener {
            imageUri = null
            binding.selectImg.setImageResource(R.drawable.svg_gallery)
            rightToLeftEditImageAnimation(binding.imageEditLayout)
            isImageSelected = false
            setMargins(binding.selectImg, 15, 15, 15, 15)
        }

        binding.cardImg.setOnClickListener {
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

        binding.propertyCountEditText.doAfterTextChanged {

            if (binding.propertyCountEditText.text.toString() != "") {
                var propertyCountET = binding.propertyCountEditText.text.toString()
                propertyCount = propertyCountET.toInt()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.add.setOnClickListener {
            propertyCount += 1
            binding.propertyCountEditText.setText("$propertyCount")
        }

        binding.remove.setOnClickListener {
            if (propertyCount > 1) {
                propertyCount -= 1
                binding.propertyCountEditText.setText("$propertyCount")
            }
        }

    }

    private fun sendData() {
        val userId = UserSessionManager(this).getUserId().toString()
        val propertyTypeSOC = "Multiple"
        val propertyName = binding.propertyChainText.text.toString()
        val propertyType = binding.properytyTypeText.text.toString()
        val websiteUrl = binding.websiteText.text.toString()
        val numberOfProperties = binding.propertyCountEditText.text.toString()
        val baseCurrency = binding.baseCurrencyText.text.toString()

        if (imageUri != null) {
            val hotelLogo = prepareFilePart(imageUri!!, "hotelLogo", this)
            val chainOnboardingApi = RetrofitObject.retrofit.firstChainOnboarding(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyTypeSOC),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                hotelLogo!!,
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), numberOfProperties),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), baseCurrency),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
            )

            chainOnboardingApi.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!
                        SharedPreference(applicationContext).saveSecondChainFlagValue(true)

                        Toast.makeText(
                            applicationContext,
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(applicationContext, FinalOnboardingScreen::class.java)
                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@SecondChainOnboardingActivity,
                            android.util.Pair(binding.logo, "logo_img"),
                            android.util.Pair(binding.onBoardingImg, "onBoardingImg"),
                            android.util.Pair(binding.cardSingleNext, "Btn"),
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
        } else {
            val chainOnboardingApi = RetrofitObject.retrofit.firstChainOnboardingWithoutImg(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyTypeSOC),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), websiteUrl),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), numberOfProperties),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyType),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), baseCurrency),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), propertyName),
            )

            chainOnboardingApi.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!
                        SharedPreference(applicationContext).saveSecondChainFlagValue(true)
                        Toast.makeText(
                            applicationContext,
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(applicationContext, FinalOnboardingScreen::class.java)

                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        val options = ActivityOptions.makeSceneTransitionAnimation(
                            this@SecondChainOnboardingActivity,
                            android.util.Pair(binding.logo, "logo_img"),
                            android.util.Pair(binding.onBoardingImg, "onBoardingImg"),
                            android.util.Pair(binding.cardSingleNext, "Btn"),
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
                    binding.selectImg.setImageURI(imageUri)
                    setMargins(binding.selectImg, 0, 0, 0, 0)
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
}