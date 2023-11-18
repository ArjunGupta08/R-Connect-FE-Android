package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import rconnect.retvens.technologies.R

class PaymentTypeBottomSheet : DialogFragment() {

    companion object {
        const val TAG = "AddRecipientWalletSheet"
    }


    override fun getTheme(): Int = R.style.Theme_AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_create_inclusion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}