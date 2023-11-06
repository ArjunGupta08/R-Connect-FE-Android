package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordBinding
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordMobileBinding
import rconnect.retvens.technologies.databinding.ActivityEnterVerificationCodeBinding
import rconnect.retvens.technologies.databinding.ActivityEnterVerificationCodeMobileBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenMobileBinding

class EnterVerificationCodeActivity : AppCompatActivity() {
    var binding: ActivityEnterVerificationCodeBinding? = null
    lateinit var bindingMobile:ActivityEnterVerificationCodeMobileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = ActivityEnterVerificationCodeBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // Landscape orientation for tablets

            binding = ActivityEnterVerificationCodeBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
        } else {
            // Portrait orientation for mobile (default or any other orientation)

            bindingMobile = ActivityEnterVerificationCodeMobileBinding.inflate(layoutInflater)
            setContentView(bindingMobile.root)
        }


        if (binding is ActivityEnterVerificationCodeBinding){

        binding!!.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }
        binding!!.submit.setOnClickListener {
            val intent = (Intent(this, CreateNewPasswordActivity::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair(binding!!.logo,"logo_img"),
                Pair(binding!!.onBoardingImg,"onBoardingImg"),
                Pair(binding!!.forgotBackbtn,"backBtn"),
                Pair(binding!!.submit,"Btn")
            ).toBundle()
            startActivity(intent, options)
        }
    }
        else{
            val intent = Intent(this,CreateNewPasswordActivity::class.java)
            bindingMobile.resetCard.setOnClickListener {
                startActivity(intent)
            }
        }

    }
}