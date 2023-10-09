package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondChainOnboardingBinding

class SecondChainOnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySecondChainOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondChainOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener { onBackPressed() }

        binding.cardSingleNext.setOnClickListener {
            val intent = Intent(this, ThirdChainOnboardingScreen::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(binding.logo,"logo_img"),
                android.util.Pair(binding.onBoardingImg,"onBoardingImg"),
                android.util.Pair(binding.cardSingleNext,"Btn"),
                android.util.Pair(binding.demoBackbtn,"backBtn")).toBundle()

            startActivity(intent, options)


        }

    }
}