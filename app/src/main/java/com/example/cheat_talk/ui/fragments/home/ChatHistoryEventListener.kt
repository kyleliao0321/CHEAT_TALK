package com.example.cheat_talk.ui.fragments.home

import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.db.entities.ChatHistoryEntity

interface ChatHistoryEventListener {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    fun onClick(chatHistory: ChatHistoryEntity)
}