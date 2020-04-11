package com.example.cheat_talk

import java.lang.Long.parseLong

object Util {
    fun parseMacAddressToLong(macAddress: String): Long {
        val cleanMacAddressString = macAddress.replace("-", "")
        return parseLong(cleanMacAddressString, 16)
    }

    fun setMessagePrimaryKey(base: Long, size: Int): Long {
        val idString = base.toString() + size.toString()
        return idString.toLong()
    }
}