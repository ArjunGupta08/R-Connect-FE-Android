package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySignUpScreenBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen

class SignUpScreen : AppCompatActivity() {

    private lateinit var bindingTab: ActivitySignUpScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivitySignUpScreenBinding.inflate(layoutInflater)
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(bindingTab.root)

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab.moveToLogin.setOnClickListener {
            val intent = (Intent(this, LoginScreen::class.java))

            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(bindingTab.logo,"logo_img"),
                android.util.Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                android.util.Pair(bindingTab.cardCreateAccount,"Btn"),
                android.util.Pair(bindingTab.loginTitle,"login_board")).toBundle())
        }

    }

}