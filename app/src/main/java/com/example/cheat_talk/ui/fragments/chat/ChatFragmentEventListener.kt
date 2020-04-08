package com.example.cheat_talk.ui.fragments.chat

import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity

interface ChatFragmentEventListener {
    fun onConnectButtonClick(chatHistory: ChatHistoryEntity)
    fun onDisconnectButtonClick()
    fun onSendMessage(chatMessage: ChatMessageEntity)
    fun onGoBackHomeClick()
}