package com.example.imagesearch.domain.model

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T?) : Result<T>()
    data class Failure(val errorMsg: String) : Result<Nothing>()
    object Loading: Result<Nothing>()
}