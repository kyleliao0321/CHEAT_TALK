package com.example.cheat_talk.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
class ChatHistoryEntity (
    @PrimaryKey
    @NonNull
    val HID: Long?,
    var pairedName: String?,
    var lastMessage: String?,
    var lastDate: String?
) {
   data class Builder(
       var HID: Long? = null,
       var pairedName: String? = null,
       var lastMessage: String? = null,
       var lastDate: String? = null
   ) {
       fun HID(hid: Long) = apply { this.HID = HID }
       fun pairedName(pairedName: String) = apply { this.pairedName = pairedName }
       fun lastMessage(lastMessage: String) = apply { this.lastMessage = lastMessage }
       fun lastDate(lastDate: String) = apply { this.lastDate = lastDate }
       fun build(): ChatHistoryEntity = ChatHistoryEntity(HID, pairedName, lastMessage, lastDate)
   }
}