package com.example.examentercerparcialdavidsilva.data.mapper

import com.example.examentercerparcialdavidsilva.data.dto.cat.CatFactDto
import com.example.examentercerparcialdavidsilva.data.dto.cat.CatFactsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.dog.DogBreedsResponseDto
import com.example.examentercerparcialdavidsilva.data.dto.weather.WeatherResponseDto
import com.example.examentercerparcialdavidsilva.data.local.FaunaFactDataEntity
import com.example.examentercerparcialdavidsilva.data.local.NatureWeatherDataEntity
import java.util.Locale

/**
 * Mapper 1: Combina datos de API 1 (Open-Meteo Weather) y API 2 (Dog CEO Breeds)
 * para mapearlos a la entidad genérica Tabla 1: NatureWeatherDataEntity.
 */
object NatureWeatherMapper {
    fun mapToEntity(
        weatherDto: WeatherResponseDto?,
        dogBreedsDto: DogBreedsResponseDto?
    ): NatureWeatherDataEntity {
        val current = weatherDto?.current
        val breedsMap = dogBreedsDto?.message ?: emptyMap()

        val temp = current?.temperature2m ?: 0.0
        val humidity = current?.relativeHumidity2m ?: 0
        val windSpeed = current?.windSpeed10m ?: 0.0
        val timezone = weatherDto?.timezone ?: "America/La_Paz"

        val totalBreeds = breedsMap.size
        val sampleKey = breedsMap.keys.firstOrNull() ?: "Ninguna"
        val sampleBreed = if (sampleKey != "Ninguna") sampleKey.replaceFirstChar { it.uppercase(Locale.ROOT) } else "Ninguna"

        return NatureWeatherDataEntity(
            temperature = temp,
            humidity = humidity,
            windSpeed = windSpeed,
            timezone = timezone,
            totalDogBreeds = totalBreeds,
            sampleBreedName = sampleBreed
        )
    }
}

/**
 * Mapper 2: Combina datos de API 2 (Dog CEO Breeds) y API 3 (Cat Facts)
 * para mapearlos a la lista de entidades genéricas Tabla 2: FaunaFactDataEntity.
 */
object FaunaFactMapper {
    fun mapToEntities(
        dogBreedsDto: DogBreedsResponseDto?,
        catFactsDto: CatFactsResponseDto?
    ): List<FaunaFactDataEntity> {
        val dogBreedsList: List<Map.Entry<String, List<String>>> = dogBreedsDto?.message?.entries?.toList() ?: emptyList()
        val catFactsList: List<CatFactDto> = catFactsDto?.data ?: emptyList()

        if (catFactsList.isEmpty() && dogBreedsList.isEmpty()) {
            return emptyList()
        }

        val resultList = mutableListOf<FaunaFactDataEntity>()
        val maxCount = maxOf(dogBreedsList.size, catFactsList.size).coerceAtMost(10)

        for (i in 0 until maxCount) {
            val dogEntry = if (dogBreedsList.isNotEmpty()) dogBreedsList[i % dogBreedsList.size] else null
            val catFact = if (catFactsList.isNotEmpty()) catFactsList[i % catFactsList.size] else null

            val breedName = dogEntry?.key?.replaceFirstChar { it.uppercase(Locale.ROOT) } ?: "Sin datos de perro"
            val subBreeds = dogEntry?.value?.joinToString(", ") { sub -> sub.replaceFirstChar { it.uppercase(Locale.ROOT) } }
                ?.ifEmpty { "Sin subrazas" } ?: "Sin subrazas"

            val factText = catFact?.fact ?: "Sin dato curioso de gato"
            val factLen = catFact?.length ?: factText.length

            resultList.add(
                FaunaFactDataEntity(
                    dogBreedName = breedName,
                    subBreedsInfo = subBreeds,
                    catFactText = factText,
                    catFactLength = factLen
                )
            )
        }

        return resultList
    }
}
