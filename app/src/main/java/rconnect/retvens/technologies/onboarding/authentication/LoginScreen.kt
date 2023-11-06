package rconnect.retvens.technologies.onboarding.authentication

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityLoginMobileScreenBinding
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import rconnect.retvens.technologies.utils.leftInAnimation
import rconnect.retvens.technologies.utils.rightInAnimation

class LoginScreen : AppCompatActivity() {

    private  var binding: ActivityLoginScreenBinding? = null
    private lateinit var bindingMobile : ActivityLoginMobileScreenBinding
    private lateinit var parentLayout : ConstraintLayout

    private lateinit var roboto : Typeface
    private lateinit var robotoBold : Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // Landscape orientation for tablets

            binding = ActivityLoginScreenBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
        } else {
            // Portrait orientation for mobile (default or any other orientation)
            bindingMobile = ActivityLoginMobileScreenBinding.inflate(layoutInflater)
            setContentView(bindingMobile.root)
        }

//        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)


        roboto = ResourcesCompat.getFont(applicationContext, R.font.roboto)!!
        robotoBold = ResourcesCompat.getFont(applicationContext, R.font.roboto_bold)!!



        if (binding is ActivityLoginScreenBinding){
            Toast.makeText(applicationContext, "Tablet", Toast.LENGTH_SHORT).show()

            parentLayout = findViewById(R.id.parent_layout)

            replaceFragment(SignUpFragment())
            bottomSlideInAnimation(binding!!.authFragContainer, applicationContext)

            binding!!.openSignUpScreen.setOnClickListener {

                replaceFragment(SignUpFragment())

                rightInAnimation(binding!!.authFragContainer, applicationContext)
                binding!!.onBoardingImg.setImageResource(R.drawable.vector_request_demo)
                rightInAnimation(binding!!.onBoardingImg, applicationContext)

//                binding!!.openSignUpScreen.text = getString(R.string.sign_up)
//                binding!!.openSignUpScreen.typeface = robotoBold
//                binding!!.openSignUpScreen.setTextSize(16f)
//                binding!!.openSignUpScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
//                binding!!.openSignUpScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_right_black_background)

                setText(binding!!.openSignUpScreen,"Sign up",16f,robotoBold,R.color.white,R.drawable.corner_right_black_background)

//                binding!!.openLoginScreen.text = getString(R.string.existing_user_log_in)
//                binding!!.openLoginScreen.typeface = roboto
//                binding!!.openLoginScreen.setTextSize(14f)
//                binding!!.openLoginScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
//                binding!!.openLoginScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_left_grey_background)

                setText(binding!!.openLoginScreen,"Existing User?\nLog in",14f,roboto,R.color.black,R.drawable.corner_left_grey_background)


            }

            binding!!.openLoginScreen.setOnClickListener {

                replaceFragment(LoginFragment())

                leftInAnimation( binding!!.authFragContainer, applicationContext)
                binding!!.onBoardingImg.setImageResource(R.drawable.vector_login)
                leftInAnimation( binding!!.onBoardingImg, applicationContext)

//                binding!!.openSignUpScreen.text = getString(R.string.new_here_sign_up)
//                binding!!.openSignUpScreen.typeface = roboto
//                binding!!.openSignUpScreen.setTextSize(14f)
//                binding!!.openSignUpScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
//                binding!!.openSignUpScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_right_grey_background)
                setText(binding!!.openSignUpScreen,"New here?\nSign up",14f,roboto,R.color.black,R.drawable.corner_right_grey_background)


//                binding!!.openLoginScreen.text = getString(R.string.log_in)
//                binding!!.openLoginScreen.typeface = robotoBold
//                binding!!.openLoginScreen.setTextSize(16f)
//                binding!!.openLoginScreen.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
//                binding!!.openLoginScreen.background = ContextCompat.getDrawable(applicationContext, R.drawable.corner_left_black_background)
                setText(binding!!.openLoginScreen,"Log in",16f,robotoBold,R.color.white,R.drawable.corner_left_black_background)




            }
        }else{
            Toast.makeText(applicationContext, "Mobile", Toast.LENGTH_SHORT).show()
            replaceMobileFragment(LoginFragment())
            bindingMobile.openLoginScreen.setOnClickListener {
                replaceMobileFragment(LoginFragment())
                leftInAnimation( bindingMobile!!.authFragContainerMobile, applicationContext)
                setText(bindingMobile!!.openSignUpScreen,"New here?\nSign up",12f,roboto,R.color.black,R.drawable.mobile_corner_right_grey_background)

                setText(bindingMobile!!.openLoginScreen,"Log in",14f,robotoBold,R.color.white,R.drawable.mobile_corner_left_black_background)
                bindingMobile.openLoginScreen.elevation = 9f
                bindingMobile.openSignUpScreen.elevation = 0f

            }
            bindingMobile.openSignUpScreen.setOnClickListener {
                replaceMobileFragment(SignUpFragment())
                rightInAnimation(bindingMobile!!.authFragContainerMobile, applicationContext)
                setText(bindingMobile!!.openSignUpScreen,"Sign up",14f,robotoBold,R.color.white,R.drawable.mobile_corner_left_black_background)
                bindingMobile.openSignUpScreen.elevation = 9f
                bindingMobile.openLoginScreen.elevation = 0f

                setText(bindingMobile!!.openLoginScreen,"Existing User?\nLog in",12f,roboto,R.color.black,R.drawable.mobile_corner_right_grey_background)

            }

        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.authFragContainer,fragment)
            transaction.commit()
        }
    }

    private fun replaceMobileFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.authFragContainerMobile,fragment)
            transaction.commit()
        }
    }

    private fun setText(textView:TextView, text:String, size: Float, typFace: Typeface,color:Int,setBackground:Int ){
        textView.text = text
        textView.textSize = size
        textView.typeface = typFace
        textView.setTextColor(ContextCompat.getColor(applicationContext,color))
        textView.background = ContextCompat.getDrawable(applicationContext,setBackground)
    }

}