package rconnect.retvens.technologies.dashboard.channelManager.ChannelsManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.promotions.PromotionsAdapter
import rconnect.retvens.technologies.dashboard.channelManager.promotions.PromotionsDataClass
import rconnect.retvens.technologies.databinding.FragmentChannelManagementBinding


class ChannelManagementFragment : Fragment() {


    private lateinit var bindingTab:FragmentChannelManagementBinding
    private  var activeList:ArrayList<String> = ArrayList()
    private lateinit var channelManagementAdapter: ChannelManagementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindingTab = FragmentChannelManagementBinding.inflate(inflater)
        return bindingTab.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingTab.activeChannelsRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
        bindingTab.activeChannelsRecycler.setHasFixedSize(true)

        setData()


        channelManagementAdapter = ChannelManagementAdapter(requireContext(), activeList)
        bindingTab.activeChannelsRecycler.adapter = channelManagementAdapter
        channelManagementAdapter.notifyDataSetChanged()

        bindingTab.addNewChannel.setOnClickListener {
            replaceFragment(ConnectionSettingFragment())
        }
    }

    private fun setData() {
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")
        activeList.add("Text")

    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer, fragment)
            transaction.commit()
        }
    }


}