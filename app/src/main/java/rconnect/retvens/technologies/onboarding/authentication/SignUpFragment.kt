package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.databinding.FragmentLoginMobileBinding
import rconnect.retvens.technologies.databinding.FragmentSignUpBinding
import rconnect.retvens.technologies.databinding.FragmentSignUpMobileBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen
import rconnect.retvens.technologies.utils.SharedPreference
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.shakeAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException

class SignUpFragment : Fragment() {

    var binding : FragmentSignUpBinding? = null
    private lateinit var bindingMobile : FragmentSignUpMobileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val currentOrientation = resources.configuration.orientation

        when (currentOrientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation

                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                binding = FragmentSignUpBinding.inflate(layoutInflater)
                return binding!!.root
            }
            else -> {
                // Portrait orientation (default or any other orientation)
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                bindingMobile = FragmentSignUpMobileBinding.inflate(layoutInflater)
                return bindingMobile!!.root
            }
        }
//        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (binding is FragmentSignUpBinding) {

            binding!!.cardCreateAccount.setOnClickListener {

                if (binding!!.firstNameText.text!!.isEmpty()) {
                    shakeAnimation(binding!!.firstNameLayout, requireContext())
                    binding!!.firstNameLayout.error = ("Please enter your First Name")
                } else if (binding!!.lastNameText.text!!.isEmpty()) {
                    shakeAnimation(binding!!.lastNameLayout, requireContext())
                    binding!!.lastNameLayout.error = ("Please enter your Last Name")
                } else if (binding!!.phoneText.text!!.isEmpty()) {
                    shakeAnimation(binding!!.phoneLayout, requireContext())
                    binding!!.phoneLayout.error = ("Please enter your Phone number")
                } else if (binding!!.emailText.text!!.isEmpty()) {
                    shakeAnimation(binding!!.emailLayout, requireContext())
                    binding!!.emailLayout.error = ("Please enter your email")
                } else if (binding!!.passwordText.text!!.isEmpty()) {
                    shakeAnimation(binding!!.passwordLayout, requireContext())
                    binding!!.passwordLayout.error = ("Please enter your password")
                } else {
                    signUp()
                }
            }

            selectDesignation()
        }
        else{

            bindingMobile!!.cardCreateAccount.setOnClickListener {

                if (bindingMobile!!.firstNameText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.firstNameLayout, requireContext())
                    bindingMobile.lastNameLayout.isErrorEnabled = false
                    bindingMobile.phoneLayout.isErrorEnabled = false
                    bindingMobile!!.emailLayout.isErrorEnabled = false
                    bindingMobile.passwordLayout.isErrorEnabled = false
                    bindingMobile!!.firstNameLayout.error = ("Please enter your First Name")
                } else if (bindingMobile!!.lastNameText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.lastNameLayout, requireContext())
                    bindingMobile!!.firstNameLayout.isErrorEnabled = false
                    bindingMobile.phoneLayout.isErrorEnabled = false
                    bindingMobile!!.emailLayout.isErrorEnabled = false
                    bindingMobile.passwordLayout.isErrorEnabled = false
                    bindingMobile!!.lastNameLayout.error = ("Please enter your Last Name")
                } else if (bindingMobile!!.phoneText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.phoneLayout, requireContext())
                    bindingMobile!!.firstNameLayout.isErrorEnabled = false
                    bindingMobile.lastNameLayout.isErrorEnabled = false
                    bindingMobile!!.emailLayout.isErrorEnabled = false
                    bindingMobile.passwordLayout.isErrorEnabled = false
                    bindingMobile!!.phoneLayout.error = ("Please enter your Phone number")
                } else if (bindingMobile!!.emailText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.emailLayout, requireContext())
                    bindingMobile!!.firstNameLayout.isErrorEnabled = false
                    bindingMobile.lastNameLayout.isErrorEnabled = false
                    bindingMobile.phoneLayout.isErrorEnabled = false
                    bindingMobile.passwordLayout.isErrorEnabled = false
                    bindingMobile!!.emailLayout.error = ("Please enter your email")
                } else if (bindingMobile!!.passwordText.text!!.isEmpty()) {
                    shakeAnimation(bindingMobile!!.passwordLayout, requireContext())
                    bindingMobile!!.firstNameLayout.isErrorEnabled = false
                    bindingMobile.lastNameLayout.isErrorEnabled = false
                    bindingMobile.phoneLayout.isErrorEnabled = false
                    bindingMobile!!.emailLayout.isErrorEnabled = false
                    bindingMobile!!.passwordLayout.error = ("Please enter your password")
                } else {
                    signUpMobile()
                }
            }


        }
    }


    private fun selectDesignation() {

        // Initialize Places API


        // Initialize the AutoCompleteTextView and adapter
        val autoCompleteTextView = binding!!.designationText
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        autoCompleteTextView.setAdapter(adapter)

        // Set up the Autocomplete request
        autoCompleteTextView.threshold = 1  // Minimum characters to start autocomplete
        autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedDesignation = adapter.getItem(position).toString()

            // Handle the selected address as needed
            autoCompleteTextView.setText(selectedDesignation)
        }

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Create a FindAutocompletePredictionsRequest
                val request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery(s.toString())
                    .build()

                // Perform the autocomplete request

                val getDesignation = RetrofitObject.retrofit.getDesignation()

                getDesignation.enqueue(object : Callback<DesignationDataClass?> {
                    override fun onResponse(
                        call: Call<DesignationDataClass?>,
                        response: Response<DesignationDataClass?>
                    ) {
                        if (response.isSuccessful){
                            val response = response.body()!!
                            val suggestionList = mutableListOf<String>()

                            for (prediction in response.data) {
                                suggestionList.add(prediction.designation)
                                Log.d("suggetion", suggestionList.toString())
                            }

                            // Update the adapter with the new suggestions
                            adapter.clear()
                            adapter.addAll(suggestionList)
                            autoCompleteTextView.setAdapter(adapter)
                            adapter.notifyDataSetChanged()
                            requireActivity().finish()


                        }else{
                            Log.e("error",response.code().toString())
                        }
                    }

                    override fun onFailure(call: Call<DesignationDataClass?>, t: Throwable) {
                        Log.e("error",t.message.toString())
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun signUp() {

        val signUpApi = RetrofitObject.retrofit.signUp(
            SignUpDataClass(
                binding!!.firstNameText.text.toString(),
                binding!!.lastNameText.text.toString(),
                binding!!.designationText.text.toString(),
                binding!!.phoneText.text.toString(),
                binding!!.emailText.text.toString(),
                binding!!.passwordText.text.toString()
        )
        )

        signUpApi.enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(
                call: Call<SignUpResponse?>,
                response: Response<SignUpResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    UserSessionManager(requireContext()).saveUserData(response.userId, "")
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), FirstOnBoardingScreen::class.java)
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                        android.util.Pair(binding!!.cardCreateAccount,"Btn")).toBundle())
                    requireActivity().finish()
                    SharedPreference(requireContext()).saveSignUpFlagValue(true)
                    requireActivity().finish()
                }else{
                    Log.e("error",response.body().toString())
                }
            }

            override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                Log.e("error",t.message.toString())
            }
        })

    }
    private fun signUpMobile() {

        val signUpApi = RetrofitObject.retrofit.signUp(
            SignUpDataClass(
                bindingMobile!!.firstNameText.text.toString(),
                bindingMobile!!.lastNameText.text.toString(),
                bindingMobile!!.designationText.text.toString(),
                bindingMobile!!.phoneText.text.toString(),
                bindingMobile!!.emailText.text.toString(),
                bindingMobile!!.passwordText.text.toString()
        )
        )

        signUpApi.enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(
                call: Call<SignUpResponse?>,
                response: Response<SignUpResponse?>
            ) {
                if (response.isSuccessful){
                    val response = response.body()!!
                    UserSessionManager(requireContext()).saveUserData(response.userId, "")
                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), FirstOnBoardingScreen::class.java)
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                        android.util.Pair(bindingMobile!!.cardCreateAccount,"Btn")).toBundle())
                    requireActivity().finish()
                    SharedPreference(requireContext()).saveSignUpFlagValue(true)
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