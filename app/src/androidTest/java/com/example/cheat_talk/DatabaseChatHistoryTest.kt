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
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class DatabaseChatHistoryTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var chatDao: ChatDao
    private lateinit var db: ChatDatabase
    private lateinit var dbChatHistoryList: LiveData<List<ChatHistoryEntity>>
    private val localChatHistoryList: ArrayList<ChatHistoryEntity> = ArrayList<ChatHistoryEntity>()

    @Before
    fun initializeDatabaseAndStartObserver() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java).build()
        chatDao = db.chatDao()
        dbChatHistoryList = chatDao.getAllChatHistories()
        dbChatHistoryList.observeForever(chatHistoryObserver)
    }

    @After
    @Throws(IOException::class)
    fun closeDatabaseAndRemoveObserver() {
        dbChatHistoryList.removeObserver(chatHistoryObserver)
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun chatHistoryInsertAndUpdateAndDeleteTest() {
        // inserting mock chat history
        val mockChatHistoryIDs = listOf<Long>(132435454L, 132435455L, 132435456L)
        val mockChatHistoryNames = listOf<String>("Kyle", "Johnson", "Anna")
        for (pair in mockChatHistoryIDs.zip(mockChatHistoryNames)) {
            val chatHistory = ChatHistoryEntity.Builder()
                .hid(pair.first)
                .pairedName(pair.second)
                .lastMessage("testste")
                .lastDate(Date())
                .build()
            localChatHistoryList.add(chatHistory)
            chatDao.insertChatHistory(chatHistory)
        }

        // updating existed chat history
        for (chatHistory in localChatHistoryList) {
            chatHistory.incrementSizeByOne()
            chatDao.updateChatHistory(chatHistory)
        }

        // delete existed chat history
        val tempList = localChatHistoryList.map { history -> history }
        for (tempHistory in tempList) {
            localChatHistoryList.remove(tempHistory)
            chatDao.deleteChatHistory(tempHistory)
        }
    }

    private val chatHistoryObserver = Observer<List<ChatHistoryEntity>> {
        Assert.assertTrue(localChatHistoryList.size == it.size)
        for (pair in localChatHistoryList.zip(it)) {
            println("Local chat history ${pair.first.pairedName} has size : ${pair.first.size}")
            println("db chat history ${pair.second.pairedName} has size : ${pair.second.size}")
            Assert.assertTrue(pair.first.HID == pair.second.HID)
            Assert.assertTrue(pair.first.pairedName == pair.second.pairedName)
            Assert.assertTrue(pair.first.lastDate == pair.second.lastDate)
            Assert.assertTrue(pair.first.size == pair.second.size)
        }
    }
}