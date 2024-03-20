package dev.divisioncom.news.data

import dev.divisioncom.news.data.model.Article
import dev.divisioncom.news.database.NewsDatabase
import dev.divisioncom.news.database.models.ArticleDBO
import dev.divisioncom.newsapi.NewsApi
import dev.divisioncom.newsapi.models.ArticleDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import java.io.IOException

class ArticlesRepository(
    private val database: NewsDatabase,
    private val api: NewsApi,
) {

    fun getAll(): Flow<RequestResult<List<Article>>> {
        val cachedAllArticles: Flow<RequestResult.Success<List<ArticleDBO>>> = getAllFromDatabase()

        val remoteArticles: Flow<RequestResult.Success<*>> = getAllFromServer()

        cachedAllArticles.map
        {

        }
        return cachedAllArticles.combine(remoteArticles)
        {

        }
    }

    private fun getAllFromServer(): Flow<RequestResult<List<ArticleDBO>>> {
        return flow { emit(api.everything()) }
            .map { result ->
                if (result.isSuccess) {
                    val response = result.getOrThrow()
                    RequestResult.Success(response.articles)
                } else {
                    RequestResult.Error(null)
                }
            }
            .filterIsInstance<RequestResult.Success<List<ArticleDTO>>>()
            .map { requestResult: RequestResult.Success<List<ArticleDTO>> ->
                requestResult.map { dtos -> dtos.map { articleDto -> articleDto.toArticleDbo() } }
            }.onEach { requestResult: RequestResult<List<ArticleDBO>> ->
                database.articlesDao.insert(requestResult.data)
            }
    }

    private fun getAllFromDatabase(): Flow<RequestResult.Success<List<ArticleDBO>>> {
        return database.articlesDao
            .getAll()
            .map { RequestResult.Success(it) }
    }

    suspend fun search(query: String): Flow<Article> {
        api.everything()
        TODO("Not implemented")
    }
}

sealed class RequestResult<E>(internal val data: E) {

    class InProgress<E>(data: E) : RequestResult<E>(data)
    class Success<E>(data: E) : RequestResult<E>(data)
    class Error<E>(data: E) : RequestResult<E>(data)
}

internal fun <T : Any> RequestResult<T?>.requireData(): T = checkNotNull(data)

internal fun <I, O> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    val outData = mapper(data)
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(outData)
        is RequestResult.Error -> RequestResult.Error(outData)
        is RequestResult.InProgress -> RequestResult.InProgress(outData)
    }
}
