package rconnect.retvens.technologies.dashboard.Dashboard

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.RatesAndInventory.RatesAndInventoryFragment
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AddPropertyFragment
import rconnect.retvens.technologies.dashboard.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.promotions.PromotionsFragment
import rconnect.retvens.technologies.databinding.ActivityDashboardBinding
import rconnect.retvens.technologies.utils.bottomSlideInAnimation

class DashboardActivity : AppCompatActivity() {

    var isRateOpen = false

    private lateinit var binding: ActivityDashboardBinding

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
        } else {
            binding.propertyTypeImage.setImageResource(R.drawable.svg_add_property)
            binding.letsText.text = "Let’s add your properties"
        }

        binding.addPropertyBtn.setOnClickListener {
            binding.welcomeLayout.isVisible = false
            binding.dashboardFragmentContainer.isVisible = true
            bottomSlideInAnimation(binding.dashboardFragmentContainer, applicationContext)

            if (isSingle){
                replaceFragment(AddRoomTypeFragment())
            } else {
                replaceFragment(AddPropertyFragment())
            }

        }

        binding.dashboardCard.setOnClickListener {
            isCardSelected(binding.dashboardCard, binding.dashboardTxt)
        }
        binding.bookingCard.setOnClickListener {
            isCardSelected(binding.bookingCard, binding.bookingTxt)
            replaceFragment(ViewPropertiesFragment())
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.ratesCard.setOnClickListener {
            isCardSelected(binding.ratesCard, binding.ratesTxt)
//            binding.rateDropDown.setImageResource(R.drawable.svg_up)
            if (!isRateOpen){
                binding.rateLayout.isVisible = true
//                binding.ratesCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
//                binding.ratesTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_up)
                draw?.colorFilter = PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP)
                binding.rateDropDown.setImageDrawable(draw)
                isRateOpen = true
            }
            else{
                binding.rateLayout.isVisible = false
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_arrow_down)
                draw?.colorFilter = PorterDuffColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP)
                binding.rateDropDown.setImageDrawable(draw)
                binding.ratesCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
                binding.ratesTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.black))
                isRateOpen = false
            }

//            binding.rateDropDown
        }
        binding.reportsCard.setOnClickListener {
            isCardSelected(binding.reportsCard,binding.reportsTxt)
        }
        binding.promotionsCard.setOnClickListener {
            isCardSelected(binding.promotionsCard, binding.promotionsTxt)
            replaceFragment(PromotionsFragment())


            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.channelCard.setOnClickListener {
            isCardSelected(binding.channelCard, binding.channelTxt)
        }
        binding.othersCard.setOnClickListener {
            isCardSelected(binding.othersCard, binding.othersTxt)
        }
        binding.pmsCard.setOnClickListener {
            isCardSelected(binding.pmsCard, binding.pmsTxt)
        }
        binding.helpCard.setOnClickListener {
            isCardSelected(binding.helpCard, binding.helpTxt)
        }
        binding.settingCard.setOnClickListener {
            isCardSelected(binding.settingCard, binding.settingTxt)
        }

//        binding.ratesCard.setOnClickListener {
//            replaceFragment(RatesAndInventoryFragment())
//            binding.welcomeLayout.isVisible = false
//            binding.dashboardFragmentContainer.isVisible = true
//        }

    }

    private fun isCardSelected(card: MaterialCardView, text: TextView) {

        binding.dashboardCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.dashboardTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

        binding.reportsCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        binding.reportsTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.textColor))

        binding.bookingCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.bookingTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

//        binding.ratesCard.setCardBackgroundColor(
//            ContextCompat.getColor(
//                applicationContext,
//                R.color.white
//            )
//        )
//        binding.ratesTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.textColor))

        binding.promotionsCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.promotionsTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

        binding.channelCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.channelTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

        binding.othersCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.othersTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

        binding.pmsCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.pmsTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.textColor))

        binding.helpCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.helpTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.textColor))

        binding.settingCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.settingTxt.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.textColor
            )
        )

        card.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
        text.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,fragment)
            transaction.commit()
        }

        binding.welcomeLayout.isVisible = false
        binding.dashboardFragmentContainer.isVisible = true
        bottomSlideInAnimation(binding.dashboardFragmentContainer, applicationContext)

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}