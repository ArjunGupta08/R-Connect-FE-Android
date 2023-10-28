package rconnect.retvens.technologies.onboarding.authentication

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityLoginMobileScreenBinding
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import rconnect.retvens.technologies.utils.leftInAnimation
import rconnect.retvens.technologies.utils.rightInAnimation

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var bindingMobile : ActivityLoginMobileScreenBinding
    private lateinit var parentLayout : ConstraintLayout

    private lateinit var roboto : Typeface
    private lateinit var robotoBold : Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentOrientation = resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivityLoginScreenBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityLoginMobileScreenBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
        }

        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        roboto = ResourcesCompat.getFont(applicationContext, R.font.roboto)!!
        robotoBold = ResourcesCompat.getFont(applicationContext, R.font.roboto_bold)!!



        if (binding is ActivityLoginScreenBinding){

            parentLayout = findViewById(R.id.parent_layout)

            replaceFragment(SignUpFragment())
            bottomSlideInAnimation(binding.authFragContainer, applicationContext)

            binding.openSignUpScreen.setOnClickListener {

                replaceFragment(SignUpFragment())

                rightInAnimation(binding.authFragContainer, applicationContext)
                binding.onBoardingImg.setImageResource(R.drawable.vector_request_demo)
                rightInAnimation(binding.onBoardingImg, applicationContext)

                binding.openSignUpScreen.text = getString(R.string.sign_up)
                binding.openSignUpScreen.typeface = robotoBold
                binding.openSignUpScreen.setTextSize(16f)
                binding.openSignUpScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                binding.openSignUpScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_right_black_background)

                binding.openLoginScreen.text = getString(R.string.existing_user_log_in)
                binding.openLoginScreen.typeface = roboto
                binding.openLoginScreen.setTextSize(14f)
                binding.openLoginScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                binding.openLoginScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_left_grey_background)

            }

            binding.openLoginScreen.setOnClickListener {

                replaceFragment(LoginFragment())

                leftInAnimation(binding.authFragContainer, applicationContext)
                binding.onBoardingImg.setImageResource(R.drawable.vector_login)
                leftInAnimation(binding.onBoardingImg, applicationContext)

                binding.openSignUpScreen.text = getString(R.string.new_here_sign_up)
                binding.openSignUpScreen.typeface = roboto
                binding.openSignUpScreen.setTextSize(14f)
                binding.openSignUpScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                binding.openSignUpScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_right_grey_background)

                binding.openLoginScreen.text = getString(R.string.log_in)
                binding.openLoginScreen.typeface = robotoBold
                binding.openLoginScreen.setTextSize(16f)
                binding.openLoginScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                binding.openLoginScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_left_black_background)

            }
        }else{
            Toast.makeText(applicationContext,"Nothing to show",Toast.LENGTH_LONG).show()
        }



    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.authFragContainer,fragment)
            transaction.commit()
        }
    }

}