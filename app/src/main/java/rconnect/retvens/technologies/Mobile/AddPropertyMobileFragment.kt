package rconnect.retvens.technologies.Mobile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddAmenitiesDataClass
import rconnect.retvens.technologies.databinding.FragmentAddPropertyMobileBinding
class AddPropertyMobileFragment : Fragment() {



    lateinit var bindingMobile:FragmentAddPropertyMobileBinding
    var list1 : ArrayList<AddAmenitiesDataClass> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingMobile = FragmentAddPropertyMobileBinding.inflate(layoutInflater,container,false)
        return bindingMobile.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        bindingMobile.amenitiesRecycler.layoutManager = layoutManager

        list1.add(AddAmenitiesDataClass("Karan"))
        list1.add(AddAmenitiesDataClass("Arjun"))
        list1.add(AddAmenitiesDataClass("Rahul"))
        list1.add(AddAmenitiesDataClass("Sachin"))
        list1.add(AddAmenitiesDataClass("Prashant"))
        list1.add(AddAmenitiesDataClass("Aman"))


        val addAmenitiesMobileAdapter = AddAmenitiesMobileAdapter(requireContext(),list1)
        bindingMobile.amenitiesRecycler.adapter = addAmenitiesMobileAdapter
        addAmenitiesMobileAdapter.notifyDataSetChanged()




    }


}