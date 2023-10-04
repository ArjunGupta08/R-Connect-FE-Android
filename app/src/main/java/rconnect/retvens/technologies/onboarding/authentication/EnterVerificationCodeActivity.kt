package rconnect.retvens.technologies.onboarding.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityEnterVerificationCodeBinding

class EnterVerificationCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEnterVerificationCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }
        binding.submit.setOnClickListener {
            startActivity(Intent(this, CreateNewPasswordActivity::class.java))
        }
    }
}