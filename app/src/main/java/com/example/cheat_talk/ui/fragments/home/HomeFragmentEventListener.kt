package com.example.cheat_talk.ui.fragments.home

import com.example.cheat_talk.db.entities.ChatHistoryEntity

interface HomeFragmentEventListener {
    fun onChatHistoryItemClick(chatHistory: ChatHistoryEntity)
    fun onChatHistoryItemSwipedRight(chatHistory: ChatHistoryEntity)
}