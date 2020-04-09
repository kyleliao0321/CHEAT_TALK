package com.example.cheat_talk

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cheat_talk.db.ChatDao
import com.example.cheat_talk.db.ChatDatabase
import com.example.cheat_talk.db.entities.ChatHistoryEntity
import com.example.cheat_talk.db.entities.ChatMessageEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

@RunWith(AndroidJUnit4::class)
class DatabaseChatMessageTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var db: ChatDatabase
    private lateinit var chatDao: ChatDao
    private lateinit var currentChatHistory: ChatHistoryEntity
    private lateinit var dbChatHistoryList: LiveData<List<ChatMessageEntity>>
    private val localChatMessageList: ArrayList<ChatMessageEntity> = ArrayList<ChatMessageEntity>()


    @Before
    fun initializeDatabaseAndChatHistory() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java).build()
        chatDao = db.chatDao()
        currentChatHistory = ChatHistoryEntity.Builder()
            .hid(354546464646L)
            .pairedName("Kyle")
            .lastMessage("3121")
            .lastDate(Date())
            .build()
        chatDao.insertChatHistory(currentChatHistory)

        dbChatHistoryList = chatDao.getAllChatMessageByID(currentChatHistory.HID!!)
        dbChatHistoryList.observeForever(dbChatMessageObserver)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabaseAndRemoveObserver() {
        dbChatHistoryList.removeObserver(dbChatMessageObserver)
        db.close()
    }

    @Test
    fun chatMessageInsertAndDeleteTest() {
        val mockChatContent = listOf<String>("Hello", "World", "31424", "12425353")
        val mockLocal = listOf<Boolean>(true, false, true, false)
        val chatHistoryMid: Long = currentChatHistory.HID!!

        for (pair in mockChatContent.zip(mockLocal)) {
            val chatMessage = ChatMessageEntity.Builder()
                .mid(midGenerator(chatHistoryMid, currentChatHistory.size))
                .hid(chatHistoryMid)
                .content(pair.first)
                .local(pair.second)
                .date(Date())
                .build()
            localChatMessageList.add(chatMessage)
            chatDao.insertChatMessage(chatMessage)

            currentChatHistory.incrementSizeByOne()
            chatDao.updateChatHistory(currentChatHistory)
        }

        localChatMessageList.clear()
        chatDao.deleteChatHistory(currentChatHistory)
    }

    private fun midGenerator(base: Long, size: Int): Long {
        val midString: String = base.toString() + size.toString()
        return midString.toLong()
    }

    private val dbChatMessageObserver = Observer<List<ChatMessageEntity>> {
        println("Current size: ${it.size}")
        assertTrue(localChatMessageList.size == it.size)
        for (pair in localChatMessageList.zip(it)) {
            assertTrue(pair.first.MID == pair.second.MID)
            assertTrue(pair.first.content == pair.second.content)
            assertTrue(pair.first.date == pair.second.date)
            assertTrue(pair.first.local == pair.second.local)
        }
    }
}