package rconnect.retvens.technologies.dashboard.addPropertyFrags

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.Dashboard.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.addRoomType.SelectImagesAdapter
import rconnect.retvens.technologies.dashboard.addRoomType.SelectImagesDataClass
import rconnect.retvens.technologies.databinding.FragmentAddPropertyBinding
import rconnect.retvens.technologies.utils.fadeOutAnimation
import rconnect.retvens.technologies.utils.fadeInAnimation

class AddPropertyFragment : Fragment(), OnMapReadyCallback, SelectImagesAdapter.OnItemClickListener {

    private lateinit var binding : FragmentAddPropertyBinding

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    private lateinit var imageUri: Uri
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var page = 1

    lateinit var selectImagesAdapter: SelectImagesAdapter
    private var selectedImagesList = ArrayList<SelectImagesDataClass>()

    private var isPropertyLogo = true

    private var isImageSelected = false
    private var isImageEdited = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        lToRInAnimation(binding.propertyProfileLayout, requireContext())

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFrag.getMapAsync(this)

        binding.imagesRecycler.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.imagesRecycler.setHasFixedSize(true)

        selectImagesAdapter = SelectImagesAdapter(requireContext(), selectedImagesList)
        selectImagesAdapter.setOnItemClickListener(this)
        binding.imagesRecycler.adapter = selectImagesAdapter

        binding.continueBtn.setOnClickListener {

            if (page == 1){

                page = 2

                binding.addressAndContacts.textSize = 20.0f
                binding.addressAndContacts.typeface = robotoMedium

                binding.propertyProfile.textSize = 16.0f
                binding.propertyProfile.typeface = roboto

                binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

                binding.propertyProfileLayout.visibility = View.GONE
                binding.addressLayout.visibility = View.VISIBLE
                rToL_InAnimation(binding.addressLayout, requireContext())

            } else {

                val childFragment: Fragment = ViewPropertiesFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                transaction.commit()

            }
        }

        binding.cancel.setOnClickListener {

            val welcomeLayout = requireActivity().findViewById<LinearLayout>(R.id.welcomeLayout)
            welcomeLayout.visibility = View.VISIBLE

            val dashboardFragmentContainer = requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)
            dashboardFragmentContainer.visibility = View.GONE

        }

        binding.propertyProfile.setOnClickListener {
            page = 1

            binding.propertyProfile.textSize = 20.0f
            binding.propertyProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))
            binding.propertyProfile.typeface = robotoMedium

            binding.addressAndContacts.textSize = 16.0f
            binding.addressAndContacts.typeface = roboto

            binding.propertyProfileLayout.visibility = View.VISIBLE
            lToRInAnimation(binding.propertyProfileLayout, requireContext())
            binding.addressLayout.visibility = View.GONE

            binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
        }

        binding.addressAndContacts.setOnClickListener {
            page = 2

            binding.addressAndContacts.textSize = 20.0f
            binding.addressAndContacts.typeface = robotoMedium
            binding.addressAndContacts.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.propertyProfile.textSize = 16.0f
            binding.propertyProfile.typeface = roboto

            binding.propertyProfileLayout.visibility = View.GONE
            binding.addressLayout.visibility = View.VISIBLE
            rToL_InAnimation(binding.addressLayout, requireContext())

            binding.propertyProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.addressAndContacts.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
        }

        binding.add.setOnClickListener { openAddAmenitiesDialog() }

        binding.replaceImage.setOnClickListener {
            openGallery()
            fadeOutAnimation(binding.imageEditLayout, requireContext())
            isImageEdited = false
            binding.imageEditLayout.isVisible = false
        }

        binding.removeImage.setOnClickListener {
            imageUri = Uri.EMPTY
            isImageSelected = false
            binding.propertyLogoImage.setImageURI(imageUri)
            binding.propertyLogoImage.isVisible = false
            binding.propertyLogoLayout.isVisible = true
            fadeOutAnimation(binding.imageEditLayout, requireContext())
            isImageEdited = false
            binding.imageEditLayout.isVisible = false
        }

        binding.propertyLogoCard.setOnClickListener {
            if (!isImageSelected){
                isPropertyLogo = true
                openGallery()
            } else {
                if (!isImageEdited) {
                    binding.imageEditLayout.isVisible = true
                    // load the animation
                    fadeInAnimation(binding.imageEditLayout, requireContext())
                    isImageEdited = true
                } else {
                    fadeOutAnimation(binding.imageEditLayout, requireContext())
                    binding.imageEditLayout.isVisible = false
                    isImageEdited = false
                }
            }
        }

    }

    private fun rToL_InAnimation(view: View, context: Context) {
        // load the animation
        val animSlideIn: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.r_to_l_in_animation
        )
        // start the animation
        view.startAnimation(animSlideIn)
    }

    private fun lToRInAnimation(view: View, context: Context) {
        // load the animation
        val animSlideOut: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.l_to_r_in_animation
        )
        // start the animation
        view.startAnimation(animSlideOut)
    }

    // This method is called when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        val myPos = LatLng(28.679079 , 77.069710)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos))
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            imageUri = data.data!!
            if (imageUri != null) {
                try {
                    if (isPropertyLogo) {
                        binding.propertyLogoImage.setImageURI(imageUri)
                        binding.propertyLogoImage.isVisible = true
                        binding.propertyLogoLayout.isVisible = false
                        isImageSelected = true
                    } else {
                        selectedImagesList.add(SelectImagesDataClass(imageUri))
                        selectImagesAdapter =
                            SelectImagesAdapter(requireContext(), selectedImagesList)
                        selectImagesAdapter.setOnItemClickListener(this)
                        binding.imagesRecycler.adapter = selectImagesAdapter
                    }
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }

    private fun openAddAmenitiesDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_amenities)

        val createNewAmenityBtn = dialog.findViewById<TextView>(R.id.createNewAmenityBtn)
        createNewAmenityBtn.setOnClickListener {
            openCreateNewAmenityDialog()
        }
        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val amenitiesRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesRecycler)
        amenitiesRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        val amenities = ArrayList<AddAmenitiesDataClass>()

        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))
        amenities.add(AddAmenitiesDataClass("amenityName"))

        val addAmenitiesAdapter = AddAmenitiesAdapter(requireContext(), amenities)
        amenitiesRecycler.adapter = addAmenitiesAdapter

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
//        dialog.window?.setGravity(Gravity.BOTTOM)
    }
    private fun openCreateNewAmenityDialog() {
        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_create_amenities)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        val amenitiesIconRecycler = dialog.findViewById<RecyclerView>(R.id.amenitiesIconRecycler)
        amenitiesIconRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        val amenities = ArrayList<AmenitiesIconDataClass>()

        amenities.add(AmenitiesIconDataClass(R.drawable.check))
        amenities.add(AmenitiesIconDataClass(R.drawable.check))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_keys))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_keys))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_add))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_add))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))
        amenities.add(AmenitiesIconDataClass(R.drawable.svg_ac))

        val amenitiesIconAdapter = AmenitiesIconAdapter(requireContext(), amenities)
        amenitiesIconRecycler.adapter = amenitiesIconAdapter

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    override fun onAddButtonClick() {
        isPropertyLogo = false
        openGallery()
    }

}