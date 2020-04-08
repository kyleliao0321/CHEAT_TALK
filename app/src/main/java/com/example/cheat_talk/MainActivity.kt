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
            viewModel.updateViewStateToChat()
        }
        override fun onChatHistoryItemSwipedRight(chatHistory: ChatHistoryEntity) {
            Toast.makeText(this@MainActivity, "Delete ${chatHistory.pairedName}.", Toast.LENGTH_LONG).show()
        }
        override fun onFragmentCreate() {
            viewModel.updateViewStateToHome()
        }
    }

    val discoveryFragmentEventListener: DiscoveryFragmentEventListener = object: DiscoveryFragmentEventListener {
        override fun onDeviceItemSwipeRight(mockBluetoothDevice: MockBluetoothDevice) {
            viewModel.updateViewStateToChat()
        }

        override fun onRefreshButtonClick(): List<MockBluetoothDevice> {
            val mockBluetoothDevice: MockBluetoothDevice = MockBluetoothDevice("11-22-33-AA-CC-DD", "Kyle")
            return listOf(mockBluetoothDevice)
        }

        override fun onFragmentCreate(): List<MockBluetoothDevice> {
            viewModel.updateViewStateToDiscovery()
            val mockBluetoothDevice: MockBluetoothDevice = MockBluetoothDevice("21-32-33-AA-CC-DD", "Jack")
            return listOf(mockBluetoothDevice, mockBluetoothDevice, mockBluetoothDevice)
        }
    }

    val chatFragmentEventListener: ChatFragmentEventListener = object: ChatFragmentEventListener {
        override fun onFragmentCreate(): Long {
            // TODO: get room id
            viewModel.updateViewStateToChat()
            return 12435464665L
        }

        override fun onSendMessage(chatMessage: ChatMessageEntity) {
            // TODO: write the message into bluetooth socket and db
            Toast.makeText(this@MainActivity, chatMessage.content, Toast.LENGTH_LONG).show()
        }

        override fun onGoBackHomeClick() {
            if (!isTablet) {
                viewModel.updateViewStateToHome()
            }
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
                    transition.replace(R.id.dual_chat_container, ChatFragment()).commit()
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
