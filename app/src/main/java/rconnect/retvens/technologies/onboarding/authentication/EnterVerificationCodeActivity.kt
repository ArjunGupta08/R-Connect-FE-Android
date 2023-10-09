package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityEnterVerificationCodeBinding

class EnterVerificationCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEnterVerificationCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }
        binding.submit.setOnClickListener {
            val intent = (Intent(this, CreateNewPasswordActivity::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair(binding.logo,"logo_img"),
                Pair(binding.onBoardingImg,"onBoardingImg"),
                Pair(binding.forgotBackbtn,"backBtn"),
                Pair(binding.submit,"Btn")
            ).toBundle()

            startActivity(intent, options)
        }
    }
}