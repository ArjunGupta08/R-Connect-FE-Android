package rconnect.retvens.technologies.dashboard.channelManager.Reservations

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetAllRatePlans
import rconnect.retvens.technologies.databinding.FragmentReservationsBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReservationsFragment : Fragment() {
    lateinit var binding:FragmentReservationsBinding

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReservationsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = ArrayList<ReserveData>()
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))
        list.add(ReserveData("ABCD123456789"))

        binding.reservationRecycler.layoutManager = LinearLayoutManager(requireContext())
        val reservationsAdapter = ReservationsAdapter(requireContext(),list)
        binding.reservationRecycler.adapter = reservationsAdapter
        reservationsAdapter.notifyDataSetChanged()

        binding.sourceText.setOnClickListener {
            val property = ArrayList<String>()
            property.add("Commercial")
            property.add("Personal")
            property.add("Null")
            showDropdownMenu(requireContext(),binding.sourceText,it,property)
        }

    }

    fun getBooking () {
        progressDialog = showProgressDialog(requireContext())
        val get = OAuthClient<SingleConfiguration>(requireContext()).create(SingleConfiguration::class.java).getBookingDetails(
            UserSessionManager(requireContext()).getUserId().toString(),
            UserSessionManager(requireContext()).getPropertyId().toString(),
        )
        get.enqueue(object : Callback<GetAllRatePlans?> {
            override fun onResponse(
                call: Call<GetAllRatePlans?>,
                response: Response<GetAllRatePlans?>
            ) {
                progressDialog.dismiss()
                Log.d("booking" , response.toString())
            }

            override fun onFailure(call: Call<GetAllRatePlans?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

}