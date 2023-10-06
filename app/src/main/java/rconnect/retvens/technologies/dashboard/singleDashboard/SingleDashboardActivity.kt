package rconnect.retvens.technologies.dashboard.singleDashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.databinding.ActivitySingleDashboardBinding

class SingleDashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySingleDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(AddRoomTypeFragment())

        binding.addRoomsBtn.setOnClickListener {
            binding.welcomeLayout.isVisible = false
            binding.singleDashboardFragmentContainer.isVisible = true
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.singleDashboardFragmentContainer,fragment)
            transaction.commit()
        }
    }
}