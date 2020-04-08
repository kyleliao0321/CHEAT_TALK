package com.example.cheat_talk.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.MainActivity
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.HomeFragmentBinding
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.viewmodel.ChatViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment: Fragment() {
    private lateinit var eventListener: HomeFragmentEventListener
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var chatHistoriesAdapter: ChatHistoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        eventListener = (requireActivity() as MainActivity).homeFragmentEventListener
        eventListener.onFragmentCreate()

        val binding: HomeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false)

        chatHistoriesAdapter = ChatHistoriesAdapter(chatHistoryEventListener)
        viewModel.getChatHistoryList().observe(viewLifecycleOwner, Observer {
            chatHistoriesAdapter.chatHistories = it
        })

        val itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback =
            ChatHistoryItemHelper(
                0, ItemTouchHelper.LEFT, chatHistoryEventListener
            )

        with(binding) {
            chatHistoryContainer.adapter = chatHistoriesAdapter
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(chatHistoryContainer)
        }
        return binding.root
    }

    private val chatHistoryEventListener: ChatHistoryEventListener = object: ChatHistoryEventListener {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            val swipedChatHistory: ChatHistoryEntity = chatHistoriesAdapter.chatHistories[position]
            eventListener.onChatHistoryItemSwipedRight(swipedChatHistory)
        }

        override fun onClick(chatHistory: ChatHistoryEntity) {
            eventListener.onChatHistoryItemClick(chatHistory)
        }

    }
}