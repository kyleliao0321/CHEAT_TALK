package com.example.cheat_talk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import java.util.*

class ChatViewModel: ViewModel() {
    var viewChatHistory: ChatHistoryEntity? = null
    private val connectedChatHistory = MutableLiveData<ChatHistoryEntity>()
    private val nearbyDeviceList = MutableLiveData<List<MockBluetoothDevice>>()
    private val chatHistoryList = MutableLiveData<List<ChatHistoryEntity>>()
    private val chatHistoryMessage = MutableLiveData<List<ChatMessageEntity>>()
    val bluetoothConnectionState = MutableLiveData<BluetoothConnectionState>()
    val viewState = MutableLiveData<ViewState>()

    init {
        nearbyDeviceList.value = listOf<MockBluetoothDevice>()
        chatHistoryList.value = mockChatHistories()
        chatHistoryMessage.value= createMockMessages()
        bluetoothConnectionState.value = BluetoothConnectionState.UNCONNECTED
        viewState.value = ViewState.Launch
        connectedChatHistory.value = null
    }

    fun updateBluetoothConnectionStateToUnconnected() { bluetoothConnectionState.value = BluetoothConnectionState.UNCONNECTED }
    fun updateBluetoothConnectionStateToConnecting() { bluetoothConnectionState.value = BluetoothConnectionState.CONNECTING }
    fun updateBluetoothConnectionStateToConnected() { bluetoothConnectionState.value = BluetoothConnectionState.CONNECTED }
    fun updateBluetoothConnectionStateToDiscovering() { bluetoothConnectionState.value = BluetoothConnectionState.DISCOVERING }

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

    fun setConnectedChatHistory(chatHistory: ChatHistoryEntity?) {
        connectedChatHistory.value = chatHistory
    }

    fun getConnectedChatHistory(): LiveData<ChatHistoryEntity> {
        return connectedChatHistory
    }

    private fun mockChatHistories(): List<ChatHistoryEntity> {
        val chatHistory: ChatHistoryEntity =
            ChatHistoryEntity.Builder()
                .hid(2435546464L)
                .pairedName("Kyle")
                .lastMessage("Hellow World")
                .lastDate(Date())
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
            chatMessageList.add(
                ChatMessageEntity.Builder()
                    .mid(index.toLong())
                    .content(value)
                    .date(Date())
                    .local(index/2 == 0)
                    .hid(index.toLong())
                    .build()
            )
        }

        return chatMessageList
    }
}