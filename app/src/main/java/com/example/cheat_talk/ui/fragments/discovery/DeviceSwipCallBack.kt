package com.example.cheat_talk.ui.fragments.discovery

import androidx.recyclerview.widget.RecyclerView

interface DeviceSwipeCallBack {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
}