package com.example.imagesearch.domain.model


data class ImageSearchResultModel(
    val totalCount: Long?,
    val imageSearchList: List<ImageSearchModel>?
)
data class ImageSearchModel(
    val thumbnail: String?,
    val original: String?,
    val title: String?
)