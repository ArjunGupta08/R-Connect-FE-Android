package rconnect.retvens.technologies.dashboard.singleDashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.ActivitySingleDashboardBinding

class SingleDashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySingleDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}