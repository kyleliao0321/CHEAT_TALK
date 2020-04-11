package com.example.cheat_talk

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.example.cheat_talk.db.entities.ChatMessageEntity
import com.example.cheat_talk.mockDataObject.MockBluetoothDevice
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

/**
 * A mock bluetooth service class
 *
 * @property mHandler: passing message to handle UI update in Main thread.
 */
class BluetoothService: Service(), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private lateinit var mHandler: Handler
    private var connectedJob: Job? = null
    private val binder = LocalBinder()

    inner class LocalBinder: Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun setHandler(handler: Handler) {
        mHandler = handler
    }

    fun startBluetoothDiscovery() = launch {
        mHandler.obtainMessage(Constaints.BLUETOOTH_DISCOVERING).sendToTarget()
        delay(4000)
        val mockBluetoothAddress = listOf<String>("11-44-AA-BB-CC-DD", "34-12-AA-BB-CC-DD")
        val mockBluetoothName = listOf<String>("Kyle", "Johnson")
        val mockBluetoothDevices = mockBluetoothAddress.zip(mockBluetoothName).map { pair ->
            MockBluetoothDevice(pair.first, pair.second)
        }
        mHandler.obtainMessage(Constaints.BLUETOOTH_DISCOVERED, mockBluetoothDevices).sendToTarget()
    }

    fun startBluetoothConnecting(mockBluetoothDevice: MockBluetoothDevice) = launch {
        mHandler.obtainMessage(Constaints.BLUETOOTH_CONNECTING).sendToTarget()
        delay(3000)
        if (Random.nextBoolean()) {
            connectedJob = connectedProcess()
            mHandler.obtainMessage(Constaints.BLUETOOTH_CONNECTED_SUCCESS, mockBluetoothDevice)
                .sendToTarget()
        } else {
            mHandler.obtainMessage(Constaints.BLUETOOTH_CONNECTED_FAIL).sendToTarget()
        }
    }

    fun writeMessage(chatMessage: ChatMessageEntity) = launch {
        mHandler.obtainMessage(Constaints.BLUETOOTH_WRITE_MESSAGE).sendToTarget()
        delay(1000)
    }

    fun startBluetoothDisconnecting() = launch {
        mHandler.obtainMessage(Constaints.BLUETOOTH_DISCONNECTING).sendToTarget()
        delay(2000)
        if (connectedJob != null && connectedJob!!.isActive) {
            connectedJob!!.cancelAndJoin()
            connectedJob = null
        }
        mHandler.obtainMessage(Constaints.BLUETOOTH_DISCONNECTED).sendToTarget()
    }

    private fun connectedProcess() = launch {
        while (isActive) {
            delay(5000)
            val messageContent = "hello"
            mHandler.obtainMessage(Constaints.BLUETOOTH_READ_MESSAGE, messageContent).sendToTarget()
        }
        mHandler.obtainMessage(Constaints.BLUETOOTH_DISCONNECTED).sendToTarget()
    }
}