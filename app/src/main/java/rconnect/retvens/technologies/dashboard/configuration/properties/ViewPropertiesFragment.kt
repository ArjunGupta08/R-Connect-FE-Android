package rconnect.retvens.technologies.dashboard.configuration.properties

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.json.JSONObject
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddPropertyFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.databinding.FragmentViewPropertiesBinding
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewPropertiesFragment : Fragment(), ViewPropertiesAdapterView2.OnEditPropertyListener {
    lateinit var binding:FragmentViewPropertiesBinding

    private var viewT = 1

    private lateinit var progressDialog : Dialog
    private var data = ArrayList<PropData>()

    private lateinit var viewPropertiesAdapter : ViewPropertiesAdapter
    private lateinit var viewPropertiesAdapter2 : ViewPropertiesAdapterView2
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

        /* ---------- View Type 1 ------------*/
        binding.topPerformingPropertyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.hotelsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.resortsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        /*  -----View Type 2 ----------*/
        binding.allRecycler.layoutManager = LinearLayoutManager(requireContext())

        fetchProp()

        /* ------------------- Change View Type -------------------*/
        binding.llView.setOnClickListener {

            if (viewT == 1) {
                viewT = 2

                binding.allProp.visibility = View.VISIBLE
                binding.propLayout.visibility = View.GONE

                binding.viewTypeIcon.setImageResource(R.drawable.svg_view_type)

            } else if (viewT == 2) {
                viewT = 1

                binding.viewTypeIcon.setImageResource(R.drawable.svg_view_type_2)

                binding.allProp.visibility = View.GONE
                binding.propLayout.visibility = View.VISIBLE
            }
        }

        binding.addNewProperty.setOnClickListener {

            Const.isAddingNewProperty = true
            val dashboardFragmentContainer = requireActivity().findViewById<FrameLayout>(R.id.dashboardFragmentContainer)

            val childFragment: Fragment = AddPropertyFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()

            bottomSlideInAnimation(dashboardFragmentContainer, requireContext())

        }
    }

    private fun fetchProp(){
        progressDialog = showProgressDialog(requireContext())
        val i = UserSessionManager(requireContext()).getUserId().toString()
        val fetchProp = OAuthClient<ChainConfiguration>(requireContext()).create(ChainConfiguration::class.java).fetchProperty(i)
        fetchProp.enqueue(object : Callback<FetchPropertyData?> {
            override fun onResponse(call: Call<FetchPropertyData?>, response: Response<FetchPropertyData?>) {

                if (response.isSuccessful) {
                    println("Response : ${response.code()} ${response.message()} ${response.body()?.message} ${response.body()?.data}")
                    if (isAdded) {
                        try {
                            data = response.body()!!.data

                            viewPropertiesAdapter = ViewPropertiesAdapter(requireContext(), data)
                            binding.topPerformingPropertyRecyclerView.adapter = viewPropertiesAdapter
                            binding.hotelsRecycler.adapter = viewPropertiesAdapter
                            binding.resortsRecycler.adapter = viewPropertiesAdapter

                            viewPropertiesAdapter2 = ViewPropertiesAdapterView2(requireContext(), data)
                            binding.allRecycler.adapter = viewPropertiesAdapter2
                            viewPropertiesAdapter2.setOnEditPropertyClickListener(this@ViewPropertiesFragment)
                            viewPropertiesAdapter2.notifyDataSetChanged()

                            binding.search.addTextChangedListener(object : TextWatcher {
                                override fun beforeTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    count: Int,
                                    after: Int
                                ) {

                                }

                                override fun onTextChanged(
                                    s: CharSequence?,
                                    start: Int,
                                    before: Int,
                                    count: Int
                                ) {
                                    val filterData = data.filter {
                                        it.propertyName.contains(s.toString(), true)
                                    }
                                    viewPropertiesAdapter.filterList(filterData)
                                    viewPropertiesAdapter2.filterList(filterData)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })

                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                progressDialog.dismiss()

            }

            override fun onFailure(call: Call<FetchPropertyData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
                progressDialog.dismiss()
            }
        })
    }

    override fun onEditPropertyClick(propertyId: String) {
        Const.isAddingNewProperty = true

        val childFragment: Fragment = AddPropertyFragment(propertyId)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.dashboardFragmentContainer,childFragment)
        transaction.commit()
    }
}