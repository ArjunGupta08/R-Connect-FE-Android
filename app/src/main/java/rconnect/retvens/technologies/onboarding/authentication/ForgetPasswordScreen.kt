package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.WindowManager
import android.widget.Toast
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityLoginMobileScreenBinding
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding

class ForgetPasswordScreen : AppCompatActivity() {



    private  var binding: ActivityForgetPasswordScreenBinding? = null
    private lateinit var bindingMobile : ActivityForgetPasswordScreenMobileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         val currentOrientation = resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivityForgetPasswordScreenBinding.inflate(layoutInflater)
                setContentView(binding!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityForgetPasswordScreenMobileBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
        }

        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

//        bindingTab = ActivityForgetPasswordScreenBinding.inflate(layoutInflater)
//        setContentView(bindingTab.root)

        if (binding is ActivityForgetPasswordScreenBinding){
            Toast.makeText(applicationContext, "Tablet", Toast.LENGTH_SHORT).show()
            binding!!.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }

            binding!!.resetCard.setOnClickListener {
            val intent = (Intent(this, EnterVerificationCodeActivity::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair(binding!!.logo,"logo_img"),
                Pair(binding!!.onBoardingImg,"onBoardingImg"),
                Pair(binding!!.forgotBackbtn,"backBtn"),
                Pair(binding!!.resetCard,"Btn")
            ).toBundle()

            startActivity(intent, options)

        }
    }
        else {

            bindingMobile!!.resetCard.setOnClickListener {
                val intent = (Intent(this, EnterVerificationCodeActivity::class.java))
                startActivity(intent)
                Toast.makeText(applicationContext, "reset", Toast.LENGTH_SHORT).show()

//                val options = ActivityOptions.makeSceneTransitionAnimation(
//                    this,
//                    Pair(bindingMobile!!.logo, "logo_img"),
//                    Pair(bindingTab!!.onBoardingImg, "onBoardingImg"),
//                    Pair(bindingTab!!.forgotBackbtn, "backBtn"),
//                    Pair(bindingTab!!.resetCard, "Btn")
//                ).toBundle()

//                startActivity(intent, options)


            }
        }
    }
}