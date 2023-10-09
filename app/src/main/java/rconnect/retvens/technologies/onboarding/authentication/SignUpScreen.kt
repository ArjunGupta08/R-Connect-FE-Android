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

        bindingTab.cardCreateAccount.setOnClickListener {

//            if (bindingTab.firstNameText.text!!.isEmpty()){
//                showSnackBarMessage( "Please enter your First Name" )
//            } else if (bindingTab.lastNameText.text!!.isEmpty()){
//                showSnackBarMessage("Please enter your Last Name")
//            } else if (bindingTab.phoneText.text!!.isEmpty()){
//                showSnackBarMessage( "Please enter your Phone number" )
//            } else if (bindingTab.emailText.text!!.isEmpty()){
//                showSnackBarMessage("Please enter your email")
//            } else if (bindingTab.passwordText.text!!.isEmpty()){
//                showSnackBarMessage("Please enter your password")
//            } else{
                val intent = Intent(this,FirstOnBoardingScreen::class.java)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this,
                    android.util.Pair(bindingTab.logo,"logo_img"),
                    android.util.Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                    android.util.Pair(bindingTab.cardCreateAccount,"Btn"),
                    android.util.Pair(bindingTab.demoBackbtn,"backBtn")).toBundle())
//            }
        }


    }

    private fun showSnackBarMessage(message:String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(applicationContext,parentLayout,message,Snackbar.LENGTH_SHORT).show()
    }

}