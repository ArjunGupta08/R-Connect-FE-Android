package rconnect.retvens.technologies.onboarding.singleHotelOnboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivityFourOnboardingBinding
import rconnect.retvens.technologies.onboarding.FinalOnboardingScreen

class FourOnboardingActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFourOnboardingBinding
    private lateinit var roomDetailsAdapter: FourOnboardingRoomAdapter

    private var list = ArrayList<FourOnboardingRoomDataClass>()

    private var maxOccupancy = 1
    private var maxAdults = 1
    private var maxChild = 1
    private var roomInventory = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFourOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.demoBackbtn.setOnClickListener {
            onBackPressed()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        binding.save.setOnClickListener {
            if (binding.roomTypeText.text!!.isNotEmpty()) {
                addRoomData(false)
            } else {
                Snackbar.make(binding.roomTypeLayout, "Add Room Data", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.cancel.setOnClickListener {
            addRoomData(true)
            roomDetailsAdapter = FourOnboardingRoomAdapter(this, list)
            binding.recyclerView.adapter = roomDetailsAdapter
            roomDetailsAdapter.notifyDataSetChanged()
            binding.addMore.isVisible = false
        }

        binding.addMaxOccupancy.setOnClickListener {
            maxOccupancy = +1
            binding.maxOccupancyCount.text = "$maxOccupancy"
        }
        binding.addRoomInventory.setOnClickListener {
            roomInventory = +1
            binding.roomsInventoryCount.text = "$roomInventory"
        }
        binding.addMaxAdult.setOnClickListener {
            maxAdults = +1
            binding.maxAdultCount.text = "$maxAdults"
        }
        binding.addMaxChild.setOnClickListener {
            maxChild = +1
            binding.maxChildCount.text = "$maxChild"
        }

        binding.removeMaxOccupancy.setOnClickListener {
            if (maxOccupancy > 1) {
                maxOccupancy--
                binding.maxOccupancyCount.text = "$maxOccupancy"
            }
        }
        binding.removeRoomInventory.setOnClickListener {
            if (roomInventory > 1) {
                roomInventory--
                binding.roomsInventoryCount.text = "$roomInventory"
            }
        }
        binding.removeMaxAdult.setOnClickListener {
            if (maxAdults > 1) {
                maxAdults--
                binding.maxAdultCount.text = "$maxAdults"
            }
        }
        binding.removeMaxChild.setOnClickListener {
            if (maxChild > 1) {
                maxChild--
                binding.maxChildCount.text = "$maxChild"
            }
        }

        binding.cardSingleNext.setOnClickListener {
            val intent = Intent(this, FinalOnboardingScreen::class.java)
            intent.putExtra("isSingle", true)
            startActivity(intent)
        }

    }

    private fun addRoomData(clearingList : Boolean) {
        if (clearingList){
            list.clear()
        } else {
            list.add(
                FourOnboardingRoomDataClass(
                    binding.roomsInventoryCount.text.toString(),
                    binding.roomTypeText.text.toString(),
                    binding.maxOccupancyCount.text.toString(),
                    binding.maxChildCount.text.toString(),
                    binding.maxAdultCount.text.toString()
                )
            )
        }

        binding.roomTypeText.text!!.clear()
        binding.roomsInventoryCount.text = "1"
        binding.maxOccupancyCount.text = "1"
        binding.maxChildCount.text = "1"
        binding.maxAdultCount.text = "1"

        roomDetailsAdapter = FourOnboardingRoomAdapter(this, list)
        binding.recyclerView.adapter = roomDetailsAdapter
        roomDetailsAdapter.notifyDataSetChanged()
        binding.addMore.isVisible = true

    }
}