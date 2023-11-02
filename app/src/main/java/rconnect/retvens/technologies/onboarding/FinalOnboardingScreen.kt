package rconnect.retvens.technologies.onboarding

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivityFinalOnboardingScreenBinding
import rconnect.retvens.technologies.databinding.ActivityFinalOnboardingScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityFirstOnboardingScreenMobileBinding
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenBinding
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenMobileBinding

class FinalOnboardingScreen : AppCompatActivity() {


    lateinit var bindingMobile:ActivityFinalOnboardingScreenMobileBinding

    private var bindingTab:ActivityFinalOnboardingScreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentOrientation = resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                bindingTab = ActivityFinalOnboardingScreenBinding.inflate(layoutInflater)
                setContentView(bindingTab!!.root)
            }

            else -> {
                // Portrait orientation (default or any other orientation)
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = ActivityFinalOnboardingScreenMobileBinding.inflate(layoutInflater)
                setContentView(bindingMobile.root)
            }
        }

        val isSingle = intent.getBooleanExtra("isSingle", false)

//        bindingTab = ActivityFinalOnboardingScreenBinding.inflate(layoutInflater)
//        setContentView(bindingTab.root)

        if (bindingTab is ActivityFinalOnboardingScreenBinding) {

            bindingTab!!.goToDash.setOnClickListener {

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("isSingle", isSingle)
                startActivity(intent)
                finish()
            }

        bindingTab!!.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        Handler().postDelayed({
            bindingTab!!.dashLayout.isVisible = true
            bindingTab!!.congratulationLayout.isVisible = false

            bindingTab!!.coverImg.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.svg_final_onboarding
                )
            )
        }, 3000)

    }

        // Mobile works starts here...



        else{
            Handler().postDelayed({
                bindingMobile!!.dashLayout.isVisible = true
                bindingMobile!!.congratulationLayout.isVisible = false

                bindingMobile!!.coverImg.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.svg_final_onboarding
                    )
                )
            }, 3000)

            val isSingle = intent.getBooleanExtra("isSingle", false)

            bindingMobile!!.goToDash.setOnClickListener {

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("isSingle", isSingle)
                startActivity(intent)
            }

        }

    }
}