package rconnect.retvens.technologies.dashboard.configuration.reservation

import android.app.Dialog
import android.content.Context
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
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentIdentityDocumentsBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IdentityDocumentsFragment : Fragment(), IdentityTypeAdapter.OnUpdate {
    private lateinit var binding:FragmentIdentityDocumentsBinding

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
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        val identity = OAuthClient<GeneralsAPI>(requireContext()).create(GeneralsAPI::class.java).getIdentityTypeApi(UserSessionManager(requireContext()).getUserId().toString(), UserSessionManager(requireContext()).getPropertyId().toString())
        identity.enqueue(object : Callback<GetIdentityTypeDataClass?> {
            override fun onResponse(
                call: Call<GetIdentityTypeDataClass?>,
                response: Response<GetIdentityTypeDataClass?>
            ) {
                if (isAdded){
                    if (response.isSuccessful){
                        val identityTypeAdapter = IdentityTypeAdapter(response.body()!!.data, requireContext())
                        binding.reservationTypeRecycler.adapter = identityTypeAdapter
                        identityTypeAdapter.setOnUpdateListener(this@IdentityDocumentsFragment)
                        identityTypeAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("error" , "${response.code()} ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<GetIdentityTypeDataClass?>, t: Throwable) {

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

        val shortCode = dialog.findViewById<TextInputEditText>(R.id.shortCode)
        val identityTypeName = dialog.findViewById<TextInputEditText>(R.id.identityTypeName)

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            saveIdentity(requireContext(), dialog, shortCode.text.toString(), identityTypeName.text.toString())
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }

    private fun saveIdentity(context: Context, dialog: Dialog, shortCodeTxt : String, identityTypeTxt:String) {
        val create = OAuthClient<GeneralsAPI>(context).create(GeneralsAPI::class.java).addIdentityTypeApi(AddIdentityTypeDataClass(UserSessionManager(context).getUserId().toString(), UserSessionManager(context).getPropertyId().toString(), shortCodeTxt, identityTypeTxt))
        create.enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                Log.d( "reservation", "${response.code()} ${response.message()}")
                dialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Log.d("error", t.localizedMessage)
            }
        })
    }

    override fun onUpdateIdentityType() {
        setUpRecycler()
    }
}