package rconnect.retvens.technologies.dashboard.chainDashboard

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.chainDashboard.addPropertyFrags.AddPropertyFragment_2
import rconnect.retvens.technologies.dashboard.chainDashboard.addPropertyFrags.AddPropertyFragment_1
import rconnect.retvens.technologies.dashboard.chainDashboard.addPropertyFrags.AddPropertyFragment_3
import rconnect.retvens.technologies.databinding.ActivityChainDashboardBinding

class ChainDashboardActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChainDashboardBinding

    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var roboto : Typeface
    private lateinit var robotoBold : Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roboto = ResourcesCompat.getFont(this, R.font.roboto)!!
        robotoBold = ResourcesCompat.getFont(this, R.font.roboto_bold)!!

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

        replaceFragment(AddPropertyFragment_1())

        binding.addPropertyBtn.setOnClickListener {
            binding.welcomeLayout.isVisible = false
            binding.addPropertyLayout.isVisible = true
        }

        binding.cancel.setOnClickListener {
            binding.welcomeLayout.isVisible = true
            binding.addPropertyLayout.isVisible = false
        }

        binding.propertyProfile.setOnClickListener {
            replaceFragment(AddPropertyFragment_1())
            binding.propertyProfile.textSize = 20.0f
            binding.propertyProfile.typeface = robotoBold
        }
        binding.addressAndContacts.setOnClickListener {
            replaceFragment(AddPropertyFragment_2())
            binding.addressAndContacts.textSize = 20.0f
            binding.addressAndContacts.typeface = robotoBold
            binding.propertyProfile.textSize = 18.0f
            binding.propertyProfile.typeface = roboto
        }
        binding.roomsAndAmenities.setOnClickListener {
            replaceFragment(AddPropertyFragment_3())
            binding.roomsAndAmenities.textSize = 20.0f
            binding.roomsAndAmenities.typeface = robotoBold
            binding.propertyProfile.textSize = 18.0f
            binding.propertyProfile.typeface = roboto
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.propertyFragmentContainer,fragment)
            transaction.commit()
        }
        binding.addressAndContacts.typeface = roboto
        binding.addressAndContacts.textSize = 18.0f
        binding.roomsAndAmenities.typeface = roboto
        binding.roomsAndAmenities.textSize = 18.0f
    }
}