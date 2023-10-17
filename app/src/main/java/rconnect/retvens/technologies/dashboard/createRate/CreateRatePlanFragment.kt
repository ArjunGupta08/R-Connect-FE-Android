package rconnect.retvens.technologies.dashboard.createRate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentCreateRatePlanBinding


class CreateRatePlanFragment : Fragment() {
    lateinit var binding:FragmentCreateRatePlanBinding
    val planList = ArrayList<CreateRateData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRatePlanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        planList.add(CreateRateData("Breakfast","Every Day","Per Person","150.00"))
        planList.add(CreateRateData("Lunch","Every Day","Per Person","300.00"))
        planList.add(CreateRateData("Dinner","Every Day","Per Person","300.00"))
        planList.add(CreateRateData("Tea or Coffee","Every Day","Per Person","00.00"))

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),planList)
        binding.recyclerInclusion.adapter = createRatePlanAdapter
        createRatePlanAdapter.notifyDataSetChanged()

    }
}