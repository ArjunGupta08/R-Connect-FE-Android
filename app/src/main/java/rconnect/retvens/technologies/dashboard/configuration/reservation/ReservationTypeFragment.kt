package rconnect.retvens.technologies.dashboard.configuration.reservation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.roomType.RoomTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentReservationTypeBinding
import rconnect.retvens.technologies.databinding.FragmentRoomTypeBinding
import rconnect.retvens.technologies.utils.Const


class ReservationTypeFragment : Fragment() {

    private lateinit var binding : FragmentReservationTypeBinding

    private lateinit var reservationTypeAdapter: ReservationTypeAdapter
    private var reservationTypeList = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReservationTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        reservationTypeRecycler()

        binding.createNewBtn.setOnClickListener {
            openCreateNewDialog()
        }

    }

    private fun openCreateNewDialog() {
        val dialog = Dialog(requireContext()) // Use 'this' as the context, assuming this code is within an Activity
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(R.layout.dialog_create_reservation_type)
        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent) // Makes the background transparent
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val cancel = dialog.findViewById<TextView>(R.id.cancel)
        val save = dialog.findViewById<CardView>(R.id.saveBtn)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        save.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.END)

        dialog.show()

    }


    private fun reservationTypeRecycler() {
        binding.reservationTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")
        reservationTypeList.add("4")

        reservationTypeAdapter = ReservationTypeAdapter(reservationTypeList, requireContext())
        binding.reservationTypeRecycler.adapter = reservationTypeAdapter
        reservationTypeAdapter.notifyDataSetChanged()
    }



}