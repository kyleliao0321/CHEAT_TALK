package com.example.cheat_talk.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
class ChatMessageEntity(
    @PrimaryKey
    @NonNull
    val MID: Int?,
    val content: String?,
    val date: String?,
    val local: Boolean?
) {
    data class Builder(
        var MID: Int? = null,
        var content: String? = null,
        var date: String? = null,
        var local: Boolean? = null
    ) {
        fun MID(mid: Int) = apply { this.MID = mid }
        fun content(content: String) = apply { this.content = content }
        fun date(date: String) = apply { this.date = date }
        fun local(local: Boolean) = apply { this.local = local }
        fun build(): ChatMessageEntity = ChatMessageEntity(MID, content, date, local)
    }
}