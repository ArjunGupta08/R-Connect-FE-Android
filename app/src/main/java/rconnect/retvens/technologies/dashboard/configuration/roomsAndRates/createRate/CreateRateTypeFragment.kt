package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.databinding.FragmentCreateRateTypeBinding
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding

class CreateRateTypeFragment : Fragment() {

    lateinit var binding:FragmentCreateRateTypeBinding
    val rateList = ArrayList<CreateRateData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRateTypeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))
//        rateList.add(CreateRateData("Breakfast","Sunday","Per Adult","200"))



        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRateTypeAdapter = CreateRateTypeAdapter(requireContext(),rateList)
        binding.recyclerInclusion.adapter = createRateTypeAdapter
        createRateTypeAdapter.notifyDataSetChanged()







    }


}