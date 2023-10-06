package rconnect.retvens.technologies.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.chainDashboard.ChainDashboardActivity
import rconnect.retvens.technologies.dashboard.singleDashboard.SingleDashboardActivity
import rconnect.retvens.technologies.databinding.ActivityFinalOnboardingScreenBinding

class FinalOnboardingScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityFinalOnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityFinalOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        Handler().postDelayed({
            bindingTab.dashLayout.isVisible = true
            bindingTab.congratulationLayout.isVisible = false

            bindingTab.coverImg.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.svg_final_onboarding))
        }, 3000)

        val isSingle = intent.getBooleanExtra("isSingle", false)

        bindingTab.goToDash.setOnClickListener {
            if (isSingle) {
                startActivity(Intent(this, SingleDashboardActivity::class.java))
            } else {
                startActivity(Intent(this, ChainDashboardActivity::class.java))
            }
        }

    }
}