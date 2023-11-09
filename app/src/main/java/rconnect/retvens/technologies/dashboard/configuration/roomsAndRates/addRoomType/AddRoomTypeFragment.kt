package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectBathroomImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectBedImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectViewImagesAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeFragment
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.rightInAnimation

class AddRoomTypeFragment : Fragment(),
    SelectRoomImagesAdapter.OnItemClickListener,
    SelectViewImagesAdapter.OnItemClickListener,
    SelectBathroomImagesAdapter.OnItemClickListener,
    SelectBedImagesAdapter.OnItemClickListener {

    private lateinit var binding:FragmentAddRoomTypeBinding

    private lateinit var imageUri: Uri
    private var PICK_IMAGE_REQUEST_CODE : Int = 0

    private var recyclerType = 1
    private lateinit var selectRoomImagesAdapter: SelectRoomImagesAdapter
    private lateinit var selectViewImagesAdapter: SelectViewImagesAdapter
    private lateinit var selectBathroomImagesAdapter: SelectBathroomImagesAdapter
    private lateinit var selectBedImagesAdapter: SelectBedImagesAdapter

    private var page = 1
    private var bedCount = 1
    private var bedTypeList = ArrayList<String>()
    private lateinit var addBedTypeAdapter: AddBedTypeAdapter

    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface

    private var selectedRoomImagesList = ArrayList<SelectImagesDataClass>()
    private var selectedViewImagesList = ArrayList<SelectImagesDataClass>()
    private var selectedBathroomImagesList = ArrayList<SelectImagesDataClass>()
    private var selectedBedImagesList = ArrayList<SelectImagesDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddRoomTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        binding.continueBtn.setOnClickListener {

            if (page == 1){

                page = 2

                binding.roomImages.textSize = 20.0f
                binding.roomImages.typeface = robotoMedium
                binding.roomImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.roomProfile.textSize = 16.0f
                binding.roomProfile.typeface = roboto

                binding.chargePlans.textSize = 16.0f
                binding.chargePlans.typeface = roboto

                binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
                binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

                val childFragment: Fragment = AddImagesFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.createRoomFragContainer,childFragment)
                transaction.commit()

                binding.frame.visibility = View.GONE
                binding.createRoomFragContainer.visibility = View.VISIBLE
                rightInAnimation(binding.createRoomFragContainer, requireContext())


            } else if (page == 2){

                page = 3

                binding.chargePlans.textSize = 20.0f
                binding.chargePlans.typeface = robotoMedium
                binding.chargePlans.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

                binding.roomImages.textSize = 16.0f
                binding.roomImages.typeface = roboto

                binding.roomProfile.textSize = 16.0f
                binding.roomProfile.typeface = roboto

                binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
                binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

                val childFragment: Fragment = ChargesAndRatesFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.createRoomFragContainer,childFragment)
                transaction.commit()

                binding.frame.visibility = View.GONE
                binding.createRoomFragContainer.visibility = View.VISIBLE
                rightInAnimation(binding.createRoomFragContainer, requireContext())

                binding.continueTxt.text = "Save"
            } else {
//                sendData()
            }
        }

        binding.cancel.setOnClickListener {

            if (Const.isAddingNewRoom){

                Const.isAddingNewRoom = false
                val childFragment: Fragment = RoomTypeFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.dashboardFragmentContainer,childFragment)
                transaction.commit()

            } else {

                Const.isAddingNewRoom = true

                val welcomeLayout = requireActivity().findViewById<LinearLayout>(R.id.welcomeLayout)
                welcomeLayout.visibility = View.VISIBLE

                val dashboardFragmentContainer =
                    requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)
                dashboardFragmentContainer.visibility = View.GONE
            }
        }

        binding.roomProfile.setOnClickListener {

            page = 1

            binding.roomProfile.textSize = 20.0f
            binding.roomProfile.typeface = robotoMedium
            binding.roomProfile.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomImages.textSize = 16.0f
            binding.roomImages.typeface = roboto

            binding.chargePlans.textSize = 16.0f
            binding.chargePlans.typeface = roboto

            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            binding.frame.visibility = View.VISIBLE
            binding.createRoomFragContainer.visibility = View.GONE
            rightInAnimation(binding.createRoomFragContainer, requireContext())
        }

        binding.roomImages.setOnClickListener {

            page = 2

            binding.roomImages.textSize = 20.0f
            binding.roomImages.typeface = robotoMedium
            binding.roomImages.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomProfile.textSize = 16.0f
            binding.roomProfile.typeface = roboto

            binding.chargePlans.textSize = 16.0f
            binding.chargePlans.typeface = roboto

            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))
            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))

            val childFragment: Fragment = AddImagesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.createRoomFragContainer,childFragment)
            transaction.commit()

            binding.frame.visibility = View.GONE
            binding.createRoomFragContainer.visibility = View.VISIBLE
            rightInAnimation(binding.createRoomFragContainer, requireContext())
        }

        binding.chargePlans.setOnClickListener {

            page = 3

            binding.chargePlans.textSize = 20.0f
            binding.chargePlans.typeface = robotoMedium
            binding.chargePlans.setTextColor(ContextCompat.getColor(requireContext(), R.color.secondary))

            binding.roomImages.textSize = 16.0f
            binding.roomImages.typeface = roboto

            binding.roomProfile.textSize = 16.0f
            binding.roomProfile.typeface = roboto

            binding.roomImages.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.roomProfile.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_grey_background))
            binding.chargePlans.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.corner_top_white_background))

            val childFragment: Fragment = ChargesAndRatesFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.createRoomFragContainer,childFragment)
            transaction.commit()

            binding.frame.visibility = View.GONE
            binding.createRoomFragContainer.visibility = View.VISIBLE
            rightInAnimation(binding.createRoomFragContainer, requireContext())

        }

        binding.bedTypeRecycler.layoutManager = GridLayoutManager(requireContext(), 2)


        binding.roomsRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        binding.viewRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        binding.bathroomRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

        binding.bedRecycler.layoutManager = GridLayoutManager(requireContext(), 6)

//        selectRoomImagesAdapter = SelectRoomImagesAdapter(requireContext(), selectedRoomImagesList)
//        selectRoomImagesAdapter.setOnItemClickListener(this)
//        binding.roomsRecycler.adapter = selectRoomImagesAdapter

        selectViewImagesAdapter = SelectViewImagesAdapter(requireContext(), selectedViewImagesList)
        selectViewImagesAdapter.setOnItemClickListener(this)
        binding.viewRecycler.adapter = selectViewImagesAdapter

        selectBathroomImagesAdapter = SelectBathroomImagesAdapter(requireContext(), selectedBathroomImagesList)
        selectBathroomImagesAdapter.setOnItemClickListener(this)
        binding.bathroomRecycler.adapter = selectBathroomImagesAdapter

        selectBedImagesAdapter = SelectBedImagesAdapter(requireContext(), selectedBedImagesList)
        selectBedImagesAdapter.setOnItemClickListener(this)
        binding.bedRecycler.adapter = selectBedImagesAdapter


        binding.add.setOnClickListener { openAddAmenitiesDialog() }

        bedType()
    }

    private fun bedType(){
        binding.addBeds.setOnClickListener {
            bedCount++
            binding.bedCount.setText("$bedCount")
            bedTypeList.add("$bedCount")
            addBedTypeAdapter = AddBedTypeAdapter(requireContext(), bedTypeList)
            binding.bedTypeRecycler.adapter = addBedTypeAdapter
            addBedTypeAdapter.notifyDataSetChanged()
        }
        binding.removeBeds.setOnClickListener {
            if (bedCount>1) {
                bedTypeList.remove("$bedCount")
                bedCount--
                binding.bedCount.setText("$bedCount")
                addBedTypeAdapter = AddBedTypeAdapter(requireContext(), bedTypeList)
                binding.bedTypeRecycler.adapter = addBedTypeAdapter
                addBedTypeAdapter.notifyDataSetChanged()
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

                    when (recyclerType) {
                        1 -> {
                            selectedRoomImagesList.add(SelectImagesDataClass(imageUri))
//                            selectRoomImagesAdapter = SelectRoomImagesAdapter(requireContext(), selectedRoomImagesList)
//                            selectRoomImagesAdapter.setOnItemClickListener(this)
//                            binding.roomsRecycler.adapter = selectRoomImagesAdapter
                        }
                        2 -> {
                            selectedViewImagesList.add(SelectImagesDataClass(imageUri))
                            selectViewImagesAdapter = SelectViewImagesAdapter(requireContext(), selectedViewImagesList)
                            selectViewImagesAdapter.setOnItemClickListener(this)
                            binding.viewRecycler.adapter = selectViewImagesAdapter
                        }
                        3 -> {
                            selectedBathroomImagesList.add(SelectImagesDataClass(imageUri))
                            selectBathroomImagesAdapter = SelectBathroomImagesAdapter(requireContext(), selectedBathroomImagesList)
                            selectBathroomImagesAdapter.setOnItemClickListener(this)
                            binding.bathroomRecycler.adapter = selectBathroomImagesAdapter
                        }
                        4 -> {
                            selectedBedImagesList.add(SelectImagesDataClass(imageUri))
                            selectBedImagesAdapter = SelectBedImagesAdapter(requireContext(), selectedBedImagesList)
                            selectBedImagesAdapter.setOnItemClickListener(this)
                            binding.bedRecycler.adapter = selectBedImagesAdapter
                        }
                    }
                }catch(e:RuntimeException){
                    Log.d("cropperOnPersonal", e.toString())
                }catch(e:ClassCastException){
                    Log.d("cropperOnPersonal", e.toString())
                }
            }

        }
    }



    override fun onAddViewImage() {
        recyclerType = 2
        openGallery()
    }

    override fun onAddBathroomImage() {
        recyclerType = 3
        openGallery()
    }

    override fun onAddBedImage() {
        recyclerType = 4
        openGallery()
    }

    override fun onAddRoomImage(position: Int) {
        recyclerType = 1
        openGallery()
    }

}