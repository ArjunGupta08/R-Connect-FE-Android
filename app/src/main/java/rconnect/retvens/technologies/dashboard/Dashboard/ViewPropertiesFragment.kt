package rconnect.retvens.technologies.dashboard.Dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import rconnect.retvens.technologies.dashboard.ViewPropertiesDataClass
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding

class ViewPropertiesFragment : Fragment() {
    lateinit var binding:FragmentViewPropertiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPropertiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topPerformingPropertyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.topPerformingPropertyRecyclerView.setHasFixedSize(true)

        binding.hotelsRecycler.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.hotelsRecycler.setHasFixedSize(true)

        binding.resortsRecycler.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.resortsRecycler.setHasFixedSize(true)

        val list = ArrayList<ViewPropertiesDataClass>()
        val hotelsList = ArrayList<ViewPropertiesDataClass>()

        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))

        val viewPropertiesAdapter = ViewPropertiesAdapter(requireContext(), list)
        binding.topPerformingPropertyRecyclerView.adapter = viewPropertiesAdapter
        binding.hotelsRecycler.adapter = viewPropertiesAdapter
        binding.resortsRecycler.adapter = viewPropertiesAdapter
    }
}