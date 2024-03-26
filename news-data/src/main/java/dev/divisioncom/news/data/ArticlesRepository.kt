package dev.divisioncom.news.data

import dev.divisioncom.news.data.model.Article
import dev.divisioncom.news.database.NewsDatabase
import dev.divisioncom.news.database.models.ArticleDBO
import dev.divisioncom.newsapi.NewsApi
import dev.divisioncom.newsapi.models.ArticleDTO
import dev.divisioncom.newsapi.models.ResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
    private val requestResponseMergeStrategy: RequestResponseMergeStrategy<List<Article>>,
) {

    fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult<List<Article>>> = getAllFromDatabase()
            .map { result ->
                result.map { articlesDbos ->
                    articlesDbos.map { it.toArticle() }
                }
            }

        val remoteArticles: Flow<RequestResult<List<Article>>> = getAllFromServer()
            .map { result ->
                result.map { response ->
                    response.articles.map { it.toArticle() }
                }
            }

        return cachedAllArticles.combine(remoteArticles) { dbos, dtos ->
            requestResponseMergeStrategy.merge(dbos, dtos)
        }
    }

    private fun getAllFromServer(): Flow<RequestResult<ResponseDTO<ArticleDTO>>> {
        val apiRequest = flow { emit(api.everything()) }
            .onEach { result ->
                if (result.isSuccess) {
                    saveNetResponseToCache(checkNotNull(result.getOrThrow()).articles)
                }
            }
            .map { it.toRequestResult() }

        val start = flowOf<RequestResult<ResponseDTO<ArticleDTO>>>(RequestResult.InProgress())
        return merge(apiRequest, start)
    }

    private suspend fun saveNetResponseToCache(data: List<ArticleDTO>) {
        val dbos = data.map { articleDto -> articleDto.toArticleDbo() }
        database.articlesDao.insert(dbos)
    }

    private fun getAllFromDatabase(): Flow<RequestResult<List<ArticleDBO>>> {
        val dbRequest = database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
        val start = flowOf<RequestResult<List<ArticleDBO>>>(RequestResult.InProgress())
        return merge(start, dbRequest)
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }

}
