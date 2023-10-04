package rconnect.retvens.technologies.onboarding.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityCreateNewPasswordBinding

class CreateNewPasswordActivity : AppCompatActivity() {
    lateinit var binding:ActivityCreateNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotBackbtn.setOnClickListener {
            onBackPressed()
        }

    }
}