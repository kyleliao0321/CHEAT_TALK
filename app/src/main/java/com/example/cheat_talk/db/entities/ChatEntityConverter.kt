package com.example.cheat_talk.db.entities

import androidx.room.TypeConverter
import java.util.*

class ChatEntityConverter {
    @TypeConverter
    fun fromTimeStamp(date: Long?): Date? {
        return when(date) {
            null -> null
            else -> Date(date)
        }
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return when(date) {
            null -> null
            else -> date.time
        }
    }
}