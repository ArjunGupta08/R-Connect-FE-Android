package rconnect.retvens.technologies.dashboard.createRate

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentCreateRatePlanBinding


class CreateRatePlanFragment : Fragment() {
    lateinit var binding:FragmentCreateRatePlanBinding
    private val planList = ArrayList<CreateRateData>()
    private lateinit var robotoMedium : Typeface
    private lateinit var roboto: Typeface


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


        robotoMedium = ResourcesCompat.getFont(requireContext(),R.font.roboto_medium)!!
        roboto = ResourcesCompat.getFont(requireContext(),R.font.roboto)!!

        planList.add(CreateRateData("Breakfast","Every Day","Per Person","150.00"))
        planList.add(CreateRateData("Lunch","Every Day","Per Person","300.00"))
        planList.add(CreateRateData("Dinner","Every Day","Per Person","300.00"))
        planList.add(CreateRateData("Tea or Coffee","Every Day","Per Person","00.00"))

        binding.recyclerInclusion.layoutManager = LinearLayoutManager(requireContext())
        val createRatePlanAdapter = CreateRatePlanAdapter(requireContext(),planList)
        binding.recyclerInclusion.adapter = createRatePlanAdapter
        createRatePlanAdapter.notifyDataSetChanged()

        binding.policies.setOnClickListener {
            val fragmentManager = childFragmentManager
            val fragmenTransaction = fragmentManager.beginTransaction()
            fragmenTransaction.replace(R.id.ratePlanDetailsLayout,RatePoliciesFragment())
            fragmenTransaction.commit()
            binding.policies.background = ContextCompat.getDrawable(requireContext(),R.drawable.corner_top_white_background)
            binding.policies.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            binding.policies.typeface = robotoMedium
            binding.policies.textSize = 18F

            binding.ratePlanDetails.background = ContextCompat.getDrawable(requireContext(),R.drawable.corner_top_grey_background)
            binding.ratePlanDetails.typeface = roboto
            binding.ratePlanDetails.setTextColor(ContextCompat.getColor(requireContext(),R.color.lightBlack))
            binding.ratePlanDetails.textSize = 14F

        }
        binding.ratePlanDetails.setOnClickListener {
            val fragmentManager = childFragmentManager
            val fragmenTransaction = fragmentManager.beginTransaction()
            fragmenTransaction.replace(R.id.ratePlanDetailsLayout,CreateRatePlanFragment())
            fragmenTransaction.commit()
            binding.ratePlanDetails.background = ContextCompat.getDrawable(requireContext(),R.drawable.corner_top_white_background)
            binding.ratePlanDetails.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
            binding.ratePlanDetails.typeface = robotoMedium
            binding.ratePlanDetails.textSize = 18F

            binding.policies.background = ContextCompat.getDrawable(requireContext(),R.drawable.corner_top_grey_background)
            binding.policies.typeface = roboto
            binding.policies.setTextColor(ContextCompat.getColor(requireContext(),R.color.lightBlack))
            binding.policies.textSize = 14F
        }



    }
}