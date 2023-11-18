package rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.identityType

import android.app.Dialog
import android.content.Context
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
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentIdentityDocumentsBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IdentityDocumentsFragment : Fragment(), IdentityTypeAdapter.OnUpdate {
    private lateinit var binding:FragmentIdentityDocumentsBinding

    private lateinit var progressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIdentityDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }

    private fun setUpRecycler() {
        progressDialog = showProgressDialog(requireContext())
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getIdentityTypeApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetIdentityTypeDataClass?> {
            override fun onResponse(
                call: Call<GetIdentityTypeDataClass?>,
                response: Response<GetIdentityTypeDataClass?>
            ) {
                if (isAdded){
                    progressDialog.dismiss()
                    if (response.isSuccessful){
                        try {
                            val data = response.body()!!.data
                            val identityTypeAdapter = IdentityTypeAdapter(data, requireContext())
                            binding.reservationTypeRecycler.adapter = identityTypeAdapter
                            identityTypeAdapter.setOnUpdateListener(this@IdentityDocumentsFragment)
                            identityTypeAdapter.notifyDataSetChanged()

                            Log.d("identity", data.toString())
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
                                    val filterData = data.filter { t ->
                                        t.identityType.contains(s.toString(), true)
                                    }
                                    identityTypeAdapter.filterData(filterData as ArrayList<GetIdentityTypeData>)
                                }

                                override fun afterTextChanged(s: Editable?) {

                                }
                            })
                        } catch (e : Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.d("error" , "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetIdentityTypeDataClass?>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_identity_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val identityTypeLayout = dialog.findViewById<TextInputLayout>(R.id.identityTypeLayout)
        val shortCodeLayout = dialog.findViewById<TextInputLayout>(R.id.shortCodeLayout)

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val identityTypeName = dialog.findViewById<TextInputEditText>(R.id.identityTypeName)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        identityTypeName.doAfterTextChanged{
            if (identityTypeName.text!!.length >= 2) {
                shortCode.setText(generateShortCode(identityTypeName.text.toString()))
            }
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            if (identityTypeName.text!!.isEmpty()) {
                shakeAnimation(identityTypeLayout, requireContext())
            } else if (shortCode.text!!.isEmpty()) {
                shakeAnimation(shortCodeLayout, requireContext())
            } else {
                progressDialog.show()
                saveIdentity(
                    requireContext(),
                    dialog,
                    shortCode.text.toString(),
                    identityTypeName.text.toString()
                )
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun saveIdentity(context: Context, dialog: Dialog, shortCodeTxt : String, identityTypeTxt:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addIdentityTypeApi(
            AddIdentityTypeDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, identityTypeTxt)
        )
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                progressDialog.dismiss()
                dialog.dismiss()
                setUpRecycler()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                progressDialog.dismiss()
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onUpdateIdentityType() {
        setUpRecycler()
    }
}