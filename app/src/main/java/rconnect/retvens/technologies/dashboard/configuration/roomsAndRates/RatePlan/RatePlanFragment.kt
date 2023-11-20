package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.RatePlan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.CreateRateTypeFragment
import rconnect.retvens.technologies.databinding.FragmentRatePlanBinding

class RatePlanFragment : Fragment() {

    lateinit var binding: FragmentRatePlanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRatePlanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            replaceFragment(CreateRateTypeFragment())
        }

    }

    private fun roomTypeRecycler() {
        binding.roomTypeRecycler.layoutManager = LinearLayoutManager(requireContext())


    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer, fragment)
            transaction.commit()
        }
    }
}