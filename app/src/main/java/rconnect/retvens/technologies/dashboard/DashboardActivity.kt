package rconnect.retvens.technologies.dashboard

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.AddReservationFragment
import rconnect.retvens.technologies.dashboard.channelManager.BookingFragment
import rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement.ChannelManagementFragment
import rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment.DashBoardFragment
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.dashboard.channelManager.QuickReservation.QuickReservationAdapter
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.RatesAndInventoryFragment
import rconnect.retvens.technologies.dashboard.channelManager.Reservations.ReservationsFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddPropertyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ReviewRatePlanFragment
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.channelManager.promotions.PromotionsFragment
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.CorporatesPartnersFragment
import rconnect.retvens.technologies.dashboard.configuration.billings.PaymentTypesFragment
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.HolidaysFragment
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.SeasonsFragment
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.TransportationTypesFragment
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.identityType.IdentityDocumentsFragment
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.ReservationTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.Business_sourcesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenitiesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.InclusionPlansFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan.RatePlanFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.SelectedAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.MealPlanFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeFragment
import rconnect.retvens.technologies.databinding.ActivityDashboardBinding
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import rconnect.retvens.technologies.utils.topInAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private var isRateOpen = false
    private var isRateDropDownOpen = false
    private var isBillingOpen = false
    private var isGuestOpen = false
    private var isOtherOpen = false
    var isGuestDropDownOpen = false
    var isNotificationNotOpen = true

    private lateinit var binding: ActivityDashboardBinding

    private lateinit var toggle: ActionBarDrawerToggle

    var notificationList = ArrayList<NotificationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView = window.decorView
            val uiOptions = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            decorView.systemUiVisibility = uiOptions
        }

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolBar()
        getProperty()

        binding.name.text = UserSessionManager(applicationContext).getUsername().toString()

        val userSessionManager = UserSessionManager(applicationContext)

        val userId = userSessionManager.getUserId()
        val token = userSessionManager.getToken()

        Log.e("userid",userId.toString())
        Log.e("token",token.toString())

        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))
        notificationList.add(NotificationData("New Group Invitation","Karan Wants To Join Your Group ‘Salse Chat’","3 Hrs Ago"))

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

        chanelManagerNavLayout()
    }

    private fun getProperty() {
            val get = OAuthClient<ChainConfiguration>(applicationContext).create(ChainConfiguration::class.java).getPropertyById(
                UserSessionManager(applicationContext).getUserId().toString(),
                UserSessionManager(applicationContext).getPropertyId().toString()
            )
            get.enqueue(object : Callback<GetPropertyData?> {
                override fun onResponse(
                    call: Call<GetPropertyData?>,
                    response: Response<GetPropertyData?>
                ) {
                    if (response.isSuccessful) {
                            try {
                                val data = response.body()!!.data

                                Log.e("data", data.toString())

                                if (data.hotelLogo.isEmpty()) {
                                    binding.hotelImg.setImageResource(R.drawable.svg_add_property)
                                } else {
                                    Glide.with(applicationContext).load(data.hotelLogo)
                                        .into(binding.hotelImg)
                                }
                                binding.hotelName.text = (data.propertyName)
                                binding.hotelLocation.text = "${data.city}, ${data.country}"

                            } catch (e : Exception) {
                                e.printStackTrace()
                            }
                    }
                }

                override fun onFailure(call: Call<GetPropertyData?>, t: Throwable) {
                    Log.e("error", t.localizedMessage)
                }
            })
        }

    private fun toolBar() {

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

        binding.imgNotify.setOnClickListener {
            if (isNotificationNotOpen) {
                isNotificationNotOpen = false
                openNotificationDialog()
                Handler().postDelayed(Runnable { isNotificationNotOpen = true },1000)
            }
        }

        binding.quickReservation.setOnClickListener {

            openQuickReservationDialog()

        }

        binding.endCard.setOnClickListener {
            if (isRateDropDownOpen) {
                binding.d2.rotation = 0f
                binding.dropDownLayout.isVisible = false
                isRateDropDownOpen = false
            } else {
                binding.dropDownLayout.isVisible = true
                binding.d2.rotation = 180f
                isRateDropDownOpen = true
                topInAnimation(binding.dropDownLayout, applicationContext)
            }
        }

        binding.configurationPanel.setOnClickListener {
            configurationNavLayout()
            binding.d2.rotation = 0f
            isRateDropDownOpen = false
            binding.dropDownLayout.isVisible = false
            binding.configurationNavLayout.isVisible = true
            binding.channelManagerNavLayout.isVisible = false
        }

        binding.channelManager.setOnClickListener {
            chanelManagerNavLayout()
            isCardSelected(binding.dashboardCard, binding.dashboardTxt)
            binding.d2.rotation = 0f
            isRateDropDownOpen = false
            binding.dropDownLayout.isVisible = false
            binding.configurationNavLayout.isVisible = false
            binding.channelManagerNavLayout.isVisible = true
        }
    }

    private fun openNotificationDialog() {
            val dialog =
                Dialog(this) // Use 'this' as the context, assuming this code is within an Activity
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(R.layout.dialog_notification)
            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
                setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            val recyclerView = dialog.findViewById<RecyclerView>(R.id.recycler_notification)
            recyclerView.layoutManager = LinearLayoutManager(this)
            val notificationAdapter = NotificationAdapter(notificationList, this)
            recyclerView.adapter = notificationAdapter
            notificationAdapter.notifyDataSetChanged()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.END)

            dialog.show()

            dialog.findViewById<ImageView>(R.id.iv_back).setOnClickListener { dialog.dismiss() }
    }

    private fun chanelManagerNavLayout() {
        binding.quickReservation.isVisible = true
        binding.imgNotify.isVisible = true
        binding.notificationCountCard.isVisible = true

        binding.dashboardCard.setOnClickListener {
            replaceFragment(DashBoardFragment())
            isCardSelected(binding.dashboardCard, binding.dashboardTxt)
        }
        binding.bookingCard.setOnClickListener {
            isCardSelected(binding.bookingCard, binding.bookingTxt)
            replaceFragment(BookingFragment())
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.reportsCard.setOnClickListener {
            isCardSelected(binding.reportsCard,binding.reportsTxt)
        }
        binding.promotionsCard.setOnClickListener {
            isCardSelected(binding.promotionsCard, binding.promotionsTxt)
            replaceFragment(ReservationsFragment())


            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.channelCard.setOnClickListener {
            isCardSelected(binding.channelCard, binding.channelTxt)
            replaceFragment(ChannelManagementFragment())


            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.othersCard.setOnClickListener {
            replaceFragment(ReviewRatePlanFragment())
            isCardSelected(binding.othersCard, binding.othersTxt)
        }
        binding.createReservationCard.setOnClickListener {
            replaceFragment(AddReservationFragment())
            isCardSelected(binding.createReservationCard, binding.createReservationTxt)
        }
        binding.helpCard.setOnClickListener {
            isCardSelected(binding.helpCard, binding.helpTxt)
        }
        binding.settingCard.setOnClickListener {
            isCardSelected(binding.settingCard, binding.settingTxt)
        }

        binding.ratesAndInvCard.setOnClickListener {
            isCardSelected(binding.ratesAndInvCard,binding.ratesAndInvTxt)
            replaceFragment(RatesAndInventoryFragment())
            binding.welcomeLayout.isVisible = false
            binding.dashboardFragmentContainer.isVisible = true
        }
    }

    private fun configurationNavLayout() {
        binding.quickReservation.isVisible = false
        binding.imgNotify.isVisible = false
        binding.notificationCountCard.isVisible = false

        isCardSelected(binding.propertiesCard, binding.propertiesText)
        replaceFragment(ViewPropertiesFragment())

        binding.propertiesCard.setOnClickListener {
            isCardSelected(binding.propertiesCard, binding.propertiesText)
            replaceFragment(ViewPropertiesFragment())
        }

        binding.ratesCard.setOnClickListener {
            isCardSelected(binding.ratesCard, binding.ratesTxt)
//            binding.rateDropDown.setImageResource(R.drawable.svg_up)
            if (!isRateOpen){
                binding.rateLayout.isVisible = true
                topInAnimation(binding.rateLayout,applicationContext)
//                binding.ratesCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.black))
//                binding.ratesTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_up)
                draw?.colorFilter = PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP)
                binding.rateDropDown.setImageDrawable(draw)
                isRateOpen = true

                binding.roomTypeCard.setOnClickListener {
                    replaceFragment(RoomTypeFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                binding.mealPlan.setOnClickListener {
                    replaceFragment(MealPlanFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.ll3.setOnClickListener {
                    replaceFragment(RatePlanFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.inclusionPlansLL.setOnClickListener {
                    replaceFragment(InclusionPlansFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.amenitiesLL.setOnClickListener {
                    replaceFragment(AmenitiesFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
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
        }

        binding.billingsCard.setOnClickListener {
            isCardSelected(binding.billingsCard, binding.billingTxt)

            if (!isBillingOpen){
                binding.billingLayout.isVisible = true
                topInAnimation(binding.billingLayout,applicationContext)
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_arrow_down)
                draw?.colorFilter = PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP)
                binding.billingDropDown.setImageDrawable(draw)

                binding.billingDropDown.rotation = 180f
                isBillingOpen = true

                binding.paymentTypesLl.setOnClickListener {
                    replaceFragment(PaymentTypesFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            else{
                binding.billingLayout.isVisible = false
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_arrow_down)
                draw?.colorFilter = PorterDuffColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP)
                binding.billingDropDown.setImageDrawable(draw)

                binding.billingDropDown.rotation = 0f
                binding.billingsCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
                binding.billingTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.black))
                isBillingOpen = false
            }

        }

        binding.guestsCard.setOnClickListener {
            isCardSelected(binding.guestsCard, binding.guestsTxt)

            if (!isGuestOpen){
                binding.guestsLayout.isVisible = true
                topInAnimation(binding.guestsLayout,applicationContext)

                val draw = ContextCompat.getDrawable(this, R.drawable.svg_up)
                draw?.colorFilter = PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP)
                binding.guestsDropDown.setImageDrawable(draw)
                isGuestOpen = true

                binding.guestReservationLl.setOnClickListener {
                    replaceFragment(ReservationTypeFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.identityDocumentsLL.setOnClickListener {
                    replaceFragment(IdentityDocumentsFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            else{
                binding.guestsLayout.isVisible = false
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_arrow_down)
                draw?.colorFilter = PorterDuffColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP)
                binding.guestsDropDown.setImageDrawable(draw)
                binding.guestsCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
                binding.guestsTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.black))
                isGuestOpen = false
            }
        }

        binding.othersCard.setOnClickListener {
            isCardSelected(binding.othersCard, binding.othersTxt)

            if (!isOtherOpen){
                binding.othersLayout.isVisible = true
                topInAnimation(binding.othersLayout,applicationContext)

                val draw = ContextCompat.getDrawable(this, R.drawable.svg_up)
                draw?.colorFilter = PorterDuffColorFilter(Color.WHITE,PorterDuff.Mode.SRC_ATOP)
                binding.otherDropDown.setImageDrawable(draw)
                isOtherOpen = true

                binding.seasonsLl.setOnClickListener {
                    replaceFragment(SeasonsFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.holidaysLl.setOnClickListener {
                    replaceFragment(HolidaysFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.transportationTypesLayout.setOnClickListener {
                    replaceFragment(TransportationTypesFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                binding.businessSourceLayout.setOnClickListener {
                    replaceFragment(Business_sourcesFragment())
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            else{
                binding.othersLayout.isVisible = false
                val draw = ContextCompat.getDrawable(this, R.drawable.svg_arrow_down)
                draw?.colorFilter = PorterDuffColorFilter(Color.BLACK,PorterDuff.Mode.SRC_ATOP)
                binding.otherDropDown.setImageDrawable(draw)
                binding.othersCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
                binding.othersTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.black))
                isOtherOpen = false
            }
        }
        binding.pmsCard.setOnClickListener {
            replaceFragment(CorporatesPartnersFragment())
            isCardSelected(binding.pmsCard, binding.txtPms)
        }

    }

    private fun openQuickReservationDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        val width = (resources.displayMetrics.widthPixels * 0.75).toInt()
        val height = ViewGroup.LayoutParams.MATCH_PARENT // Use MATCH_PARENT for height to take up the entire screen height

        dialog.setContentView(R.layout.dialog_layout_quickreservation)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent

            // Hide the status bar and system navigation
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )

            setLayout(
                width,
                height
            )

            setGravity(Gravity.END) // Set the dialog to attach to the right end of the screen
        }

        dialog.show()

        val roomDetailRecycler = dialog.findViewById<RecyclerView>(R.id.room_detail_recycler)
        roomDetailRecycler.layoutManager = LinearLayoutManager(this)

        val number: ArrayList<String> = ArrayList()
        number.add("1")

        val adapter = QuickReservationAdapter(number, this)
        roomDetailRecycler.adapter = adapter

        val addRoom = dialog.findViewById<CardView>(R.id.addRoomType)
        addRoom.setOnClickListener {
            number.add("1")
            adapter.notifyDataSetChanged()
        }
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

        binding.ratesAndInvCard.setCardBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        binding.ratesAndInvTxt.setTextColor(ContextCompat.getColor(applicationContext,R.color.textColor))

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

        binding.propertiesCard.setCardBackgroundColor(ContextCompat.getColor( applicationContext, R.color.white ))
        binding.propertiesText.setTextColor(ContextCompat.getColor(applicationContext, R.color.textColor))

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

        binding.createReservationCard.setCardBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.createReservationTxt.setTextColor(ContextCompat.getColor(applicationContext, R.color.textColor))

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