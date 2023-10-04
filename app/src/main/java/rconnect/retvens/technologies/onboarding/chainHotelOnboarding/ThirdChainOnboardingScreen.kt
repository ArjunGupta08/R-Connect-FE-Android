package rconnect.retvens.technologies.onboarding.chainHotelOnboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityThirdChainOnboardingScreenBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class ThirdChainOnboardingScreen : AppCompatActivity() {

    private lateinit var bindingTab:ActivityThirdChainOnboardingScreenBinding

    private lateinit var thirdChainOnboardingAdapter: ThirdChainOnboardingAdapter
    private var dataList = ArrayList<ThirdChainOnboardingDataClass>()
    private var roomCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingTab = ActivityThirdChainOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(bindingTab.root)

        bindingTab.demoBackbtn.setOnClickListener { onBackPressed() }

        bindingTab.recyclerView.layoutManager = LinearLayoutManager(this)
        bindingTab.recyclerView.setHasFixedSize(true)

        bindingTab.save.setOnClickListener {
            addData()
        }

        bindingTab.remove.setOnClickListener {
            if (roomCount > 1){
                roomCount--
                bindingTab.roomCount.text = roomCount.toString()
            }
        }
        bindingTab.add.setOnClickListener {
                roomCount++
                bindingTab.roomCount.text = roomCount.toString()
        }

        bindingTab.cardSingleNext.setOnClickListener {
            startActivity(Intent(this, FinalOnboardingScreen::class.java))
        }

    }

    private fun addData() {
        dataList.add(
            ThirdChainOnboardingDataClass(
                bindingTab.propertyText.text.toString(),
                bindingTab.addressText.text.toString(),
                bindingTab.ratingBar.rating.toString(),
                bindingTab.roomCount.text.toString()
            )
        )

        thirdChainOnboardingAdapter = ThirdChainOnboardingAdapter(applicationContext, dataList)
        bindingTab.recyclerView.adapter = thirdChainOnboardingAdapter
        thirdChainOnboardingAdapter.notifyDataSetChanged()

        bindingTab.propertyText.text?.clear()
        bindingTab.addressText.text?.clear()
    }

}