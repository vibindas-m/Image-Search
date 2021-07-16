package com.example.imagesearch.domain.model


data class ImageSearchResultModel(
    val paginationModel: PaginationModel?,
    val imageSearchList: List<ImageSearchModel>?
)
data class ImageSearchModel(
    val thumbnail: String?,
    val original: String?,
    val title: String?
)

data class PaginationModel(
    val hasNext: Boolean,
    val totalCount: Long,
    val pageNumber: Int
)