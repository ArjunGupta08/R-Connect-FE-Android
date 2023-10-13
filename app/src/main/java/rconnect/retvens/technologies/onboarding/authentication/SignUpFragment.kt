package rconnect.retvens.technologies.onboarding.authentication

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentSignUpBinding
import rconnect.retvens.technologies.onboarding.FirstOnBoardingScreen

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
            val intent = Intent(requireContext(), FirstOnBoardingScreen::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
                android.util.Pair(binding.cardCreateAccount,"Btn")).toBundle())
//            }
        }

    }

    private fun showSnackBarMessage(message:String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//        Snackbar.make(applicationContext,parentLayout,message,Snackbar.LENGTH_SHORT).show()
    }

}