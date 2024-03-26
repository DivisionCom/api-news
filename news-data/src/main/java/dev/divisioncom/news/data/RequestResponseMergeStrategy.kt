package dev.divisioncom.news.data

interface RequestResponseMergeStrategy<E> {

    fun merge(right: RequestResult<E>, left: RequestResult<E>): RequestResult<E>
}