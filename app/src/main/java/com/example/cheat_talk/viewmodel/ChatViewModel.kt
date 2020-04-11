package com.example.cheat_talk.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.cheat_talk.Util
import com.example.cheat_talk.db.ChatRepository
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val chatRepository: ChatRepository = ChatRepository(application)
    private val connectedChatHistory = MutableLiveData<ChatHistoryEntity>()
    private val nearbyDeviceList = MutableLiveData<List<MockBluetoothDevice>>()
    private var chatHistoryList: LiveData<List<ChatHistoryEntity>>
    private lateinit var chatHistoryMessage: LiveData<List<ChatMessageEntity>>
    var viewChatHistory: ChatHistoryEntity? = null
    val bluetoothConnectionState = MutableLiveData<BluetoothConnectionState>()
    val viewState = MutableLiveData<ViewState>()

    init {
        chatHistoryList = chatRepository.getAllChatHistories()
        nearbyDeviceList.value = listOf<MockBluetoothDevice>()
        bluetoothConnectionState.value = BluetoothConnectionState.UNCONNECTED
        viewState.value = ViewState.Launch
        connectedChatHistory.value = null
    }

    fun setNearbyDeviceList(nearbyDeviceList: List<MockBluetoothDevice>) {
        this.nearbyDeviceList.value = nearbyDeviceList
    }

    fun getNearbyDeviceList(): LiveData<List<MockBluetoothDevice>> {
        return nearbyDeviceList
    }

    fun setConnectedChatHistory(chatHistory: ChatHistoryEntity?) {
        connectedChatHistory.value = chatHistory
    }

    fun getConnectedChatHistory(): LiveData<ChatHistoryEntity> {
        return connectedChatHistory
    }

    fun getChatHistoryList(): LiveData<List<ChatHistoryEntity>> {
        return chatHistoryList
    }

    fun getChatHistoryMessage(id: Long): LiveData<List<ChatMessageEntity>> {
        return chatRepository.getAllChatMessagesByID(id)
    }

    fun insertChatHistory(chatHistory: ChatHistoryEntity) {
        viewModelScope.launch { chatRepository.insertChatHistory(chatHistory) }
    }

    fun insertChatMessageAndUpdateChatHistory(chatMessage: ChatMessageEntity) {
        viewModelScope.launch {
            val targetHistoryID = chatMessage.HID
            val targetHistoryList = chatRepository.getChatHistoryByID(targetHistoryID!!)
            val targetHistory = targetHistoryList[0]
            chatMessage.MID = Util.setMessagePrimaryKey(targetHistoryID, targetHistory.size)
            
            targetHistory.incrementSizeByOne()
            targetHistory.lastMessage = chatMessage.content
            targetHistory.lastDate = chatMessage.date
            chatRepository.insertChatMessage(chatMessage)
            chatRepository.updateChatHistory(targetHistory)
        }
    }

    fun deleteChatHistory(chatHistory: ChatHistoryEntity) {
        viewModelScope.launch { chatRepository.deleteChatHistory(chatHistory) }
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
}