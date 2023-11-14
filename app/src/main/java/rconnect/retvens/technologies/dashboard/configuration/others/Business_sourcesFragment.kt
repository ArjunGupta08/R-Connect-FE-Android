package rconnect.retvens.technologies.dashboard.configuration.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.BusinessSourceAdapter
import rconnect.retvens.technologies.databinding.FragmentBusinessSourcesBinding

class Business_sourcesFragment : Fragment() {

    val list = ArrayList<String>()
    lateinit var binding:FragmentBusinessSourcesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessSourcesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")



        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
        val businessSourceAdapter = BusinessSourceAdapter(list,requireContext())
        binding.reservationTypeRecycler.adapter = businessSourceAdapter
        businessSourceAdapter.notifyDataSetChanged()


    }


}