package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class ThirdChainOnboardingScreen : AppCompatActivity() {

    private lateinit var binding:ActivityThirdChainOnboardingScreenBinding

    private lateinit var thirdChainOnboardingAdapter: ThirdChainOnboardingAdapter
    private var dataList = ArrayList<ThirdChainOnboardingDataClass>()

    private var roomCount = 1
    private var rateCount = 1.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdChainOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener { onBackPressed() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        binding.save.setOnClickListener {
            addData()
        }

        binding.roomCount.doAfterTextChanged {

            if (binding.roomCount.text.toString() != "") {
                var roomCountET = binding.roomCount.text.toString()
                roomCount = roomCountET.toInt()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }

        }

        binding.removeRooms.setOnClickListener {
            if (roomCount > 1){
                roomCount--
                binding.roomCount.setText("$roomCount")
            }
        }
        binding.addRooms.setOnClickListener {
                roomCount++
                binding.roomCount.setText("$roomCount")
        }

        binding.rateCount.doAfterTextChanged {
            if (binding.rateCount.text.toString() != "") {
                var rateCountET = binding.rateCount.text.toString()
                rateCount = rateCountET.toDouble()
            } else {
//                Toast.makeText(applicationContext, "This field can not be empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.removeRate.setOnClickListener {
            if (rateCount > 1){
                rateCount--
                binding.rateCount.setText("$rateCount")
            }
        }
        binding.addRate.setOnClickListener {
                rateCount++
            binding.rateCount.setText("$rateCount")
        }

        binding.cardSingleNext.setOnClickListener {
            val intent = (Intent(this, FinalOnboardingScreen::class.java))

            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                android.util.Pair(binding.logo,"logo_img"),
                android.util.Pair(binding.onBoardingImg,"onBoardingImg"),
                android.util.Pair(binding.demoBackbtn,"backBtn")).toBundle()

            startActivity(intent, options)
        }

    }

    private fun addData() {
//        dataList.add(
//            ThirdChainOnboardingDataClass(
//                bindingTab.propertyText.text.toString(),
//                bindingTab.addressText.text.toString(),
//                bindingTab.ratingBar.rating.toString(),
//                bindingTab.roomCount.text.toString()
//            )
//        )

        thirdChainOnboardingAdapter = ThirdChainOnboardingAdapter(applicationContext, dataList)
        binding.recyclerView.adapter = thirdChainOnboardingAdapter
        thirdChainOnboardingAdapter.notifyDataSetChanged()

        binding.propertyText.text?.clear()
    }

}