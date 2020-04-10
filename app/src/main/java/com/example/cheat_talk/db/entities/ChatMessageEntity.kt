package com.example.cheat_talk.db.entities

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(
    tableName = "chat_message",
    foreignKeys = [ForeignKey(
        entity = ChatHistoryEntity::class,
        parentColumns = ["history_id"],
        childColumns = ["message_owner"],
        onDelete = ForeignKey.CASCADE
    )]
)
class ChatMessageEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "message_id") var MID: Long?,
    @ColumnInfo(name = "message_content") val content: String?,
    @field:TypeConverters(ChatEntityConverter::class)
    @ColumnInfo(name = "message_date")
    val date: Date?,
    @ColumnInfo(name = "message_local") val local: Boolean?,
    @ColumnInfo(name = "message_owner") val HID: Long?
) {
    data class Builder(
        var MID: Long? = null,
        var content: String? = null,
        var date: Date? = null,
        var local: Boolean? = null,
        var HID: Long? = null
    ) {
        fun mid(mid: Long) = apply { this.MID = mid }
        fun content(content: String) = apply { this.content = content }
        fun date(date: Date) = apply { this.date = date }
        fun local(local: Boolean) = apply { this.local = local }
        fun hid(hid: Long) = apply { this.HID = hid}
        fun build(): ChatMessageEntity = ChatMessageEntity(MID, content, date, local, HID)
    }
}