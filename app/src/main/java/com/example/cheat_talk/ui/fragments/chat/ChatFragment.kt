package com.example.cheat_talk.ui.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.ChatFragmentBinding
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.viewmodel.ChatViewModel
import java.util.*
import kotlin.properties.Delegates

class ChatFragment: Fragment() {
    private lateinit var eventListener: ChatFragmentEventListener
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private var viewChatHistory: ChatHistoryEntity? = null
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListener = (requireActivity() as MainActivity).chatFragmentEventListener

        val binding: ChatFragmentBinding = ChatFragmentBinding.inflate(inflater, container, false)

        chatMessageAdapter = ChatMessageAdapter()
        viewModel.getChatHistoryMessage(324243535L).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            chatMessageAdapter.messageList = it
        })

        viewChatHistory = viewModel.viewChatHistory
        viewModel.getConnectedChatHistory().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(viewChatHistory == it) {
                true -> {
                    binding.chatConnectButton.hide()
                    binding.chatDisconnectButton.show()
                    binding.sendMessageButton.setOnClickListener(View.OnClickListener {
                        sendMessageClick()
                    })
                }
                false -> {
                    binding.chatConnectButton.show()
                    binding.chatDisconnectButton.hide()
                    binding.sendMessageButton.setOnClickListener(View.OnClickListener {
                        Toast.makeText(requireActivity(), "Cannot send message while disconnected!", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        })


        with(binding) {
            chatHistory = viewChatHistory
            chatMessagesContainer.adapter = chatMessageAdapter
            backHome.setOnClickListener(View.OnClickListener {
                goBackHomeClick()
            })
            chatConnectButton.setOnClickListener(View.OnClickListener {
                onConnectButtonClick()
            })
            chatDisconnectButton.setOnClickListener(View.OnClickListener {
                onDisconnectButtonClick()
            })
        }

        return binding.root
    }

    private fun goBackHomeClick() {
        eventListener.onGoBackHomeClick()
    }

    private fun onConnectButtonClick() {
        eventListener.onConnectButtonClick(viewChatHistory!!)
    }

    private fun onDisconnectButtonClick() {
        eventListener.onDisconnectButtonClick()
    }

    private fun sendMessageClick() {
        val editTextView: EditText = requireView().findViewById(R.id.message_buffer)
        val message: String? = editTextView.text.toString()
        if (message != null) {
            val chatMessage: ChatMessageEntity = ChatMessageEntity.Builder()
                .mid(24353535535L)
                .content(message)
                .date(Date())
                .local(true)
                .hid(24353535535L)
                .build()
            editTextView.text.clear()
            eventListener.onSendMessage(chatMessage)
        }
    }
}