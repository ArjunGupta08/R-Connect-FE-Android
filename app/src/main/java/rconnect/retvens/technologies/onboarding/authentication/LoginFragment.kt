package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.LoginRequest
import rconnect.retvens.technologies.LoginResponse
import rconnect.retvens.technologies.dashboard.DashboardActivity
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.utils.SharedPrefOnboardingFlags
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class LoginFragment : Fragment() {

    lateinit var binding : FragmentLoginBinding
     var userName = ""
     var authCode = ""
     var password = ""

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
                userName = binding.username.text.toString()
                authCode = binding.authCode.text.toString()
                password = binding.password.text.toString()
                binding.usernameLayout.isErrorEnabled = false
                binding.authCodeLayout.isErrorEnabled = false
                binding.passwordLayout.isErrorEnabled = false
                try {
                    getLogin()
                }
                catch (e:Exception){
                    println(e.toString())
                }
            }
        }


    }

    private fun getLogin() {
        val apiClient = RetrofitObject.authentication.login(LoginRequest(userName,authCode,password,"Android"))

        UserSessionManager(requireContext()).savePropertyId(authCode)

        apiClient.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "Login SuccessFull", Toast.LENGTH_SHORT).show()

                    SharedPrefOnboardingFlags(requireContext()).saveLoginFlagValue(true)
                    UserSessionManager(requireContext()).saveUserData(response.body()!!.data!!.userId, response.body()!!.data!!.token)

                    val intent = Intent(requireContext(), DashboardActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else{
                    Log.e("error",response.message().toString())
                    Toast.makeText(requireContext(), "Incorrect details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

}