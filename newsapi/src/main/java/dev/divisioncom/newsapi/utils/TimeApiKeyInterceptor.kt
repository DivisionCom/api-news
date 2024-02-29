package dev.divisioncom.newsapi.utils

import okhttp3.Interceptor
import okhttp3.Response

internal class TimeApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("X-Api-Key", apiKey)
                .build()
        )
    }
}