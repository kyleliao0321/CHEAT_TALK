package com.example.cheat_talk.ui.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.ChatFragmentBinding
import com.example.cheat_talk.db.entities.ChatMessageEntity
import java.util.*

class ChatFragment: Fragment() {
    private lateinit var chatMessageAdapter: ChatMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ChatFragmentBinding = ChatFragmentBinding.inflate(inflater, container, false)

        // setup recycler view

        chatMessageAdapter = ChatMessageAdapter()
        chatMessageAdapter.messageList = createMockMessages()

        with(binding) {
            chatHistoryName = "Test Name"
            chatMessagesContainer.adapter = chatMessageAdapter
            sendMessageButton.setOnClickListener(View.OnClickListener {
                sendMessageClick()
            })
        }

        return binding.root
    }

    private fun sendMessageClick() {
        // TODO: emit event to main activity with ChatMessageEntity as argument.
        val editTextView: EditText = view!!.findViewById(R.id.message_buffer)
        val message: String? = editTextView.text.toString()
        if (message != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            editTextView.text.clear()
        }
    }

    private fun createMockMessages(): List<ChatMessageEntity> {
        val messageContents: List<String> = listOf(
            "hello",
            "hello",
            "hello",
            "hello"
        )
        var chatMessageList: MutableList<ChatMessageEntity> = arrayListOf()

        for ((index, value) in messageContents.withIndex()) {
            val dateString: String = Date().toString()
            chatMessageList.add(ChatMessageEntity.Builder()
                .MID(index)
                .content(value)
                .date(dateString)
                .local(index/2 == 0)
                .build()
            )
        }

        return chatMessageList
    }
}