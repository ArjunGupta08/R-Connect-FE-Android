package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.databinding.FragmentAmenitiesBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchTargetTimeZoneId
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AmenitiesFragment : Fragment(), CreateAmenityDialog.OnAmenitySave, AmenitiesAdapter.OnItemUpdate{
    private lateinit var binding: FragmentAmenitiesBinding

    lateinit var progressDialog: Dialog
    private var mLastClickTime : Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAmenitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAmenity()

        binding.createNewBtn.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = CreateAmenityDialog()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
            openDialog.setOnAmenityDialogListener(this@AmenitiesFragment)

        }

    }

    private fun getAmenity() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        progressDialog = showProgressDialog(requireContext())

        val get = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).fetchAmenitiesApi(
            UserSessionManager(requireContext()).getUserId().toString(),
            UserSessionManager(requireContext()).getPropertyId().toString(),
            fetchTargetTimeZoneId()
        )
        get.enqueue(object : Callback<FetchAmenities?> {
            override fun onResponse(
                call: Call<FetchAmenities?>,
                response: Response<FetchAmenities?>
            ) {
                Log.d("response", response.code().toString())
                progressDialog.dismiss()
                if (response.isSuccessful){
                    if (isAdded){
                        try {

                            val data = response.body()!!.data
                            val adapter = AmenitiesAdapter(data, requireContext())
                            binding.recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                            adapter.setOnItemUpdateListener(this@AmenitiesFragment)

                            Log.d("response", response.body()!!.data.toString())

                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FetchAmenities?>, t: Throwable) {
                progressDialog.dismiss()
                Log.e("error", t.localizedMessage)
            }
        })
    }
    override fun onAmenitySave() {
        getAmenity()
    }

    override fun onUpdate(currentItem: FetchAmenitiesData) {
        val openDialog = CreateAmenityDialog(currentItem)
        val fragManager = childFragmentManager
        fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
        openDialog.setOnAmenityDialogListener(this@AmenitiesFragment)
    }
}