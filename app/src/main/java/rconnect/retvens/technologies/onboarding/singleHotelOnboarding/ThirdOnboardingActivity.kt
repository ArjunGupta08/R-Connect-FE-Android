package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityThirdOnboardingBinding

class ThirdOnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThirdOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        binding.cardSingleNext.setOnClickListener {
            startActivity(Intent(this, FourOnboardingActivity::class.java))
        }

    }
}