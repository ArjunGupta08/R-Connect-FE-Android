package rconnect.retvens.technologies.dashboard.channelManager.promotions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.addPropertyFrags.AmenitiesIconDataClass
import rconnect.retvens.technologies.databinding.FragmentPromotionsBinding

class PromotionsFragment : Fragment() {

    lateinit var binding : FragmentPromotionsBinding

    lateinit var promotionsAdapter: PromotionsAdapter
    var promotionList = ArrayList<PromotionsDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPromotionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.livePromotionsRecyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
        binding.livePromotionsRecyclerView.setHasFixedSize(true)

        promotionList.add(PromotionsDataClass("Diwali Offer"))
        promotionList.add(PromotionsDataClass("Bumper Offer"))
        promotionList.add(PromotionsDataClass("Great Indian Sale"))
        promotionList.add(PromotionsDataClass("Grand Offer"))
        promotionList.add(PromotionsDataClass("Big Billion Days"))
        promotionList.add(PromotionsDataClass("Big Saving Days"))

        promotionsAdapter = PromotionsAdapter(requireContext(), promotionList)
        binding.livePromotionsRecyclerView.adapter = promotionsAdapter
        promotionsAdapter.notifyDataSetChanged()

    }
}