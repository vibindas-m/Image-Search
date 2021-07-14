package com.example.imagesearch.data

data class ImageSearchResponse(
    val _type: String?,
    val totalCount: Long?,
    val value: List<ImageSearchData>?
)

data class ImageSearchData(
    val url: String?,
    val height: Int?,
    val width: Int?,
    val thumbnail: String?,
    val thumbnailHeight: Int?,
    val thumbnailWidth: Int?,
    val base64Encoding: String?,
    val name: String?,
    val title: String?,
    val provider: Provider?,
    val imageWebSearchUrl: String?,
    val webpageUrl: String?
)

data class Provider(
    val name: String?,
    val favIcon: String?,
    val favIconBase64Encoding: String?
)