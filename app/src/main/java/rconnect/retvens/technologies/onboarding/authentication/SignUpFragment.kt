package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentSignUpBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    lateinit var binding : FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardCreateAccount.setOnClickListener {

            if (binding.firstNameText.text!!.isEmpty()){
                shakeAnimation(binding.firstNameLayout,requireContext())
                binding.firstNameLayout.error = ( "Please enter your First Name" )
            } else if (binding.lastNameText.text!!.isEmpty()){
                shakeAnimation(binding.lastNameLayout, requireContext())
                binding.lastNameLayout.error = ("Please enter your Last Name")
            } else if (binding.phoneText.text!!.isEmpty()){
                shakeAnimation(binding.phoneLayout, requireContext())
                binding.phoneLayout.error  = ( "Please enter your Phone number" )
            } else if (binding.emailText.text!!.isEmpty()){
                shakeAnimation(binding.emailLayout, requireContext())
                binding.emailLayout.error = ("Please enter your email")
            } else if (binding .passwordText.text!!.isEmpty()){
                shakeAnimation(binding.passwordLayout, requireContext())
                binding.passwordLayout.error = ("Please enter your password")
            } else{
                signUp()
            }
        }

    }

    private fun signUp() {

        val signUpApi = RetrofitObject.retrofit.signUp(
            SignUpDataClass(
            binding.firstNameText.text.toString(),
            binding.lastNameText.text.toString(),
            binding.designationText.text.toString(),
            binding.phoneText.text.toString(),
            binding.emailText.text.toString(),
            binding.passwordText.text.toString()
        )
        )

        signUpApi.enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(
                call: Call<SignUpResponse?>,
                response: Response<SignUpResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), FirstOnBoardingScreen::class.java)
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                        android.util.Pair(binding.cardCreateAccount,"Btn")).toBundle())
                }else{
                    Log.e("error",response.body().toString())
                }
            }

            override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }

}