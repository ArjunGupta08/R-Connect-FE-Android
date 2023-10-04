package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondChainOnboardingBinding

class SecondChainOnboardingActivity : AppCompatActivity() {

    lateinit var binding : ActivitySecondChainOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondChainOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener { onBackPressed() }

        binding.cardSingleNext.setOnClickListener {
            startActivity(Intent(this, ThirdChainOnboardingScreen::class.java))
        }

    }
}