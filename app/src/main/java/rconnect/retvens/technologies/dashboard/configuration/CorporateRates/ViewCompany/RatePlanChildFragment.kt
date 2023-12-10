package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentRatePlanChildBinding

class RatePlanChildFragment : Fragment() {
    private lateinit var binding: FragmentRatePlanChildBinding

    private lateinit var companyLedgerAdapter: RatePlanChildAdapter
    private var list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanChildBinding.inflate(inflater, container, false)
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


        companyLedgerAdapter = RatePlanChildAdapter(list, requireContext())
        binding.recyclerView.adapter = companyLedgerAdapter
        companyLedgerAdapter.notifyDataSetChanged()
    }
}