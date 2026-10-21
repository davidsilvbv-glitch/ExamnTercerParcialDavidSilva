package com.example.examentercerparcialdavidsilva.data.dto.dog

import com.google.gson.annotations.SerializedName

data class DogBreedsResponseDto(
    @SerializedName("message") val message: Map<String, List<String>>? = null,
    @SerializedName("status") val status: String? = null
)
