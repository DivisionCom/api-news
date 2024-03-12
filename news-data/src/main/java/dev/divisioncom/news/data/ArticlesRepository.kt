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

    fun getAll(): Flow<List<Article>> {
        return database.articlesDao
            .getAll()
            .map { articles -> articles.map { it.toArticle() } }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }
}