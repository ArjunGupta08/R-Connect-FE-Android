package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenBinding
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenMobileBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.onboarding.chainHotelOnboarding.ThirdChainOnboardingAdapter
import rconnect.retvens.technologies.onboarding.chainHotelOnboarding.ThirdChainOnboardingDataClass
import rconnect.retvens.technologies.utils.SharedPrefOnboardingFlags
import rconnect.retvens.technologies.utils.SharedPreference
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdSingleOnboardingScreen : AppCompatActivity() {

    private  var binding:ActivityThirdChainOnboardingScreenBinding? = null
    lateinit var bindingMobile:ActivityThirdChainOnboardingScreenMobileBinding

    private lateinit var thirdChainOnboardingAdapter: ThirdChainOnboardingAdapter
    private var dataList = ArrayList<ThirdChainOnboardingDataClass>()

    private var roomCount = 1
    private var rateCount = 1.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation

        when(currentOrientation){

            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivityThirdChainOnboardingScreenBinding.inflate(layoutInflater)
                setContentView(binding!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityThirdChainOnboardingScreenMobileBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
        }

        if (binding is ActivityThirdChainOnboardingScreenBinding){

        binding!!.demoBackbtn.setOnClickListener { onBackPressed() }

        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.setHasFixedSize(true)

        binding!!.save.setOnClickListener {
            addData()
        }

        binding!!.roomCount.doAfterTextChanged {

            if (binding!!.roomCount.text.toString() != "") {
                var roomCountET = binding!!.roomCount.text.toString()
                roomCount = roomCountET.toInt()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }

        }

            binding!!.removeRooms.setOnClickListener {
            if (roomCount > 1){
                roomCount--
                binding!!.roomCount.setText("$roomCount")
            }
        }
            binding!!.addRooms.setOnClickListener {
                roomCount++
                binding!!.roomCount.setText("$roomCount")
        }

            binding!!.rateCount.doAfterTextChanged {
            if (binding!!.rateCount.text.toString() != "") {
                var rateCountET = binding!!.rateCount.text.toString()
                rateCount = rateCountET.toDouble()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }
        }
            binding!!.removeRate.setOnClickListener {
            if (rateCount > 1){
                rateCount--
                binding!!.rateCount.setText("$rateCount")
            }
        }
            binding!!.addRate.setOnClickListener {
                rateCount++
                binding!!.rateCount.setText("$rateCount")
        }

            binding!!.cardSingleNext.setOnClickListener {
            if (binding!!.taxNameText.text!!.isEmpty()) {
                shakeAnimation(binding!!.TaxNameLayout,applicationContext)
                binding!!.TaxNameLayout.error = "Please enter tax name"
            }
            else if (binding!!.registrationNumberText.text!!.isEmpty()) {
                shakeAnimation(binding!!.registrationNumberLayout,applicationContext)
                binding!!.registrationNumberLayout.error = "Please enter registration number"
                binding!!.TaxNameLayout.isErrorEnabled = false
            }
            else{
                sendData()
            }
        }
    }
        else{

            bindingMobile!!.recyclerView.layoutManager = LinearLayoutManager(this)
            bindingMobile!!.recyclerView.setHasFixedSize(true)

            bindingMobile!!.save.setOnClickListener {
                addData()
            }

            bindingMobile!!.roomCount.doAfterTextChanged {

                if (bindingMobile!!.roomCount.text.toString() != "") {
                    var roomCountET = bindingMobile!!.roomCount.text.toString()
                    roomCount = roomCountET.toInt()
                } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
                }

            }

            bindingMobile!!.removeRooms.setOnClickListener {
                if (roomCount > 1){
                    roomCount--
                    bindingMobile!!.roomCount.setText("$roomCount")
                }
            }
            bindingMobile!!.addRooms.setOnClickListener {
                roomCount++
                bindingMobile!!.roomCount.setText("$roomCount")
            }

            bindingMobile!!.rateCount.doAfterTextChanged {
                if (bindingMobile!!.rateCount.text.toString() != "") {
                    var rateCountET = bindingMobile!!.rateCount.text.toString()
                    rateCount = rateCountET.toDouble()
                } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
                }
            }
            bindingMobile!!.removeRate.setOnClickListener {
                if (rateCount > 1){
                    rateCount--
                    bindingMobile!!.rateCount.setText("$rateCount")
                }
            }
            bindingMobile!!.addRate.setOnClickListener {
                rateCount++
                bindingMobile!!.rateCount.setText("$rateCount")
            }

            bindingMobile!!.cardSingleNext.setOnClickListener {
                if (bindingMobile!!.propertyText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile.propertyLayout, applicationContext)
                    bindingMobile.propertyLayout.error = "Please enter tax name"
                    bindingMobile.TaxNameLayout.isErrorEnabled = false
                }
                else if (bindingMobile!!.TaxNameText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.TaxNameText,applicationContext)
                    bindingMobile.TaxNameLayout.error = "Please enter registration number"
                    bindingMobile!!.propertyLayout.isErrorEnabled = false
                }
                else {
                    sendDataMobile()
                }
            }
            }
        }


    private fun sendData() {
        val userId = UserSessionManager(this).getUserId().toString()
        val starCategory = binding!!.ratingBar.rating.toString()
        val roomsInProperty = binding!!.roomCount.text.toString()
        val taxName = binding!!.taxNameText.text.toString()
        val registrationNumber = binding!!.registrationNumberText.text.toString()
        val ratePercent = binding!!.rateCount.text.toString()

            val secondOnboardingApi = RetrofitObject.authentication.secondOnboarding(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), starCategory),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), roomsInProperty),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), taxName),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), registrationNumber),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), ratePercent),
            )

            secondOnboardingApi.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val respons = response.body()!!

                        SharedPrefOnboardingFlags(applicationContext).saveSecondFlagValue(true)

                        Toast.makeText(
                            applicationContext,
                            respons.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = (Intent(this@ThirdSingleOnboardingScreen, FinalOnboardingScreen::class.java))

                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                        val intent = (Intent(this@ThirdSingleOnboardingScreen, FourOnboardingActivity::class.java))
                        val options = ActivityOptions.makeSceneTransitionAnimation(this@ThirdSingleOnboardingScreen,
                            android.util.Pair(binding!!.logo,"logo_img"),
                            android.util.Pair(binding!!.onBoardingImg,"onBoardingImg"),
                            android.util.Pair(binding!!.demoBackbtn,"backBtn")).toBundle()
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

    private fun sendDataMobile() {
        val userId = UserSessionManager(this).getUserId().toString()
        val starCategory = bindingMobile!!.ratingBar.rating.toString()
        val roomsInProperty = bindingMobile!!.roomCount.text.toString()
//        val taxName = bindingMobile!!.taxNameText.text.toString()
//        val registrationNumber = bindingMobile!!.registrationNumberText.text.toString()
        val ratePercent = bindingMobile!!.rateCount.text.toString()

            val secondOnboardingApi = RetrofitObject.retrofit.secondOnboarding(
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), userId),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), starCategory),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), roomsInProperty),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "taxName"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "registrationNumber"),
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), ratePercent),
            )

            secondOnboardingApi.enqueue(object : Callback<ResponseData?> {
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

                        val intent = (Intent(this@ThirdSingleOnboardingScreen, FinalOnboardingScreen::class.java))
//                        val options = ActivityOptions.makeSceneTransitionAnimation(this@ThirdSingleOnboardingScreen,
//                            android.util.Pair(bindingMobile!!.logo,"logo_img"),
//                            android.util.Pair(bindingMobile!!.onBoardingImg,"onBoardingImg"),
//                            android.util.Pair(bindingMobile!!.demoBackbtn,"backBtn")).toBundle()
//                        startActivity(intent, options)
                        startActivity(intent)
                        finish()
                        SharedPreference(applicationContext).saveSingleFlagValue(false)
                        SharedPreference(applicationContext).saveLoginFlagValue(true)
                        SharedPreference(applicationContext).saveSignUpFlagValue(false)

                    } else {
                        Log.d("Error Onboarding", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    Log.d("Error Onboarding", t.localizedMessage.toString())
                }
            })
    }

    private fun addData() {
//        dataList.add(
//            ThirdChainOnboardingDataClass(
//                bindingTab.propertyText.text.toString(),
//                bindingTab.addressText.text.toString(),
//                bindingTab.ratingBar.rating.toString(),
//                bindingTab.roomCount.text.toString()
//            )
//        )

        thirdChainOnboardingAdapter = ThirdChainOnboardingAdapter(applicationContext, dataList)
        binding!!.recyclerView.adapter = thirdChainOnboardingAdapter
        thirdChainOnboardingAdapter.notifyDataSetChanged()

        binding!!.propertyText.text?.clear()
    }
    private fun addDataMobile() {
//        dataList.add(
//            ThirdChainOnboardingDataClass(
//                bindingTab.propertyText.text.toString(),
//                bindingTab.addressText.text.toString(),
//                bindingTab.ratingBar.rating.toString(),
//                bindingTab.roomCount.text.toString()
//            )
//        )

        thirdChainOnboardingAdapter = ThirdChainOnboardingAdapter(applicationContext, dataList)
        bindingMobile!!.recyclerView.adapter = thirdChainOnboardingAdapter
        thirdChainOnboardingAdapter.notifyDataSetChanged()

        bindingMobile!!.propertyText.text?.clear()
    }

}