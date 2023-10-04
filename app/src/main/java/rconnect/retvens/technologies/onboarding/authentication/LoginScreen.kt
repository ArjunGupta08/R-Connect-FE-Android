package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.dashboard.chainDashboard.ChainDashboardActivity
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityLoginScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.forgotPassText.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordScreen::class.java))
        }

        bindingTab.signUp.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
        }

        bindingTab.signInCard.setOnClickListener {
            startActivity(Intent(this, ChainDashboardActivity::class.java))
        }
    }
}