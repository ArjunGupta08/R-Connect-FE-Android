package rconnect.retvens.technologies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivitySplashScreenBinding
import rconnect.retvens.technologies.onboarding.authentication.LoginScreen
import rconnect.retvens.technologies.utils.SharedPreference

class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            if (SharedPreference(applicationContext).getFlagValue()){
                Handler().postDelayed(Runnable {
                 startActivity(Intent(this@SplashScreen,DashboardActivity::class.java))
                    finish()
                },1000)

            }
            else{
                Handler().postDelayed(Runnable{
                    startActivity(Intent(this@SplashScreen,LoginScreen::class.java))
                    finish()
                },1000)
            }
    }
}