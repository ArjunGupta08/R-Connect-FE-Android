package rconnect.retvens.technologies.dashboard.configuration.billings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.reservation.ReservationTypeAdapter
import rconnect.retvens.technologies.databinding.FragmentPaymentTypesBinding


class PaymentTypesFragment : Fragment() {
    private lateinit var binding : FragmentPaymentTypesBinding

    private lateinit var payTypeAdapter: PaymentTypeAdapter
    private var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

    }

    private fun setUpRecycler() {
        binding.paymentTypeRecycler.layoutManager = LinearLayoutManager(requireContext())

        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")


        payTypeAdapter = PaymentTypeAdapter(list, requireContext())
        binding.paymentTypeRecycler.adapter = payTypeAdapter
        payTypeAdapter.notifyDataSetChanged()
    }

}