package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
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
            val intent = (Intent(this, EnterVerificationCodeActivity::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair(bindingTab.logo,"logo_img"),
                Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                Pair(bindingTab.forgotBackbtn,"backBtn"),
                Pair(bindingTab.resetCard,"Btn")
            ).toBundle()

            startActivity(intent, options)

        }
    }
}