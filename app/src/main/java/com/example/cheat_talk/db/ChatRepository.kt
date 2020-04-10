package com.example.cheat_talk.db

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ChatRepository(application: Application) {
    private val chatDao: ChatDao

    init {
        // temporary testing
        val db = Room.inMemoryDatabaseBuilder(application.applicationContext, ChatDatabase::class.java).build()
        chatDao = db.chatDao()
    }

    fun getAllChatHistories(): LiveData<List<ChatHistoryEntity>> {
        return chatDao.getAllChatHistories()
    }

    fun getAllChatMessagesByID(hid: Long): LiveData<List<ChatMessageEntity>> {
        return chatDao.getAllChatMessageByID(hid)
    }

    suspend fun getChatHistoryByID(hid: Long): List<ChatHistoryEntity> {
        return chatDao.getChatHistoryByID(hid)
    }

    suspend fun insertChatHistory(chatHistory: ChatHistoryEntity): Boolean {
        return insertChatHistoryAsync(chatHistory)
    }

    suspend fun insertChatMessage(chatMessage: ChatMessageEntity): Boolean {
        return insertChatMessageAsync(chatMessage)
    }

    suspend fun updateChatHistory(chatHistory: ChatHistoryEntity): Boolean {
        return updateChatHistoryAsync(chatHistory)
    }

    suspend fun deleteChatHistory(chatHistory: ChatHistoryEntity): Boolean {
        return deleteChatHistoryAsync(chatHistory)
    }

    private suspend fun insertChatHistoryAsync(chatHistory: ChatHistoryEntity): Boolean {
        return withContext<Boolean>(Dispatchers.IO) {
            return@withContext try {
                chatDao.insertChatHistory(chatHistory)
                true
            } catch(e: Exception) {
                false
            }
        }
    }

    private suspend fun insertChatMessageAsync(chatMessage: ChatMessageEntity): Boolean {
        return withContext<Boolean>(Dispatchers.IO) {
            return@withContext try {
                chatDao.insertChatMessage(chatMessage)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun updateChatHistoryAsync(chatHistory: ChatHistoryEntity): Boolean {
        return withContext<Boolean>(Dispatchers.IO) {
            return@withContext try {
                chatDao.updateChatHistory(chatHistory)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun deleteChatHistoryAsync(chatHistory: ChatHistoryEntity): Boolean {
        return withContext<Boolean>(Dispatchers.IO) {
            return@withContext try {
                chatDao.deleteChatHistory(chatHistory)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}