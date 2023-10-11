package rconnect.retvens.technologies.dashboard.Dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AddPropertyFragment
import rconnect.retvens.technologies.dashboard.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDashboardBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set the toolbar as the support action bar
        setSupportActionBar(binding.toolbar)

        // Enable the up button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.svg_menu)
        // Set click listener for the up button

        binding.toolbar.setNavigationOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val isSingle = intent.getBooleanExtra("isSingle", false)

        if (isSingle){
            binding.propertyTypeImage.setImageResource(R.drawable.png_bed)
            binding.letsText.text = "Let’s add rooms in properties"
            replaceFragment(AddRoomTypeFragment())
        } else {
            binding.propertyTypeImage.setImageResource(R.drawable.svg_add_property)
            binding.letsText.text = "Let’s add your properties"
            replaceFragment(AddPropertyFragment())
        }

        binding.addPropertyBtn.setOnClickListener {
            binding.welcomeLayout.isVisible = false
            binding.dashboardFragmentContainer.isVisible = true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,fragment)
            transaction.commit()
        }
    }
}