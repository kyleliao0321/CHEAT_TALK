package com.example.cheat_talk.ui.fragments.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.databinding.DiscoveryFragmentBinding
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import com.example.cheat_talk.viewmodel.ChatViewModel

class DiscoveryFragment: Fragment() {
    private lateinit var eventListener: DiscoveryFragmentEventListener
    private lateinit var deviceAdapter: DeviceAdapter
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListener = (requireActivity() as MainActivity).discoveryFragmentEventListener

        val binding: DiscoveryFragmentBinding = DiscoveryFragmentBinding.inflate(inflater, container, false)

        // setup recycler view
        deviceAdapter = DeviceAdapter()
        deviceAdapter.deviceList = eventListener.onFragmentCreate()

        val itemTouchHelper: ItemTouchHelper.SimpleCallback = DeviceItemTouchHelper(
            0, ItemTouchHelper.RIGHT, deviceSwipeListener
        )

        with(binding) {
            deviceItemsContainer.adapter = deviceAdapter
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(deviceItemsContainer)
            refreshDiscoveryButton.setOnClickListener(View.OnClickListener {
                startDeviceDiscovery()
            })
        }

        return binding.root
    }

    private fun startDeviceDiscovery() {
        val deviceList: List<MockBluetoothDevice> = eventListener.onRefreshButtonClick()
        deviceAdapter.deviceList = deviceList
        // TODO: notification should done in device list setter
        deviceAdapter.notifyDataSetChanged()
    }

    private val deviceSwipeListener: DeviceSwipeCallBack = object : DeviceSwipeCallBack {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            val bluetoothDevice: MockBluetoothDevice = deviceAdapter!!.deviceList[position]
            eventListener.onDeviceItemSwipeRight(bluetoothDevice)
        }
    }
}