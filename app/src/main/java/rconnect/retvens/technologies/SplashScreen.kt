package rconnect.retvens.technologies

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding
import rconnect.retvens.technologies.databinding.ActivitySplashScreenBinding
import rconnect.retvens.technologies.databinding.ActivitySplashScreenMobileBinding
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.databinding.FragmentLoginMobileBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen
import rconnect.retvens.technologies.onboarding.authentication.LoginScreen
import rconnect.retvens.technologies.onboarding.singleHotelOnboarding.ThirdSingleOnboardingScreen
import rconnect.retvens.technologies.utils.SharedPreference

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    var binding:ActivitySplashScreenBinding ? = null
    private lateinit var bindingMobile : ActivitySplashScreenMobileBinding


    private lateinit var parentLayout : ConstraintLayout

//    ThirdSingleOnboardingScreen -> if Single = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentOrientation = resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivitySplashScreenBinding.inflate(layoutInflater)
                setContentView(binding!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                Log.e("check","1")
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivitySplashScreenMobileBinding.inflate(layoutInflater) //create xml layout of splash screen named as ActivitySplashScreenMobile
                setContentView(bindingMobile.root)
            }
        }


        if (binding is ActivitySplashScreenBinding){
            Log.e("check","2")
            if (SharedPreference(applicationContext).getLoginFlagValue()){
                Handler().postDelayed(Runnable {
                 startActivity(Intent(this@SplashScreen,DashboardActivity::class.java))
                    finish()
                },1000)

            }
            else{
                Handler().postDelayed(Runnable{
                    startActivity(Intent(this@SplashScreen,LoginScreen::class.java))
                    finish()
                },1000)
            }
    }
        else{

            // Mobile work starts here


            Log.e("check","3")



            if (SharedPreference(applicationContext).getLoginFlagValue()){
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@SplashScreen,DashboardActivity::class.java))
                    finish()
                },1000)
            }

            else if(SharedPreference(applicationContext).getSingleFlagValue()){
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@SplashScreen,ThirdSingleOnboardingScreen::class.java))
                    finish()
                },3000)
            }
            else if (SharedPreference(applicationContext).getSignUpFlagValue()){
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this@SplashScreen,FirstOnBoardingScreen::class.java))
                    finish()
                },3000)
            }

            else{
                Handler().postDelayed(Runnable{
                    startActivity(Intent(this@SplashScreen,LoginScreen::class.java))
                    finish()
                },1000)

            }
        }
    }
}