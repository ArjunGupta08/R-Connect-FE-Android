package rconnect.retvens.technologies.dashboard.configuration.others.businessSource

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.BusinessSourceAdapter
import rconnect.retvens.technologies.databinding.FragmentBusinessSourcesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Business_sourcesFragment : Fragment(),BusinessSourceAdapter.OnUpdate {

    val list = ArrayList<String>()
    lateinit var binding:FragmentBusinessSourcesBinding
    lateinit var shortCode :TextInputEditText
    lateinit var sourceName :TextInputEditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBusinessSourcesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")





        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
//        val businessSourceAdapter = BusinessSourceAdapter(list,requireContext())
//        binding.reservationTypeRecycler.adapter = businessSourceAdapter
//        businessSourceAdapter.notifyDataSetChanged()
        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }


    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_business_source)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

//        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
//        val transportationModeText = dialog.findViewById<TextInputEditText>(R.id.transportationModeText)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)
        sourceName = dialog.findViewById<TextInputEditText>(R.id.businessSource_text)
        shortCode = dialog.findViewById<TextInputEditText>(R.id.b_short_code_text)




        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
//            saveTMode(dialog, shortCode.text.toString(), transportationModeText.text.toString())
            saveTMode(dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun setUpRecycler() {
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val reservation = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getBusinessSourceApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())

        reservation.enqueue(object : Callback<GetBusinessSourceDataClass?> {
            override fun onResponse(
                call: Call<GetBusinessSourceDataClass?>,
                response: Response<GetBusinessSourceDataClass?>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "succeed", Toast.LENGTH_SHORT).show()
                    binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = BusinessSourceAdapter(response.body()!!.data, requireContext())
                    binding.reservationTypeRecycler.adapter = adapter
                    adapter.setOnUpdateListener(this@Business_sourcesFragment)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<GetBusinessSourceDataClass?>, t: Throwable) {

            }
        })


//        reservation.enqueue(object : Callback<GetTransportationTypeDataClass?> {
//            override fun onResponse(
//                call: Call<GetTransportationTypeDataClass?>,
//                response: Response<GetTransportationTypeDataClass?>
//            ) {
//                if (response.isSuccessful) {
//                    val adapter = TransportationTypeAdapter(response.body()!!.data, requireContext())
//                    binding.reservationTypeRecycler.adapter = adapter
//                    adapter.setOnUpdateListener(this@Business_sourcesFragment)
//                    adapter.notifyDataSetChanged()
//                } else {
//                    openCreateNewDialog()
//                    Log.d("respons", "${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GetTransportationTypeDataClass?>, t: Throwable) {
//                openCreateNewDialog()
//                Log.d("error", t.localizedMessage)
//            }
//        })
    }
    private fun saveTMode(dialog: Dialog) {
        val createBusinessSource = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).addBusinessSourceApi(
            AddBusinessSourceDataClass(
                UserSessionManager(requireContext()).getUserId().toString(),
                shortCode.text.toString(),
                UserSessionManager(requireContext()).getPropertyId().toString(),
                sourceName.text.toString()
            )
        )

        createBusinessSource.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                if (response.isSuccessful){
                    Toast.makeText(requireContext(), "succeed", Toast.LENGTH_SHORT).show()
                    Log.d( "transport", "${response.code()} ${response.message()}")
                    dialog.dismiss()
                    setUpRecycler()
                }
                else{
                    Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    Log.e("else",response.message().toString())
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show()
            }
        })
//        create.enqueue(object : Callback<ResponseData?> {
//            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
//                Log.d( "transport", "${response.code()} ${response.message()}")
//                dialog.dismiss()
//                setUpRecycler()
//            }
//            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
//                Log.d("saveReservationError", "${t.localizedMessage}")
//            }
//        })
    }

    override fun onUpdateBusinessSource() {
        setUpRecycler()
    }


}