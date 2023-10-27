package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.databinding.FragmentLoginMobileBinding
import rconnect.retvens.technologies.utils.shakeAnimation

class LoginFragment : Fragment() {

    lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgotPassText.setOnClickListener {
            val intent = (Intent(requireContext(), ForgetPasswordScreen::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                Pair(binding.signInCard,"Btn")
            ).toBundle()

            startActivity(intent, options)
        }

        binding.signInCard.setOnClickListener {

            if (binding.username.text!!.isEmpty()) {
                shakeAnimation(binding.usernameLayout,requireContext())
                binding.usernameLayout.error = "Please enter username"

            } else if (binding.authCode.text!!.isEmpty()) {
                binding.authCodeLayout.error = "Please enter Hotel R code"
                shakeAnimation(binding.authCodeLayout,requireContext())
                binding.usernameLayout.isErrorEnabled = false

            } else if (binding.password.text!!.isEmpty()) {
                binding.passwordLayout.error = "Please enter password"
                shakeAnimation(binding.passwordLayout,requireContext())
                binding.authCodeLayout.isErrorEnabled = false

            } else {
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
                binding.usernameLayout.isErrorEnabled = false
                binding.authCodeLayout.isErrorEnabled = false
                binding.passwordLayout.isErrorEnabled = false
            }
        }


    }

    private fun showSnackBarMessage(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(applicationContext,parentLayout,message,Snackbar.LENGTH_SHORT).show()
    }
}