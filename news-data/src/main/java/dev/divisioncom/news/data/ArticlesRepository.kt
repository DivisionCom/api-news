package dev.divisioncom.news.data

import dev.divisioncom.news.data.model.Article
import dev.divisioncom.news.database.NewsDatabase
import dev.divisioncom.newsapi.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    suspend fun getAll(): Flow<Article> {
        api.everything()
        return database.articlesDao()
            .getAll()
            .map {

            }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }
}