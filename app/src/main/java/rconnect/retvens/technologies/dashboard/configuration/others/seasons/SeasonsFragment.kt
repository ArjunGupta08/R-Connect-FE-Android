package rconnect.retvens.technologies.dashboard.configuration.others.seasons

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.CreateAmenityDialog
import rconnect.retvens.technologies.databinding.FragmentSeasonsBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.fetchTargetTimeZoneId
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date


class SeasonsFragment : Fragment(), CreateSeasonDialog.OnSeasonSave, SeasonAdapter.OnUpdate {
    private lateinit var binding: FragmentSeasonsBinding
    lateinit var loader:Dialog

    private var mLastClickTime : Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {

            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = CreateSeasonDialog()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
            openDialog.setOnSeasonDialogListener(this@SeasonsFragment)
            openDialog.isCancelable = false
        }

    }

    private fun setUpRecycler() {

        loader = showProgressDialog(requireContext())

        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getSeasonApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString(),
            fetchTargetTimeZoneId()
        )
        identity.enqueue(object : Callback<GetSeasonDataClass?> {
            override fun onResponse(
                call: Call<GetSeasonDataClass?>,
                response: Response<GetSeasonDataClass?>
            ) {
                loader.dismiss()
                if (response.isSuccessful){
                    val adapter = SeasonAdapter(response.body()!!.data, requireContext())
                    binding.paymentTypeRecycler.adapter = adapter
                    adapter.setOnUpdateListener(this@SeasonsFragment)
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("error" , "${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetSeasonDataClass?>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
                loader.dismiss()
            }
        })

    }

    override fun onSeasonSave() {
        setUpRecycler()
    }

    override fun onUpdate() {
        setUpRecycler()
    }
    override fun onEdit(item : GetSeasonData){
        // mis-clicking prevention, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()

        val openDialog = CreateSeasonDialog(item)
        val fragManager = childFragmentManager
        fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
        openDialog.setOnSeasonDialogListener(this@SeasonsFragment)
        openDialog.isCancelable = false
    }

}