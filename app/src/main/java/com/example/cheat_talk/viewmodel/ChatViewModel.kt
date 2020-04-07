package com.example.cheat_talk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice

class ChatViewModel: ViewModel() {
    var currentChatHistory: ChatHistoryEntity? = null
    private val nearbyDeviceList = MutableLiveData<List<MockBluetoothDevice>>()
    private val bluetoothConnectionState = MutableLiveData<BluetoothConnectionState>()
    val viewState = MutableLiveData<ViewState>()

    init {
        nearbyDeviceList.value = listOf<MockBluetoothDevice>()
        bluetoothConnectionState.value = BluetoothConnectionState.UNCONNECTED
        viewState.value = ViewState.Launch
    }

    fun updateBluetoothConnectionStateToUnconnected() = apply { bluetoothConnectionState.value = BluetoothConnectionState.UNCONNECTED }
    fun updateBluetoothConnectionStateToConnecting() = apply { bluetoothConnectionState.value = BluetoothConnectionState.CONNECTING }
    fun updateBluetoothConnectionStateToConnected() = apply { bluetoothConnectionState.value = BluetoothConnectionState.CONNECTED }
    fun updateBluetoothConnectionStateToDiscovering() = apply { bluetoothConnectionState.value = BluetoothConnectionState.DISCOVERING }

    fun updateViewStateToLaunch() {
        if (viewState.value != ViewState.Launch) {
            viewState.value = ViewState.Launch
        }
    }
    fun updateViewStateToLoad() {
        if (viewState.value != ViewState.Load) {
            viewState.value = ViewState.Load
        }
    }
    fun updateViewStateToHome() {
        if (viewState.value != ViewState.Home) {
            viewState.value = ViewState.Home
        }
    }
    fun updateViewStateToDiscovery() {
        if (viewState.value != ViewState.Discovery) {
            viewState.value = ViewState.Discovery
        }
    }
    fun updateViewStateToChat() {
        if (viewState.value != ViewState.Chat) {
            viewState.value = ViewState.Chat
        }
    }

    fun setNearbyDeviceList(nearbyDeviceList: List<MockBluetoothDevice>) {
        this.nearbyDeviceList.value = nearbyDeviceList
    }

    fun getNearbyDeviceList(): LiveData<List<MockBluetoothDevice>> {
        return nearbyDeviceList
    }
}