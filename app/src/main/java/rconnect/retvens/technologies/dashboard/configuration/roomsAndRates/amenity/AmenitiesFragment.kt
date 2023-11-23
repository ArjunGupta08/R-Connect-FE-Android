package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
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
            openDialog.isCancelable = false

        }
        binding.ll5.setOnClickListener {
            openFilterDialog()
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
//
//                                data.forEach {
//                                    if (it.displayStatusData.toString() == "0"){
//                                    data.remove(it)
//                                }
//                                }
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

    private fun openFilterDialog() {
        val dialog =
            Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_amenity_filter)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

        dialog.findViewById<ImageView>(R.id.iv_back).setOnClickListener { dialog.dismiss() }
    }

    override fun onUpdate(currentItem: FetchAmenitiesData) {
        val openDialog = CreateAmenityDialog(currentItem)
        val fragManager = childFragmentManager
        fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
        openDialog.setOnAmenityDialogListener(this@AmenitiesFragment)
    }
}