package com.example.cheat_talk.ui.fragments.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.databinding.DiscoveryFragmentBinding
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import com.example.cheat_talk.viewmodel.BluetoothConnectionState
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

        deviceAdapter = DeviceAdapter()
        viewModel.getNearbyDeviceList().observe(viewLifecycleOwner, Observer {
            deviceAdapter.deviceList = it
        })

        val itemTouchHelper: ItemTouchHelper.SimpleCallback = DeviceItemTouchHelper(
            0, ItemTouchHelper.RIGHT, deviceSwipeListener
        )

        with(binding) {
            deviceItemsContainer.adapter = deviceAdapter
            ItemTouchHelper(itemTouchHelper).attachToRecyclerView(deviceItemsContainer)
            refreshDiscoveryButton.setOnClickListener(View.OnClickListener {
                startDeviceDiscovery()
            })
            disconnectedButton.setOnClickListener(View.OnClickListener {
                disconnectClick()
            })
        }

        viewModel.bluetoothConnectionState.observe(viewLifecycleOwner, Observer {
            when(it) {
                BluetoothConnectionState.CONNECTED -> {
                    binding.refreshDiscoveryButton.hide()
                    binding.disconnectedButton.show()
                }
                else -> {
                    binding.refreshDiscoveryButton.show()
                    binding.disconnectedButton.hide()
                }
            }
        })
        return binding.root
    }

    private fun startDeviceDiscovery() {
        eventListener.onRefreshButtonClick()
    }

    private fun disconnectClick() {
        eventListener.onDisconnectButtonClick()
    }

    private val deviceSwipeListener: DeviceSwipeCallBack = object : DeviceSwipeCallBack {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            val bluetoothDevice: MockBluetoothDevice = deviceAdapter!!.deviceList[position]
            eventListener.onDeviceItemSwipeRight(bluetoothDevice)
        }
    }
}