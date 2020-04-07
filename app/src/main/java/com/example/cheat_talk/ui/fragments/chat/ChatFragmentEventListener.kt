package com.example.cheat_talk.ui.fragments.chat

import com.example.cheat_talk.db.entities.ChatMessageEntity

interface ChatFragmentEventListener {
    fun onFragmentCreate(): Long
    fun onSendMessage(chatMessage: ChatMessageEntity)
    fun onGoBackHomeClick()
}