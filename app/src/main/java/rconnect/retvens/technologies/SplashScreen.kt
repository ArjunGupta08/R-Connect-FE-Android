package rconnect.retvens.technologies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivitySplashScreenBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen
import rconnect.retvens.technologies.onboarding.authentication.LoginScreen
import rconnect.retvens.technologies.onboarding.singleHotelOnboarding.ThirdSingleOnboardingScreen
import rconnect.retvens.technologies.utils.SharedPrefOnboardingFlags

class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

                Handler().postDelayed(Runnable {

                    if (SharedPrefOnboardingFlags(applicationContext).getLoginFlagValue()) {
                        startActivity(Intent(this@SplashScreen, DashboardActivity::class.java))
                        finish()
                    } else if (SharedPrefOnboardingFlags(applicationContext).getSecondChainFlagValue()){
                        startActivity(Intent(this@SplashScreen, DashboardActivity::class.java))
                        finish()
                    } else if (SharedPrefOnboardingFlags(applicationContext).getSecondFlagValue()){
                        startActivity(Intent(this@SplashScreen, DashboardActivity::class.java))
                        finish()
                    } else if (SharedPrefOnboardingFlags(applicationContext).getFirstFlagValue()){
                        startActivity(Intent(this@SplashScreen, ThirdSingleOnboardingScreen::class.java))
                        finish()
                    } else if (SharedPrefOnboardingFlags(applicationContext).getSignUpFlagValue()){
                        startActivity(Intent(this@SplashScreen, FirstOnBoardingScreen::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
                        finish()
                    }

                },1000)
    }
}