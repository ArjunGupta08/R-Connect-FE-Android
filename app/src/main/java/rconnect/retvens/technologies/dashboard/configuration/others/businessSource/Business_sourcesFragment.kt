package rconnect.retvens.technologies.dashboard.configuration.others.businessSource

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.BusinessSourceAdapter
import rconnect.retvens.technologies.databinding.FragmentBusinessSourcesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Business_sourcesFragment : Fragment(),BusinessSourceAdapter.OnUpdate {

    var list = ArrayList<GetBusinessSourceData>()
    lateinit var binding:FragmentBusinessSourcesBinding
    lateinit var shortCode :TextInputEditText
    lateinit var sourceName :TextInputEditText
    lateinit var loader:Dialog
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




        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
        loader = showProgressDialog(requireContext())
        setUpRecycler()

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

        val shortLayout = dialog.findViewById<TextInputLayout>(R.id.shortLayout1)
        val sourceLayout = dialog.findViewById<TextInputLayout>(R.id.businessSourceLayout1)

        sourceName.doAfterTextChanged {
            if (sourceName.text!!.length>3) {
                shortCode.setText(generateShortCode(sourceName.text.toString()))
            }
        }


        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (sourceName.text!!.isEmpty()){
            shakeAnimation(sourceLayout,requireContext())
        }
        else if (shortCode.text!!.isEmpty()){
            shakeAnimation(shortLayout,requireContext())
        }
        else{
            loader.show()
            saveTMode(dialog)
        }
//            saveTMode(dialog, shortCode.text.toString(), transportationModeText.text.toString())
            binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
            val businessSourceAdapter = BusinessSourceAdapter(list,requireContext())
            binding.reservationTypeRecycler.adapter = businessSourceAdapter
            businessSourceAdapter.notifyDataSetChanged()
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
                loader.dismiss()
                if (response.isSuccessful){
                    if (isAdded){
                    list = response.body()!!.data
                    Log.e("response recycler",list.toString())
                    Toast.makeText(requireContext(),list.size.toString(),Toast.LENGTH_SHORT)
                    Log.e("List size",list.toString())
                    Log.e("List size",list.size.toString())
                    Toast.makeText(requireContext(), "succeed", Toast.LENGTH_SHORT).show()
                    binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())
                    val adapter = BusinessSourceAdapter(response.body()!!.data, requireContext())
                    binding.reservationTypeRecycler.adapter = adapter
                    adapter.setOnUpdateListener(this@Business_sourcesFragment)
                    adapter.notifyDataSetChanged()
                        binding.search.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {

                            }

                            override fun onTextChanged(
                                p0: CharSequence?,
                                p1: Int,
                                p2: Int,
                                p3: Int
                            ) {
                                val filteredData = list.filter {
                                    it.sourceName.contains(p0.toString())
                                }
                                adapter.filterList(filteredData as ArrayList<GetBusinessSourceData>)
                            }

                            override fun afterTextChanged(p0: Editable?) {

                            }
                        })
                }
            }
            }

            override fun onFailure(call: Call<GetBusinessSourceDataClass?>, t: Throwable) {
                loader.dismiss()
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
                loader.show()
                if (response.isSuccessful){
//                    Toast.makeText(requireContext(), "succeed", Toast.LENGTH_SHORT).show()
                    Log.d( "transport succeed", "${response.code()} ${response.message()}")
                    dialog.dismiss()
                    setUpRecycler()
                }
                else{
                    Toast.makeText(requireContext(), response.message().toString(), Toast.LENGTH_SHORT).show()
                    Log.e("else",response.message().toString())
                }
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                loader.show()
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
        loader.show()
        setUpRecycler()
    }


}