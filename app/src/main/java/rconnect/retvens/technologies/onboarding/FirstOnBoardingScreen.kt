package rconnect.retvens.technologies.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityFirstOnBoardingScreenBinding
import rconnect.retvens.technologies.onboarding.chainHotelOnboarding.SecondChainOnboardingActivity
import rconnect.retvens.technologies.onboarding.singleHotelOnboarding.SecondOnboardingScreen

class FirstOnBoardingScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityFirstOnBoardingScreenBinding

    private var isSingleSelected = false
    private var isChainSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityFirstOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab.cardSingle.setOnClickListener {

            isSingleSelected = true

            val colorResId = R.color.primary // Replace with your color resource ID
            val color = ContextCompat.getColor(applicationContext, colorResId)
            bindingTab.cardSingle.strokeColor = color
            bindingTab.text1.setTextColor(color)

            val colorResId1 = R.color.subtitle // Replace with your color resource ID
            val color1 = ContextCompat.getColor(applicationContext, colorResId1)

            bindingTab.cardChain.strokeColor = color1
            bindingTab.text2.setTextColor(color1)
        }

        bindingTab.cardChain.setOnClickListener {

            isChainSelected = true

            val colorResId = R.color.subtitle // Replace with your color resource ID
            val color = ContextCompat.getColor(applicationContext, colorResId)
            bindingTab.cardSingle.strokeColor = color
            bindingTab.text1.setTextColor(color)

            val colorResId1 = R.color.primary // Replace with your color resource ID
            val color1 = ContextCompat.getColor(applicationContext, colorResId1)

            bindingTab.cardChain.strokeColor = color1
            bindingTab.text2.setTextColor(color1)
        }

        bindingTab.cardSingleNext.setOnClickListener {
            if (isSingleSelected) {
                val intent = Intent(this, SecondOnboardingScreen::class.java)
                startActivity(intent)
            } else if (isChainSelected){
                val intent = Intent(this, SecondChainOnboardingActivity::class.java)
                startActivity(intent)
            } else{
                Toast.makeText(this, "Please Select Property Type", Toast.LENGTH_SHORT).show()
            }
        }

    }
}