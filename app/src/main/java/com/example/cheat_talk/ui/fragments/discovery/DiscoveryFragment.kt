package com.example.cheat_talk.ui.fragments.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.DiscoveryFragmentBinding
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DiscoveryFragment: Fragment() {
    private lateinit var deviceAdapter: DeviceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DiscoveryFragmentBinding = DiscoveryFragmentBinding.inflate(inflater, container, false)

        // setup recycler view
        deviceAdapter = DeviceAdapter()
        deviceAdapter.deviceList = createMockBluetoothDeviceList()
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
        // TODO: emit bluetooth device discovery event to parent activity
        Toast.makeText(requireContext(), "Start device discovery", Toast.LENGTH_LONG).show()
    }

    private val deviceSwipeListener: DeviceSwipeCallBack = object : DeviceSwipeCallBack {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            // TODO: swipe to start connection
            val bluetoothDevice: MockBluetoothDevice = deviceAdapter!!.deviceList[position]
            Toast.makeText(requireContext(), "${bluetoothDevice.name} + ${bluetoothDevice.address}", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun createMockBluetoothDeviceList(): List<MockBluetoothDevice> {
        val bluetoothDevice = MockBluetoothDevice("11-23-11-AA-BB-CC", "Kyle Liao")
        return listOf<MockBluetoothDevice>(bluetoothDevice, bluetoothDevice)
    }
}