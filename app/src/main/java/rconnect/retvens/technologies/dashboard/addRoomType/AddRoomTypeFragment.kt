package rconnect.retvens.technologies.dashboard.addRoomType

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AddAmenitiesAdapter
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AddAmenitiesDataClass
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.dashboard.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding

class AddRoomTypeFragment : Fragment() {

    private lateinit var binding:FragmentAddRoomTypeBinding

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

        binding.cancel.setOnClickListener {

            val welcomeLayout = requireActivity().findViewById<LinearLayout>(R.id.welcomeLayout)
            welcomeLayout.visibility = View.VISIBLE

            val dashboardFragmentContainer = requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)
            dashboardFragmentContainer.visibility = View.GONE

        }

        binding.add.setOnClickListener { openAddAmenitiesDialog() }

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

}