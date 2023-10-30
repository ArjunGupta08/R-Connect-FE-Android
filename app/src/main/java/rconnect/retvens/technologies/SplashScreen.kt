package rconnect.retvens.technologies

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding
import rconnect.retvens.technologies.databinding.ActivitySplashScreenBinding
import rconnect.retvens.technologies.databinding.ActivitySplashScreenMobileBinding
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.databinding.FragmentLoginMobileBinding
import rconnect.retvens.technologies.onboarding.authentication.LoginScreen
import rconnect.retvens.technologies.utils.SharedPreference

class SplashScreen : AppCompatActivity() {
    var binding:ActivitySplashScreenBinding ? = null
    private lateinit var bindingMobile : ActivitySplashScreenMobileBinding

    val currentOrientation = resources.configuration.orientation
    private lateinit var parentLayout : ConstraintLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivitySplashScreenBinding.inflate(layoutInflater)
                setContentView(binding!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivitySplashScreenMobileBinding.inflate(layoutInflater) //create xml layout of splash screen named as ActivitySplashScreenMobile
                setContentView(bindingMobile.root)
            }
        }


        if (binding is ActivitySplashScreenBinding){

        setContentView(R.layout.activity_splash_screen)
            if (SharedPreference(applicationContext).getFlagValue()){
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
            if (SharedPreference(applicationContext).getFlagValue()){
//                Handler().postDelayed(Runnable {
//                    startActivity(Intent(this@SplashScreen,DashboardActivity::class.java))
//                    finish()
//                },1000)
                Toast.makeText(applicationContext, "mobile ", Toast.LENGTH_SHORT).show()

            }
            else{
//                Handler().postDelayed(Runnable{
//                    startActivity(Intent(this@SplashScreen,LoginMobileScreenActivity::class.java))
//                    finish()
//                },1000)
                Toast.makeText(applicationContext, "Mobile else", Toast.LENGTH_SHORT).show()
            }
        }
    }
}