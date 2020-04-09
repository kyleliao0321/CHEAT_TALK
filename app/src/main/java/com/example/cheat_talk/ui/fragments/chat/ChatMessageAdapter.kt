package com.example.cheat_talk.ui.fragments.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.databinding.LocalMessageBinding
import com.example.cheat_talk.databinding.RemoteMessageBinding
import com.example.cheat_talk.db.entities.ChatMessageEntity

class ChatMessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messageList: List<ChatMessageEntity> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val LOCAL_MESSAGE_TYPE: Int = 0
    private val REMOTE_MESSAGE_TYPE: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == LOCAL_MESSAGE_TYPE) {
            val binding: LocalMessageBinding = LocalMessageBinding.inflate(inflater, parent, false)
            LocalMessageViewHolder(binding)
        } else {
            val binding: RemoteMessageBinding = RemoteMessageBinding.inflate(inflater, parent, false)
            RemoteMessageViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type: Int = getItemViewType(position)
        val currentItem: ChatMessageEntity = messageList[position]
        if (type == LOCAL_MESSAGE_TYPE) {
            (holder as LocalMessageViewHolder).bind(currentItem)
        } else {
            (holder as RemoteMessageViewHolder).bind(currentItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage: ChatMessageEntity = messageList[position]
        return if (currentMessage.local!!) {
            LOCAL_MESSAGE_TYPE
        } else {
            REMOTE_MESSAGE_TYPE
        }
    }

    inner class LocalMessageViewHolder(private val binding: LocalMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageEntity) {
            with(binding) {
                messageText = item.content
                messageDate = item.date.toString()
            }
        }
    }
    inner class RemoteMessageViewHolder(private val binding: RemoteMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatMessageEntity) {
            with(binding) {
                messageText = item.content
                messageDate = item.date.toString()
            }
        }
    }
}