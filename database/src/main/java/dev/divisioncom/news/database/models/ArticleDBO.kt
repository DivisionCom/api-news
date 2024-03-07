package dev.divisioncom.news.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ArticleDBO(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("source") @Embedded val source: Source,
    @ColumnInfo("author") val author: String,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("url") val url: String,
    @ColumnInfo("urlImage") val urlImage: String,
    @ColumnInfo("publishedAt") val publishedAt: Date,
    @ColumnInfo("content") val content: String,
)

data class Source(
    @ColumnInfo("id") val id: String,
    @ColumnInfo("name") val name: String,
)

