package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.GetRoomData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.GetRoomDataClass
import rconnect.retvens.technologies.databinding.FragmentAddRoomTypeBinding
import rconnect.retvens.technologies.databinding.FragmentRoomTypeBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchTargetTimeZoneId
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomTypeFragment : Fragment(), RoomTypeAdapter.SetOnEditClickListener {

    private lateinit var binding : FragmentRoomTypeBinding

    private lateinit var roomTypeAdapter: RoomTypeAdapter

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        roomTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            Const.isAddingNewRoom = true

            val childFragment: Fragment = AddRoomTypeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,childFragment)
            transaction.commit()

        }
    }

    private fun roomTypeRecycler() {
        progressDialog = showProgressDialog(requireContext())

        binding.roomTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val get = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).getRoomApi(
            fetchTargetTimeZoneId(),
            UserSessionManager(requireContext()).getPropertyId().toString(),
            UserSessionManager(requireContext()).getUserId().toString()
        )
        get.enqueue(object : Callback<GetRoomData?> {
            override fun onResponse(
                call: Call<GetRoomData?>,
                response: Response<GetRoomData?>
            ) {
                progressDialog.dismiss()
                Log.d("response", response.toString())
                if (response.isSuccessful) {
                    if (isAdded) {
                        try {
                            Log.d("response", response.body()!!.data.toString())
                            val data = response.body()!!.data
                            roomTypeAdapter = RoomTypeAdapter(data, requireContext())
                            binding.roomTypeRecycler.adapter = roomTypeAdapter
                            roomTypeAdapter.setOnEditButtonClickListener(this@RoomTypeFragment)
                            roomTypeAdapter.notifyDataSetChanged()

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
                                    val filterList = data.filter {
                                        it.roomTypeName.contains(s.toString(), true)
                                    }
                                    roomTypeAdapter.filterList(filterList as ArrayList<GetRoomDataClass>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })

                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GetRoomData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error", t.localizedMessage)
            }
        })
    }

    override fun onEditButtonClick(roomTypeId : String) {

        Const.isAddingNewRoom = true

        val childFragment: Fragment = AddRoomTypeFragment(roomTypeId)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.dashboardFragmentContainer,childFragment)
        transaction.commit()

    }
}