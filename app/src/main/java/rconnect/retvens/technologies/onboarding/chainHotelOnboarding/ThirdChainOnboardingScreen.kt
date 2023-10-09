package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class ThirdChainOnboardingScreen : AppCompatActivity() {

    private lateinit var binding:ActivityThirdChainOnboardingScreenBinding

    private lateinit var thirdChainOnboardingAdapter: ThirdChainOnboardingAdapter
    private var dataList = ArrayList<ThirdChainOnboardingDataClass>()
    private var roomCount = 1

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

//        bindingTab.remove.setOnClickListener {
//            if (roomCount > 1){
//                roomCount--
//                bindingTab.roomCount.text = roomCount.toString()
//            }
//        }
//        bindingTab.add.setOnClickListener {
//                roomCount++
//                bindingTab.roomCount.text = roomCount.toString()
//        }

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