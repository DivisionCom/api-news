package dev.divisioncom.newsapi.models

import dev.divisioncom.newsapi.utils.DateTimeUTCSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ArticleDTO(
    @SerialName("source") val source: SourceDTO,
    @SerialName("author") val author: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("urlImage") val urlImage: String,
    @SerialName("publishedAt") @Serializable(with = DateTimeUTCSerializer::class)
    val publishedAt: Date,
    @SerialName("content") val content: String,

    )
