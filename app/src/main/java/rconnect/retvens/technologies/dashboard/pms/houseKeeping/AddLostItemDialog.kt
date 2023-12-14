package rconnect.retvens.technologies.dashboard.pms.houseKeeping

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AmenitiesIconAdapter
import rconnect.retvens.technologies.databinding.DialogAddLostItemBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.generateShortCode
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showDropdownMenu
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLostItemDialog() : DialogFragment() {
    private lateinit var binding :DialogAddLostItemBinding

    private var mListener: OnSave? = null
    fun setOnLostDialogListener(listener: OnSave) {
        mListener = listener
    }
    interface OnSave {
        fun onLostItemSave()
    }

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }
    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    lateinit var progressDialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DialogAddLostItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgUploadLayout.error = "Max 5 Images under 5.00 MB each"

        binding.cancel.setOnClickListener {
            dismiss()
        }

        binding.saveBtn.setOnClickListener {
            dismiss()
        }

 }
}