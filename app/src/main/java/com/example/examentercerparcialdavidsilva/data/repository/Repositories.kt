package com.example.examentercerparcialdavidsilva.data.repository

import com.example.examentercerparcialdavidsilva.data.api.CatFactsApiService
import com.example.examentercerparcialdavidsilva.data.api.DogCeoApiService
import com.example.examentercerparcialdavidsilva.data.api.OpenMeteoApiService
import com.example.examentercerparcialdavidsilva.data.dto.cat.CatFactsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.dog.DogBreedsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.weather.WeatherResponseDto

/**
 * Repositorio 1: Encargado de consultar la API 1 (Open-Meteo Weather)
 */
class OpenMeteoRepository(
    private val apiService: OpenMeteoApiService
) {
    suspend fun fetchWeatherForecast(): Result<WeatherResponseDto> {
        return try {
            val response = apiService.getWeatherForecast()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Error en API Open-Meteo: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Repositorio 2: Encargado de consultar la API 2 (Dog CEO API)
 */
class DogBreedRepository(
    private val apiService: DogCeoApiService
) {
    suspend fun fetchDogBreeds(): Result<DogBreedsResponseDto> {
        return try {
            val response = apiService.getDogBreeds()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Error en API Dog CEO: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Repositorio 3: Encargado de consultar la API 3 (Cat Facts API)
 */
class CatFactRepository(
    private val apiService: CatFactsApiService
) {
    suspend fun fetchCatFacts(): Result<CatFactsResponseDto> {
        return try {
            val response = apiService.getCatFacts()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Error en API Cat Facts: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
