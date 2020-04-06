package com.example.cheat_talk.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.HomeFragmentBinding
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment: Fragment() {
    private lateinit var eventListener: HomeFragmentEventListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListener = (requireActivity() as MainActivity).homeFragmentEventListener

        val binding: HomeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false)

        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            ChatHistoryItemHelper(
                0, ItemTouchHelper.LEFT, chatHistoryEventListener
            )
        val chatHistoriesAdapter: ChatHistoriesAdapter = ChatHistoriesAdapter(chatHistoryEventListener)
        chatHistoriesAdapter.chatHistories = mockChatHistories()

        with(binding) {
            chatHistoryContainer.adapter = chatHistoriesAdapter
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(chatHistoryContainer)
            discoveryButton.setOnClickListener(View.OnClickListener {
                discoveryButtonClick()
            })
        }

        return binding.root
    }

    private fun mockChatHistories(): List<ChatHistoryEntity> {
        val chatHistory: ChatHistoryEntity =
            ChatHistoryEntity.Builder()
                .HID(2435546464L)
                .pairedName("Kyle")
                .lastMessage("Hellow World")
                .lastDate("2020/03/21 20:30")
                .build()
        return listOf(chatHistory, chatHistory)
    }

    private fun discoveryButtonClick() {
        // TODO: emit the action to main activity, and navigate to device discovery fragment
        eventListener.onDiscoveryButtonClick()
    }

    private val chatHistoryEventListener: ChatHistoryEventListener = object: ChatHistoryEventListener {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            // TODO: emit the action to main activity, to delete the ChatHistory.
            val swipedChatHistory: ChatHistoryEntity = mockChatHistories()[position]
            eventListener.onChatHistoryItemSwipedRight(swipedChatHistory)
        }

        override fun onClick(chatHistory: ChatHistoryEntity) {
            // TODO: emit the action to main activity, to navigate to chat fragment.
            eventListener.onChatHistoryItemClick(chatHistory)
        }

    }
}