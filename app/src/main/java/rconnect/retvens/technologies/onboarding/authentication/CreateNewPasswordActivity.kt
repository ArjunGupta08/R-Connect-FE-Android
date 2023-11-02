package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordBinding
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordMobileBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenMobileBinding

class CreateNewPasswordActivity : AppCompatActivity() {
     var binding:ActivityCreateNewPasswordBinding? = null
    lateinit var bindingMobile:ActivityCreateNewPasswordMobileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation
        when(currentOrientation){
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
                setContentView(binding!!.root)
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityCreateNewPasswordMobileBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
        }
//        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        if (binding is ActivityCreateNewPasswordBinding){
        binding!!.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }
    }
        else{
            bindingMobile.resetCard.setOnClickListener {
                Toast.makeText(applicationContext, "Reset", Toast.LENGTH_SHORT).show()
            }
        }





}
}