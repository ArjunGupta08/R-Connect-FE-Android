package rconnect.retvens.technologies.onboarding

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityFirstOnBoardingScreenBinding
import rconnect.retvens.technologies.databinding.ActivityFirstOnboardingScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenMobileBinding
import rconnect.retvens.technologies.onboarding.chainHotelOnboarding.SecondChainOnboardingActivity
import rconnect.retvens.technologies.onboarding.singleHotelOnboarding.SecondOnboardingScreen
import rconnect.retvens.technologies.utils.SharedPreference

class FirstOnBoardingScreen : AppCompatActivity() {

    private var bindingTab:ActivityFirstOnBoardingScreenBinding? = null
    private lateinit var bindingMobile:ActivityFirstOnboardingScreenMobileBinding
    

    private var isSingleSelected = false
    private var isChainSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation
        when(currentOrientation){

            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                bindingTab = ActivityFirstOnBoardingScreenBinding.inflate(layoutInflater)
                setContentView(bindingTab!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityFirstOnboardingScreenMobileBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
            
        }
//        bindingTab = ActivityFirstOnBoardingScreenBinding.inflate(layoutInflater)
//        setContentView(bindingTab!!.root)
        if (bindingTab is ActivityFirstOnBoardingScreenBinding){

        bindingTab!!.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab!!.cardSingle.setOnClickListener {

            isSingleSelected = true
            isChainSelected = false
            

            val colorResId = R.color.grey10 // Replace with your color resource ID
            val color = ContextCompat.getColor(applicationContext, colorResId)
            bindingTab!!.cardChain.strokeColor = color
//            bindingTab.text1.setTextColor(color)

            val colorResId1 = R.color.black // Replace with your color resource ID
            val color1 = ContextCompat.getColor(applicationContext, colorResId1)

            bindingTab!!.cardSingle.strokeColor = color1
//            bindingTab.text2.setTextColor(color1)
//            bindingTab.cardSingle.strokeWidth = 3
//            bindingTab.cardChain.strokeWidth = 3
        }

        bindingTab!!.cardChain.setOnClickListener {

            isChainSelected = true
            isSingleSelected = false

            val colorResId = R.color.grey10 // Replace with your color resource ID
            val color = ContextCompat.getColor(applicationContext, colorResId)
            bindingTab!!.cardSingle.strokeColor = color
//
            val colorResId1 = R.color.black // Replace with your color resource ID
            val color1 = ContextCompat.getColor(applicationContext, colorResId1)
            bindingTab!!.cardChain.strokeColor = color1
//            bindingTab.cardChain.strokeWidth = 3
//            bindingTab.cardSingle.strokeWidth = 3
        }

        bindingTab!!.cardSingleNext.setOnClickListener {
            if (isSingleSelected) {
                val intent = Intent(this, SecondOnboardingScreen::class.java)

                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    android.util.Pair(bindingTab!!.logo,"logo_img"),
                    android.util.Pair(bindingTab!!.onBoardingImg,"onBoardingImg"),
                    android.util.Pair(bindingTab!!.cardSingleNext,"Btn"),
                    android.util.Pair(bindingTab!!.demoBackbtn,"backBtn")).toBundle()

                startActivity(intent, options)
//                SharedPreference(applicationContext).saveSignUpFlagValue(false)

            } else if (isChainSelected){
                val intent = Intent(this, SecondChainOnboardingActivity::class.java)

                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    android.util.Pair(bindingTab!!.logo,"logo_img"),
                    android.util.Pair(bindingTab!!.onBoardingImg,"onBoardingImg"),
                    android.util.Pair(bindingTab!!.cardSingleNext,"Btn"),
                    android.util.Pair(bindingTab!!.demoBackbtn,"backBtn")).toBundle()

                startActivity(intent, options)

            } else{
                Toast.makeText(this, "Please Select Property Type", Toast.LENGTH_SHORT).show()
            }
        }

        }
        else{


//            Mobile works starts here:-

            Toast.makeText(applicationContext, "Mobile", Toast.LENGTH_SHORT).show()
            bindingMobile!!.cardSingle.setOnClickListener {

                isSingleSelected = true
                isChainSelected = false


                val colorResId = R.color.grey10 // Replace with your color resource ID
                val color = ContextCompat.getColor(applicationContext, colorResId)
                bindingMobile!!.cardChain.strokeColor = color
//            bindingTab.text1.setTextColor(color)

                val colorResId1 = R.color.black // Replace with your color resource ID
                val color1 = ContextCompat.getColor(applicationContext, colorResId1)

                bindingMobile!!.cardSingle.strokeColor = color1
//            bindingTab.text2.setTextColor(color1)
//            bindingTab.cardSingle.strokeWidth = 3
//            bindingTab.cardChain.strokeWidth = 3
            }

            bindingMobile!!.cardChain.setOnClickListener {

                isChainSelected = true
                isSingleSelected = false

                val colorResId = R.color.grey10 // Replace with your color resource ID
                val color = ContextCompat.getColor(applicationContext, colorResId)
                bindingMobile!!.cardSingle.strokeColor = color
//
                val colorResId1 = R.color.black // Replace with your color resource ID
                val color1 = ContextCompat.getColor(applicationContext, colorResId1)
                bindingMobile!!.cardChain.strokeColor = color1
//            bindingTab.cardChain.strokeWidth = 3
//            bindingTab.cardSingle.strokeWidth = 3
            }

            bindingMobile!!.cardSingleNext.setOnClickListener {
                if (isSingleSelected) {
                    val intent = Intent(this, SecondOnboardingScreen::class.java)
                    startActivity(intent)




                } else if (isChainSelected){
                    val intent = Intent(this, SecondChainOnboardingActivity::class.java)
                    startActivity(intent)

                } else{
                    Toast.makeText(this, "Please Select Property Type", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}