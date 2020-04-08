package com.example.cheat_talk.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.ChatHistoryItemBinding
import com.example.cheat_talk.db.entities.ChatHistoryEntity

class ChatHistoriesAdapter(private val listener: ChatHistoryEventListener): RecyclerView.Adapter<ChatHistoriesAdapter.ViewHolder>() {
    var chatHistories: List<ChatHistoryEntity> = listOf<ChatHistoryEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ChatHistoryItemBinding = ChatHistoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return chatHistories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatHistories[position])
    }

    inner class ViewHolder(private val binding: ChatHistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        val foregroundView: View = binding.root.findViewById(R.id.chat_history_item_foreground)
        fun bind(item: ChatHistoryEntity) {
            with(binding) {
                chatHistory = item
                clickCallBack = listener
            }
        }
    }
}