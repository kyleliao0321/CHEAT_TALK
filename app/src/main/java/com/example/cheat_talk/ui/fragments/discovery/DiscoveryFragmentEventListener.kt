package com.example.cheat_talk.ui.fragments.discovery

import com.example.cheat_talk.mockDataObject.MockBluetoothDevice

interface DiscoveryFragmentEventListener {
    fun onDeviceItemSwipeRight(mockBluetoothDevice: MockBluetoothDevice)
    fun onRefreshButtonClick(): List<MockBluetoothDevice>
    fun onFragmentCreate(): List<MockBluetoothDevice>
}