package rconnect.retvens.technologies.dashboard.pms.houseKeeping

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany.CompanyLedgerAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.CreateAmenityDialog
import rconnect.retvens.technologies.databinding.FragmentLostFoundBinding

class LostFoundFragment : Fragment(), AddLostItemDialog.OnSave {
    private lateinit var binding:FragmentLostFoundBinding

    private lateinit var lostFoundAdapter : LostFoundAdapter
    private var list = ArrayList<String>()

    private var mLastClickTime : Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLostFoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.insertFoundBtn.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = AddLostItemDialog()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
            openDialog.setOnLostDialogListener(this)
            openDialog.isCancelable = false

        }

        binding.insertLostBtn.setOnClickListener {
            // mis-clicking prevention, using threshold of 1000 ms
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@setOnClickListener;
            }
            mLastClickTime = SystemClock.elapsedRealtime()

            val openDialog = AddLostItemDialog()
            val fragManager = childFragmentManager
            fragManager.let{openDialog.show(it, CreateAmenityDialog.TAG)}
            openDialog.setOnLostDialogListener(this)
            openDialog.isCancelable = false

        }

        setUpRecycler()

    }
    private fun setUpRecycler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")
        list.add("4")


        lostFoundAdapter= LostFoundAdapter(list, requireContext())
        binding.recyclerView.adapter = lostFoundAdapter
        lostFoundAdapter.notifyDataSetChanged()
    }

    override fun onLostItemSave() {

    }

}