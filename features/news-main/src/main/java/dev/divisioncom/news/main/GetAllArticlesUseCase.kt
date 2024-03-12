package dev.divisioncom.news.main

import dev.divisioncom.news.data.ArticlesRepository
import dev.divisioncom.news.data.model.Article
import kotlinx.coroutines.flow.Flow

class GetAllArticlesUseCase(private val repository: ArticlesRepository) {

    operator suspend fun invoke(): Flow<Article> {
        return repository.getAll()
    }
}