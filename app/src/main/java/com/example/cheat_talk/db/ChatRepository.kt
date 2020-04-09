package com.example.cheat_talk.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ChatRepository(context: Context): CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val chatDao: ChatDao

    init {
        // temporary testing
        val db = Room.inMemoryDatabaseBuilder(context.applicationContext, ChatDatabase::class.java).build()
        chatDao = db.chatDao()
    }

    fun getAllChatHistories(): LiveData<List<ChatHistoryEntity>> {
        return chatDao.getAllChatHistories()
    }

    fun getAllChatMessagesByID(hid: Long): LiveData<List<ChatMessageEntity>> {
        return chatDao.getAllChatMessageByID(hid)
    }

    fun insertChatHistory(chatHistory: ChatHistoryEntity) {
        launch { insertChatHistoryAsync(chatHistory) }
    }

    fun insertChatMessage(chatMessage: ChatMessageEntity) {
        launch { insertChatMessageAsync(chatMessage) }
    }

    fun updateChatHistory(chatHistory: ChatHistoryEntity) {
        launch { updateChatHistoryAsync(chatHistory) }
    }

    fun deleteChatHistory(chatHistory: ChatHistoryEntity) {
        launch { deleteChatHistoryAsync(chatHistory) }
    }

    private suspend fun insertChatHistoryAsync(chatHistory: ChatHistoryEntity) {
        withContext(Dispatchers.IO) {
            chatDao.insertChatHistory(chatHistory)
        }
    }

    private suspend fun insertChatMessageAsync(chatMessage: ChatMessageEntity) {
        withContext(Dispatchers.IO) {
            chatDao.insertChatMessage(chatMessage)
        }
    }

    private suspend fun updateChatHistoryAsync(chatHistory: ChatHistoryEntity) {
        withContext(Dispatchers.IO) {
            chatDao.updateChatHistory(chatHistory)
        }
    }

    private suspend fun deleteChatHistoryAsync(chatHistory: ChatHistoryEntity) {
        withContext(Dispatchers.IO) {
            chatDao.deleteChatHistory(chatHistory)
        }
    }
}