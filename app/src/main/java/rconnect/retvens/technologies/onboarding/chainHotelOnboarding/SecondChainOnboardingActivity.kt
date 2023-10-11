package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySecondChainOnboardingBinding


class SecondChainOnboardingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySecondChainOnboardingBinding

    var propertyCount : Int = 1

    private lateinit var imageUri: Uri
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var isImageSelected = false
    private var isImageEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondChainOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener { onBackPressed() }

        binding.cardSingleNext.setOnClickListener {
            val intent = Intent(this, ThirdChainOnboardingScreen::class.java)

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(binding.logo,"logo_img"),
                android.util.Pair(binding.onBoardingImg,"onBoardingImg"),
                android.util.Pair(binding.cardSingleNext,"Btn"),
                android.util.Pair(binding.demoBackbtn,"backBtn")).toBundle()

            startActivity(intent, options)
        }

        binding.replaceImage.setOnClickListener {
            openGallery()
            rightToLeftEditImageAnimation(binding.imageEditLayout)
        }

        binding.removeImage.setOnClickListener {
            imageUri = Uri.EMPTY
            binding.selectImg.setImageResource(R.drawable.svg_gallery)
            rightToLeftEditImageAnimation(binding.imageEditLayout)
            isImageSelected = false
            setMargins(binding.selectImg, 15, 15, 15, 15)
        }

        binding.cardImg.setOnClickListener {
            if (!isImageSelected){
                openGallery()
            } else {
                if (!isImageEdit) {
                    binding.imageEditLayout.isVisible = true
                    // load the animation
                    val animFadein: Animation = AnimationUtils.loadAnimation(
                        applicationContext,
                        R.anim.l_to_r_in_animation
                    )
                    // start the animation
                    binding.imageEditLayout.startAnimation(animFadein)
                    isImageEdit = true
                } else {
                    rightToLeftEditImageAnimation(binding.imageEditLayout)
                }
            }
        }

        binding.propertyCountEditText.doAfterTextChanged {

            if (binding.propertyCountEditText.text.toString() != "") {
                var propertyCountET = binding.propertyCountEditText.text.toString()
                propertyCount = propertyCountET.toInt()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.add.setOnClickListener {
            propertyCount += 1
            binding.propertyCountEditText.setText("$propertyCount")
        }

        binding.remove.setOnClickListener {
            if (propertyCount > 1) {
                propertyCount -= 1
                binding.propertyCountEditText.setText("$propertyCount")
            }
        }

    }

    private fun rightToLeftEditImageAnimation(view: View) {
        isImageEdit = false
        // load the animation
        val animSlideIn: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.l_to_r_out_animation
        )
        // start the animation
        view.startAnimation(animSlideIn)
        binding.imageEditLayout.isVisible = false
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
                    isImageSelected = true
                    binding.selectImg.setImageURI(imageUri)
                    setMargins(binding.selectImg, 0, 0, 0, 0)
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }
}