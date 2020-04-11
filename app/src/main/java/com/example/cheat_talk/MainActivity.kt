package com.example.cheat_talk

import android.annotation.SuppressLint
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
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
import com.example.cheat_talk.ui.fragments.loading.LoadingFragment
import com.example.cheat_talk.viewmodel.ChatViewModel
import com.example.cheat_talk.viewmodel.ViewState
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()
    private val launchFragment: LaunchFragment = LaunchFragment()
    private var loadingFragment: LoadingFragment? = null
    private var isTablet by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var mService: BluetoothService

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

    override fun onStart() {
        super.onStart()
        Intent(this, BluetoothService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
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
            mService.startBluetoothConnecting(mockBluetoothDevice)
        }

        override fun onRefreshButtonClick() {
            mService.startBluetoothDiscovery()
            viewModel.updateBluetoothConnectionStateToDiscovering()
            viewModel.updateBluetoothConnectionStateToUnconnected()
        }

        override fun onDisconnectButtonClick() {
            mService.startBluetoothDisconnecting()
        }
    }

    val chatFragmentEventListener: ChatFragmentEventListener = object: ChatFragmentEventListener {
        override fun onSendMessage(chatMessage: ChatMessageEntity) {
            mService.writeMessage(chatMessage)
            viewModel.insertChatMessageAndUpdateChatHistory(chatMessage)
        }

        override fun onGoBackHomeClick() {
            if (!isTablet) {
                viewModel.updateViewStateToHome()
            }
        }

        override fun onConnectButtonClick(chatHistory: ChatHistoryEntity) {
            val macAddressHexString = Util.parseMacAddressToHexString(chatHistory!!.HID!!)
            val mockBluetoothDevice = MockBluetoothDevice(macAddressHexString, chatHistory.pairedName!!)
            mService.startBluetoothConnecting(mockBluetoothDevice)
        }

        override fun onDisconnectButtonClick() {
            mService.startBluetoothDisconnecting()
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

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object: Handler() {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                Constaints.BLUETOOTH_READ_MESSAGE -> {
                    val messageContent: String = msg.obj as String
                    val currentChatHistory = viewModel.getConnectedChatHistory().value!!
                    val chatMessage = ChatMessageEntity.Builder()
                        .content(messageContent)
                        .date(Date())
                        .local(false)
                        .hid(currentChatHistory.HID!!)
                        .build()
                    viewModel.insertChatMessageAndUpdateChatHistory(chatMessage)
                }
                Constaints.BLUETOOTH_WRITE_MESSAGE -> {
                    Toast.makeText(this@MainActivity, "Write", Toast.LENGTH_SHORT).show()
                }
                Constaints.BLUETOOTH_DISCOVERING -> {
                    loadingFragment = LoadingFragment.newInstance("discovering")
                    loadingFragment!!.show(supportFragmentManager, "discovering")
                }
                Constaints.BLUETOOTH_DISCOVERED -> {
                    val deviceList = msg.obj as List<MockBluetoothDevice>
                    viewModel.setNearbyDeviceList(deviceList)
                    loadingFragment!!.dismiss()
                    loadingFragment = null
                }
                Constaints.BLUETOOTH_CONNECTING -> {
                    loadingFragment = LoadingFragment.newInstance("connecting")
                    loadingFragment!!.show(supportFragmentManager, "connecting")
                }
                Constaints.BLUETOOTH_CONNECTED_SUCCESS -> {
                    val mockBluetoothDevice = msg.obj as MockBluetoothDevice
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
                    loadingFragment!!.dismiss()
                    loadingFragment = null
                }
                Constaints.BLUETOOTH_CONNECTED_FAIL -> {
                    loadingFragment!!.dismiss()
                    loadingFragment = null
                }
                Constaints.BLUETOOTH_DISCONNECTED -> {
                    viewModel.setConnectedChatHistory(null)
                    viewModel.updateBluetoothConnectionStateToUnconnected()
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    private val connection = object: ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Toast.makeText(this@MainActivity, "Disconnecting from service!", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BluetoothService.LocalBinder
            mService = binder.getService()
            mService.setHandler(mHandler)
        }

    }
}
