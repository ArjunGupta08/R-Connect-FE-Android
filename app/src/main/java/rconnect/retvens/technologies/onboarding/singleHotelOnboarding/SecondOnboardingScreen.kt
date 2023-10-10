package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class SecondOnboardingScreen : AppCompatActivity() {

    private lateinit var bindingTab : ActivitySecondOnboardingScreenBinding

    private lateinit var imageUri: Uri
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivitySecondOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.cardSingleNext.setOnClickListener {
            val intent = Intent(this,FinalOnboardingScreen::class.java)
            intent.putExtra("isSingle", true)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(bindingTab.logo,"logo_img"),
                android.util.Pair(bindingTab.onBoardingImg,"onBoardingImg"),
                android.util.Pair(bindingTab.demoBackbtn,"backBtn")).toBundle()

            startActivity(intent, options)
        }

        bindingTab.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        bindingTab.cardGallery.setOnClickListener { openGallery() }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            imageUri = data.data!!
            if (imageUri != null) {
                try {
                    bindingTab.galleryImg.setImageURI(imageUri)
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }

}