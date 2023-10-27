package rconnect.retvens.technologies.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentDashBoardBinding


class DashBoardFragment : Fragment() {

    private lateinit var bindingTab:FragmentDashBoardBinding
    private lateinit var bookingDetailsAdapter: BookingDetailsAdapter
    private  var mList:ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       bindingTab = FragmentDashBoardBinding.inflate(layoutInflater,container,false)
        return bindingTab.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBooking()

        bindingTab.bookingDetailRecycler.layoutManager = LinearLayoutManager(requireContext())

        bookingDetailsAdapter = BookingDetailsAdapter(requireContext(),mList)
        bindingTab.bookingDetailRecycler.adapter = bookingDetailsAdapter
        bookingDetailsAdapter.notifyDataSetChanged()

    }

    private fun setBooking() {
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")


    }
}