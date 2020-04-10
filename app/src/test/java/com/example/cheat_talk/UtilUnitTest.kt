package com.example.cheat_talk

import org.junit.Test
import org.junit.Assert.*

class UtilUnitTest {
    @Test
    fun macAddressParseUnitTest() {
        val macAddressList = listOf<String>(
            "11-22-33-44-AA-BB-CC-DD",
            "21-33-54-12-AB-CA-AC-DD",
            "16-12-45-56-AB-BB-CD-AD"
        )
        val macAddressLongList = listOf<Long>(
            1234605617868164317L,
            2392348266232196317,
            1590409856965922221
        )
        for (pair in macAddressList.zip(macAddressLongList)) {
            assertTrue(Util.parseMacAddressToLong(pair.first) == pair.second)
        }
    }
}