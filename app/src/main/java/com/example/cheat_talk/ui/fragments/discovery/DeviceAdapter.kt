package com.example.cheat_talk.ui.fragments.discovery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cheat_talk.R
import com.example.cheat_talk.databinding.DeviceItemBinding
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice

class DeviceAdapter: RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {
    lateinit var deviceList: List<MockBluetoothDevice>

    inner class ViewHolder(private val binding: DeviceItemBinding): RecyclerView.ViewHolder(binding.root) {
        val foregroundView: View = binding.root.findViewById<View>(R.id.device_item_foreground)
        fun bind(item: MockBluetoothDevice) {
            binding.device = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: DeviceItemBinding = DeviceItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }
}