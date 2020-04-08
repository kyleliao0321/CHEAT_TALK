package com.example.cheat_talk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import java.util.*

class ChatViewModel: ViewModel() {
    var currentChatHistory: ChatHistoryEntity? = null
    private val nearbyDeviceList = MutableLiveData<List<MockBluetoothDevice>>()
    private val bluetoothConnectionState = MutableLiveData<BluetoothConnectionState>()
    private val chatHistoryList = MutableLiveData<List<ChatHistoryEntity>>()
    private val chatHistoryMessage = MutableLiveData<List<ChatMessageEntity>>()
    val viewState = MutableLiveData<ViewState>()

    init {
        nearbyDeviceList.value = listOf<MockBluetoothDevice>()
        chatHistoryList.value = mockChatHistories()
        chatHistoryMessage.value= createMockMessages()
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

    fun getChatHistoryList(): LiveData<List<ChatHistoryEntity>> {
        return chatHistoryList
    }

    fun getChatHistoryMessage(id: Long): LiveData<List<ChatMessageEntity>> {
        return chatHistoryMessage
    }

    private fun mockChatHistories(): List<ChatHistoryEntity> {
        val chatHistory: ChatHistoryEntity =
            ChatHistoryEntity.Builder()
                .HID(2435546464L)
                .pairedName("Kyle")
                .lastMessage("Hellow World")
                .lastDate("2020/03/21 20:30")
                .build()
        return listOf(chatHistory, chatHistory)
    }

    private fun createMockMessages(): List<ChatMessageEntity> {
        val messageContents: List<String> = listOf(
            "hello",
            "hello",
            "hello",
            "hello"
        )
        var chatMessageList: MutableList<ChatMessageEntity> = arrayListOf()

        for ((index, value) in messageContents.withIndex()) {
            val dateString: String = Date().toString()
            chatMessageList.add(
                ChatMessageEntity.Builder()
                    .MID(index.toLong())
                    .content(value)
                    .date(dateString)
                    .local(index/2 == 0)
                    .build()
            )
        }

        return chatMessageList
    }
}