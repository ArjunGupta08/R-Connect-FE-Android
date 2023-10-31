package rconnect.retvens.technologies.dashboard.configuration.properties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddPropertyFragment
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPropertiesFragment : Fragment() {
    lateinit var binding:FragmentViewPropertiesBinding

    private var viewT = 1
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

        binding.llView.setOnClickListener {

            if (viewT == 1) {
                viewT = 2

                binding.topPerformingPropertyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.topPerformingPropertyRecyclerView.setHasFixedSize(true)

                binding.hotelsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.hotelsRecycler.setHasFixedSize(true)

                binding.resortsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                binding.resortsRecycler.setHasFixedSize(true)

            } else if (viewT == 2) {
                viewT = 3

                binding.allRecycler.layoutManager = LinearLayoutManager(requireContext())
                binding.allRecycler.setHasFixedSize(true)

                binding.viewTypeIcon.setImageResource(R.drawable.svg_view_type_2)

                binding.allProp.visibility = View.VISIBLE
                binding.propLayout.visibility = View.GONE

            } else if (viewT == 3) {
                viewT = 1

                binding.allProp.visibility = View.GONE
                binding.propLayout.visibility = View.VISIBLE

                binding.viewTypeIcon.setImageResource(R.drawable.svg_view_type)

                binding.topPerformingPropertyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 6)
                binding.topPerformingPropertyRecyclerView.setHasFixedSize(true)

                binding.hotelsRecycler.layoutManager = GridLayoutManager(requireContext(), 6)
                binding.hotelsRecycler.setHasFixedSize(true)

                binding.resortsRecycler.layoutManager = GridLayoutManager(requireContext(), 6)
                binding.resortsRecycler.setHasFixedSize(true)
            }

            val viewPropertiesAdapter = ViewPropertiesAdapter(requireContext(), list, viewT)
            binding.allRecycler.adapter = viewPropertiesAdapter
            binding.topPerformingPropertyRecyclerView.adapter = viewPropertiesAdapter
            binding.hotelsRecycler.adapter = viewPropertiesAdapter
            binding.resortsRecycler.adapter = viewPropertiesAdapter
        }

        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))
        list.add(ViewPropertiesDataClass("Other Room", "Indore"))
        list.add(ViewPropertiesDataClass("My Room", "Kanpur"))

        val viewPropertiesAdapter = ViewPropertiesAdapter(requireContext(), list, viewT)
        binding.topPerformingPropertyRecyclerView.adapter = viewPropertiesAdapter
        binding.hotelsRecycler.adapter = viewPropertiesAdapter
        binding.resortsRecycler.adapter = viewPropertiesAdapter

        binding.addNewProperty.setOnClickListener {

            Const.isAddingNewProperty = true
            val dashboardFragmentContainer = requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)

            val childFragment: Fragment = AddPropertyFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()

            bottomSlideInAnimation(dashboardFragmentContainer, requireContext())

        }

        fetchProp()
    }

    private fun fetchProp(){
        val i = UserSessionManager(requireContext()).getUserId().toString()
        val fetchProp = OAuthClient<ChainConfiguration>(requireContext()).create(ChainConfiguration::class.java).fetchProperty(i)
//        val fetchProp = RetrofitObject.chainConfiguration.fetchProperty(i)
        fetchProp.enqueue(object : Callback<FetchPropertyData?> {
            override fun onResponse(call: Call<FetchPropertyData?>, response: Response<FetchPropertyData?>) {

                println("Response : ${response.code()} ${response.message()} ${response.body()?.data} " )

            }

            override fun onFailure(call: Call<FetchPropertyData?>, t: Throwable) {

            }
        })
    }
}