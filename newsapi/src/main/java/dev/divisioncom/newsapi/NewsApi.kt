package dev.divisioncom.newsapi

import androidx.annotation.IntRange
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.divisioncom.newsapi.models.Article
import dev.divisioncom.newsapi.models.Language
import dev.divisioncom.newsapi.models.Response
import dev.divisioncom.newsapi.models.SortBy
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Date



// [API documentation](https://newsapi.org/docs/get-started)
interface NewsApi {

    // API details [here](https://newsapi.org/docs/endpoints/everything)

    @GET("/everything")
    suspend fun everything(
        @Query("q") query: String? = null,
        @Query("from") from: Date? = null,
        @Query("to") to: Date? = null,
        @Query("language") language: List<Language>? = null,
        @Query("sortBy") sortBy: SortBy? = null,
        @Query("pageSize") @IntRange(from = 0, to = 100) pageSize: Int = 100,
        @Query("page") @IntRange(from = 1) page: Int = 1,
    ): Response<Article>

}

fun NewsApi(
    baseUrl: String,
): NewsApi {
    val jsonConverterFactory = Json.asConverterFactory(MediaType.get("application/json"))

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .build()

    return retrofit.create(NewsApi::class.java)
}

