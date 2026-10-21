package com.example.examentercerparcialdavidsilva.data.dto.cat

import com.google.gson.annotations.SerializedName

data class CatFactsResponseDto(
    @SerializedName("current_page") val currentPage: Int? = null,
    @SerializedName("data") val data: List<CatFactDto>? = null,
    @SerializedName("total") val total: Int? = null
)

data class CatFactDto(
    @SerializedName("fact") val fact: String? = null,
    @SerializedName("length") val length: Int? = null
)
