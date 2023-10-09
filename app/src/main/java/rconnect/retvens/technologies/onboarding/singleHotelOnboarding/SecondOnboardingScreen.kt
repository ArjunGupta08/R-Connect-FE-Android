package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class SecondOnboardingScreen : AppCompatActivity() {

    private lateinit var bindingTab : ActivitySecondOnboardingScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivitySecondOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.cardSingleNext.setOnClickListener {
            val intent = Intent(this,FinalOnboardingScreen::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(bindingTab.logo,"logo_img"),
                android.util.Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                android.util.Pair(bindingTab.demoBackbtn,"backBtn")).toBundle()

            startActivity(intent, options)
        }

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

    }
}