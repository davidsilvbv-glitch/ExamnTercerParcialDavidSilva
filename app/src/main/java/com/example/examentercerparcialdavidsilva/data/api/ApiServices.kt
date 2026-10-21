package com.example.examentercerparcialdavidsilva.data.api

import com.example.examentercerparcialdavidsilva.data.dto.cat.CatFactsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.dog.DogBreedsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.weather.WeatherResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface OpenMeteoApiService {
    @GET("v1/forecast?latitude=-16.5&longitude=-68.15&current=temperature_2m,relative_humidity_2m,wind_speed_10m&timezone=America%2FLa_Paz")
    suspend fun getWeatherForecast(): Response<WeatherResponseDto>
}

interface DogCeoApiService {
    @GET("api/breeds/list/all")
    suspend fun getDogBreeds(): Response<DogBreedsResponseDto>
}

interface CatFactsApiService {
    @GET("facts?limit=10")
    suspend fun getCatFacts(): Response<CatFactsResponseDto>
}
