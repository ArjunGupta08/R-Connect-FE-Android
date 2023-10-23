package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.billings.PaymentTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentStayHistoryChildBinding


class StayHistoryChildFragment : Fragment() {
    private lateinit var binding : FragmentStayHistoryChildBinding

    private lateinit var stayHistoryAdapter: StayHistoryAdapter
    private var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStayHistoryChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

    }

    private fun setUpRecycler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")


        stayHistoryAdapter = StayHistoryAdapter(list, requireContext())
        binding.recyclerView.adapter = stayHistoryAdapter
        stayHistoryAdapter.notifyDataSetChanged()
    }

}