package com.example.cheat_talk

import java.lang.Long.parseLong
import java.lang.Long.toHexString
import java.util.*

object Util {
    fun parseMacAddressToLong(macAddress: String): Long {
        val cleanMacAddressString = macAddress.replace("-", "")
        return parseLong(cleanMacAddressString, 16)
    }

    fun parseMacAddressToHexString(macAddress: Long): String {
        return toHexString(macAddress)
    }

    fun setMessagePrimaryKey(base: Long, size: Int): Long {
        val idString = base.toString() + size.toString()
        return idString.toLong()
    }

    fun dateFormatting(date: Date): String {
        val calendar = GregorianCalendar()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)
        return "$year/$month/$day $hour:$minutes"
    }
}