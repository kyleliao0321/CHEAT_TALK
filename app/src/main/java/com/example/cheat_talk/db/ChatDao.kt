package com.example.cheat_talk.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertChatHistory(chatHistory: ChatHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertChatMessage(chatMessage: ChatMessageEntity)

    @Update
    fun updateChatHistory(chatHistory: ChatHistoryEntity)

    @Delete
    fun deleteChatHistory(chatHistory: ChatHistoryEntity)

    @Query("SELECT * FROM chat_history ORDER BY history_id ASC")
    fun getAllChatHistories(): LiveData<List<ChatHistoryEntity>>

    @Query("SELECT * FROM chat_message WHERE message_owner = :hid ORDER BY message_id ASC")
    fun getAllChatMessageByID(hid: Long): LiveData<List<ChatMessageEntity>>
}