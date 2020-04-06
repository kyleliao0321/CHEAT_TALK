package com.example.cheat_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import com.example.cheat_talk.ui.fragments.chat.ChatFragment
import com.example.cheat_talk.ui.fragments.chat.ChatFragmentEventListener
import com.example.cheat_talk.ui.fragments.discovery.DiscoveryFragment
import com.example.cheat_talk.ui.fragments.discovery.DiscoveryFragmentEventListener
import com.example.cheat_talk.ui.fragments.home.HomeFragment
import com.example.cheat_talk.ui.fragments.home.HomeFragmentEventListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chatFragment: ChatFragment = ChatFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.launch_fragment_container, chatFragment)
            .commit()
    }

    val homeFragmentEventListener: HomeFragmentEventListener = object: HomeFragmentEventListener {
        override fun onChatHistoryItemClick(chatHistory: ChatHistoryEntity) {
            Toast.makeText(this@MainActivity, "Enter ${chatHistory.pairedName}.", Toast.LENGTH_LONG).show()
        }
        override fun onChatHistoryItemSwipedRight(chatHistory: ChatHistoryEntity) {
            Toast.makeText(this@MainActivity, "Delete ${chatHistory.pairedName}.", Toast.LENGTH_LONG).show()
        }
        override fun onDiscoveryButtonClick() {
            Toast.makeText(this@MainActivity, "Start device discovery", Toast.LENGTH_LONG).show()
        }
    }

    val discoveryFragmentEventListener: DiscoveryFragmentEventListener = object: DiscoveryFragmentEventListener {
        override fun onDeviceItemSwipeRight(mockBluetoothDevice: MockBluetoothDevice) {
            Toast.makeText(
                this@MainActivity,
                "${mockBluetoothDevice.name} : ${mockBluetoothDevice.address}",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onRefreshButtonClick(): List<MockBluetoothDevice> {
            Toast.makeText(this@MainActivity, "refresh list", Toast.LENGTH_LONG).show()
            val mockBluetoothDevice: MockBluetoothDevice = MockBluetoothDevice("11-22-33-AA-CC-DD", "Kyle")
            return listOf(mockBluetoothDevice)
        }

        override fun onFragmentCreate(): List<MockBluetoothDevice> {
            val mockBluetoothDevice: MockBluetoothDevice = MockBluetoothDevice("21-32-33-AA-CC-DD", "Jack")
            return listOf(mockBluetoothDevice, mockBluetoothDevice, mockBluetoothDevice)
        }
    }

    val chatFragmentEventListener: ChatFragmentEventListener = object: ChatFragmentEventListener {
        override fun onFragmentCreate(): Long {
            // TODO: get room id
            return 12435464665L
        }

        override fun onSendMessage(chatMessage: ChatMessageEntity) {
            // TODO: write the message into bluetooth socket and db
            Toast.makeText(this@MainActivity, chatMessage.content, Toast.LENGTH_LONG).show()
        }
    }
}
