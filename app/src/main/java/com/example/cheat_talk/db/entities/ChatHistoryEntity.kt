package com.example.cheat_talk.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cheat_talk.Util
import java.util.*

@Entity(tableName = "chat_history")
class ChatHistoryEntity (
    @PrimaryKey
    @ColumnInfo(name = "history_id") var HID: Long?,
    @ColumnInfo(name = "history_name") var pairedName: String?,
    @ColumnInfo(name = "history_last_message") var lastMessage: String?,
    @field:TypeConverters(ChatEntityConverter::class)
    @ColumnInfo(name = "history_last_date")
    var lastDate: Date?,
    var size: Int = 0
) {
   data class Builder(
       var hid: Long? = null,
       var pairedName: String? = null,
       var lastMessage: String? = null,
       var lastDate: Date? = null
   ) {
       fun hid(hid: Long) = apply { this.hid = hid }
       fun pairedName(pairedName: String) = apply { this.pairedName = pairedName }
       fun lastMessage(lastMessage: String) = apply { this.lastMessage = lastMessage }
       fun lastDate(lastDate: Date) = apply { this.lastDate = lastDate }
       fun build(): ChatHistoryEntity = ChatHistoryEntity(hid, pairedName, lastMessage, lastDate)
   }

    fun incrementSizeByOne() {
        this.size++
    }

    fun getFormattedDate(): String {
        return Util.dateFormatting(lastDate!!)
    }
}