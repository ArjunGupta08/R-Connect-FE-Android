package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityForgetPasswordScreenBinding

class ForgetPasswordScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityForgetPasswordScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityForgetPasswordScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab.resetCard.setOnClickListener {
            startActivity(Intent(this, EnterVerificationCodeActivity::class.java))
        }
    }
}