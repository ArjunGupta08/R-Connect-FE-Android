package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySignUpScreenBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen

class SignUpScreen : AppCompatActivity() {

    private lateinit var bindingTab: ActivitySignUpScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivitySignUpScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab.moveToLogin.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }

        bindingTab.cardCreateAccount.setOnClickListener {
            startActivity(Intent(this,FirstOnBoardingScreen::class.java))
        }


    }
}