package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordBinding
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordMobileBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityLoginMobileScreenBinding
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding

class CreateNewPasswordActivity : AppCompatActivity() {
     var binding:ActivityCreateNewPasswordBinding? = null
    lateinit var bindingMobile:ActivityCreateNewPasswordMobileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            // Landscape orientation for tablets

            binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
            setContentView(binding!!.root)
        } else {
            // Portrait orientation for mobile (default or any other orientation)

            bindingMobile = ActivityCreateNewPasswordMobileBinding.inflate(layoutInflater)
            setContentView(bindingMobile.root)
        }

        if (binding is ActivityCreateNewPasswordBinding) {
            binding!!.forgotBackbtn.setOnClickListener {
                onBackPressed()
            }
        } else {
            bindingMobile.resetCard.setOnClickListener {
                Toast.makeText(applicationContext, "Reset", Toast.LENGTH_SHORT).show()
            }
        }





}
}