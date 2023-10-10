package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.Dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityLoginScreenBinding
    private lateinit var parentLayout : ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityLoginScreenBinding.inflate(layoutInflater)
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(bindingTab.root)

        parentLayout = findViewById(R.id.parent_layout)

        bindingTab.forgotPassText.setOnClickListener {
            val intent = (Intent(this, ForgetPasswordScreen::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair(bindingTab.logo,"logo_img"),
                Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                Pair(bindingTab.signInCard,"Btn")).toBundle()

            startActivity(intent, options)

        }

        bindingTab.signUp.setOnClickListener {
            val intent = Intent(this, SignUpScreen::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(bindingTab.logo,"logo_img"),
                android.util.Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                android.util.Pair(bindingTab.signInCard,"Btn"),
                android.util.Pair(bindingTab.loginTitle,"login_board")).toBundle()

            startActivity(intent, options)
        }

        bindingTab.signInCard.setOnClickListener {

            if (bindingTab.authCode.text!!.isEmpty()) {
                showSnackBarMessage("Please enter Hotel R code")
            } else if (bindingTab.username.text!!.isEmpty()) {
                showSnackBarMessage("Please enter Hotel R username")
            } else if (bindingTab.password.text!!.isEmpty()) {
                showSnackBarMessage("Please enter password")
            } else {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun showSnackBarMessage(message:String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(applicationContext,parentLayout,message,Snackbar.LENGTH_SHORT).show()
    }
}