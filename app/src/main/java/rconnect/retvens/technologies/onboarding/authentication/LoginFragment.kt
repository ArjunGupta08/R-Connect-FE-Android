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
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.Dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.FragmentLoginBinding

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

            if (binding.authCode.text!!.isEmpty()) {
                showSnackBarMessage("Please enter Hotel R code")
            } else if (binding.username.text!!.isEmpty()) {
                showSnackBarMessage("Please enter Hotel R username")
            } else if (binding.password.text!!.isEmpty()) {
                showSnackBarMessage("Please enter password")
            } else {
                val intent = Intent(requireContext(), DashboardActivity::class.java)
                startActivity(intent)
            }
        }

    }

    private fun showSnackBarMessage(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(applicationContext,parentLayout,message,Snackbar.LENGTH_SHORT).show()
    }
}