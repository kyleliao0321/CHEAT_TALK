package com.example.cheat_talk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.cheat_talk.databinding.ActivityMainBinding
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import com.example.cheat_talk.ui.fragments.chat.ChatFragment
import com.example.cheat_talk.ui.fragments.chat.ChatFragmentEventListener
import com.example.cheat_talk.ui.fragments.discovery.DiscoveryFragment
import com.example.cheat_talk.ui.fragments.discovery.DiscoveryFragmentEventListener
import com.example.cheat_talk.ui.fragments.home.HomeFragment
import com.example.cheat_talk.ui.fragments.home.HomeFragmentEventListener
import com.example.cheat_talk.ui.fragments.launch.LaunchFragment
import com.example.cheat_talk.ui.fragments.launch.LaunchFragmentEventListener
import com.example.cheat_talk.viewmodel.ChatViewModel
import com.example.cheat_talk.viewmodel.ViewState
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private val launchFragment: LaunchFragment = LaunchFragment()
    private var isTablet by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTablet = resources.getBoolean(R.bool.isTablet)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val bottomNavigationView = binding.bottomMenu
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_discovery_button -> {
                    viewModel.updateViewStateToDiscovery()
                    true
                }
                R.id.menu_home_button -> {
                    viewModel.updateViewStateToHome()
                    true
                }
                else -> false
            }
        }
        viewModel.viewState.observe(this, navigationObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Destroy Activity", Toast.LENGTH_LONG).show()
    }

    val homeFragmentEventListener: HomeFragmentEventListener = object: HomeFragmentEventListener {
        override fun onChatHistoryItemClick(chatHistory: ChatHistoryEntity) {
            viewModel.viewChatHistory = chatHistory
            viewModel.updateViewStateToChat()
        }
        override fun onChatHistoryItemSwipedRight(chatHistory: ChatHistoryEntity) {
            viewModel.deleteChatHistory(chatHistory)
        }
    }

    val discoveryFragmentEventListener: DiscoveryFragmentEventListener = object: DiscoveryFragmentEventListener {
        override fun onDeviceItemSwipeRight(mockBluetoothDevice: MockBluetoothDevice) {
            /* TODO:
                Searching in chat histories, find weather connection establish before.
                Should start connection in service, and update to connecting.
                Only when connection establish, update to connected.
             */
            val parsedMacAddress = Util.parseMacAddressToLong(mockBluetoothDevice.address)
            val newChatHistory = ChatHistoryEntity.Builder()
                .hid(parsedMacAddress)
                .pairedName(mockBluetoothDevice.name)
                .lastMessage("")
                .lastDate(Date())
                .build()
            viewModel.insertChatHistory(newChatHistory)
            viewModel.viewChatHistory = newChatHistory
            viewModel.setConnectedChatHistory(newChatHistory)
            viewModel.updateViewStateToChat()
            viewModel.updateBluetoothConnectionStateToConnected()
        }

        override fun onRefreshButtonClick() {
            /* TODO:
                Should start discovery process in service, and update to discovering.
                When discovering process finish, update to unconnected.
             */
            viewModel.updateBluetoothConnectionStateToDiscovering()
            val mockBluetoothDevice: MockBluetoothDevice = MockBluetoothDevice("11-22-33-AA-CC-DD", "Kyle")
            viewModel.setNearbyDeviceList(listOf(mockBluetoothDevice))
            viewModel.updateBluetoothConnectionStateToUnconnected()
        }

        override fun onDisconnectButtonClick() {
            /* TODO:
                Should start to disconnect current bluetooth connection.
                When process finish, update to unconnected.
             */
            viewModel.setConnectedChatHistory(null)
            viewModel.updateBluetoothConnectionStateToUnconnected()
            viewModel.setNearbyDeviceList(listOf())
        }
    }

    val chatFragmentEventListener: ChatFragmentEventListener = object: ChatFragmentEventListener {
        override fun onSendMessage(chatMessage: ChatMessageEntity) {
            // TODO: write the message into bluetooth socket and db
            viewModel.insertChatMessageAndUpdateChatHistory(chatMessage)
        }

        override fun onGoBackHomeClick() {
            if (!isTablet) {
                viewModel.updateViewStateToHome()
            }
        }

        override fun onConnectButtonClick(chatHistory: ChatHistoryEntity) {
            /* TODO:
                Fetch mac address fro argument, and start bluetooth discovery process.
                Find if there's same mac address device nearby.
                True -> Start connection -> update bluetooth connection state into CONNECTED
                False -> Connection Fail -> update bluetooth connection state into UNCONNECTED
             */
            viewModel.setConnectedChatHistory(chatHistory)
            viewModel.updateBluetoothConnectionStateToConnected()
        }

        override fun onDisconnectButtonClick() {
            /* TODO:
                Disconnect from current connected device
                Once finish, update into UNCONNECTED, and clean up ViewModel's connectedChatHistory
             */
            viewModel.setConnectedChatHistory(null)
            viewModel.updateBluetoothConnectionStateToUnconnected()
        }
    }

    val launchFragmentEventListener: LaunchFragmentEventListener = object : LaunchFragmentEventListener {
        override fun onFinishLaunching() {
            supportFragmentManager.beginTransaction()
                .remove(launchFragment)
                .commit()
            viewModel.updateViewStateToHome()
        }
    }

    private val navigationObserver: Observer<ViewState> = Observer<ViewState> {
        val transition = supportFragmentManager.beginTransaction()
        if (isTablet) {
            when(it) {
                ViewState.Launch -> {
                    transition.replace(R.id.launch_container, launchFragment).commit()
                    binding.bottomMenu.visibility = View.GONE
                }
                ViewState.Home -> {
                    transition.replace(R.id.dual_master_container, HomeFragment()).commit()
                    binding.bottomMenu.visibility = View.VISIBLE
                }
                ViewState.Discovery -> {
                    transition.replace(R.id.dual_master_container, DiscoveryFragment()).commit()
                    binding.bottomMenu.visibility = View.VISIBLE
                }
                ViewState.Chat -> {
                    transition
                        .replace(R.id.dual_master_container, HomeFragment())
                        .replace(R.id.dual_chat_container, ChatFragment()).commit()
                    binding.bottomMenu.visibility = View.VISIBLE
                }
            }
        } else {
            when(it) {
                ViewState.Launch -> {
                    transition.replace(R.id.launch_container, launchFragment).commit()
                    binding.bottomMenu.visibility = View.GONE
                }
                ViewState.Home -> {
                    transition.replace(R.id.nav_graph_container, HomeFragment()).commit()
                    binding.bottomMenu.visibility = View.VISIBLE
                }
                ViewState.Discovery -> {
                    transition.replace(R.id.nav_graph_container, DiscoveryFragment()).commit()
                    binding.bottomMenu.visibility = View.VISIBLE
                }
                ViewState.Chat -> {
                    transition.replace(R.id.nav_graph_container, ChatFragment()).commit()
                    binding.bottomMenu.visibility = View.GONE
                }
            }
        }
    }
}
