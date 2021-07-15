package com.example.imagesearch.domain.model

interface UseCase <P, R> {
    fun execute(params: P): R
}

interface UseCaseWithParams <P, R> {
    fun execute(params: P): R
}

interface Cancellable {
    fun cancel()
}