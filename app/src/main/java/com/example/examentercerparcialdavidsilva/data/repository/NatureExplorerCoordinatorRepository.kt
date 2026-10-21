package com.example.examentercerparcialdavidsilva.data.repository

import com.example.examentercerparcialdavidsilva.data.local.FaunaFactDataEntity
import com.example.examentercerparcialdavidsilva.data.local.NatureExplorerDao
import com.example.examentercerparcialdavidsilva.data.local.NatureWeatherDataEntity
import com.example.examentercerparcialdavidsilva.data.mapper.FaunaFactMapper
import com.example.examentercerparcialdavidsilva.data.mapper.NatureWeatherMapper
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio Coordinador:
 * Coordina las consultas a los 3 repositorios de API independientes,
 * ejecuta los 2 mappers y guarda los resultados en las 2 tablas de Room DB.
 */
class NatureExplorerCoordinatorRepository(
    private val openMeteoRepository: OpenMeteoRepository,
    private val dogBreedRepository: DogBreedRepository,
    private val catFactRepository: CatFactRepository,
    private val dao: NatureExplorerDao
) {

    /**
     * Consulta las 3 APIs, aplica los mappers y persiste en Room DB.
     */
    suspend fun fetchAndStoreAllData(): Result<Unit> {
        return try {
            val weatherResult = openMeteoRepository.fetchWeatherForecast()
            val dogResult = dogBreedRepository.fetchDogBreeds()
            val catResult = catFactRepository.fetchCatFacts()

            val weatherDto = weatherResult.getOrNull()
            val dogDto = dogResult.getOrNull()
            val catDto = catResult.getOrNull()

            if (weatherDto == null && dogDto == null && catDto == null) {
                val errorMsg = listOfNotNull(
                    weatherResult.exceptionOrNull()?.message,
                    dogResult.exceptionOrNull()?.message,
                    catResult.exceptionOrNull()?.message
                ).joinToString(" | ")
                return Result.failure(Exception("Error al consultar las APIs: $errorMsg"))
            }

            // Mapper 1: API 1 (Weather) + API 2 (Dog CEO) -> Tabla 1 (NatureWeatherDataEntity)
            if (weatherDto != null || dogDto != null) {
                val natureWeatherEntity = NatureWeatherMapper.mapToEntity(weatherDto, dogDto)
                dao.insertNatureWeather(natureWeatherEntity)
            }

            // Mapper 2: API 2 (Dog CEO) + API 3 (Cat Facts) -> Tabla 2 (FaunaFactDataEntity)
            if (dogDto != null || catDto != null) {
                val faunaFactEntities = FaunaFactMapper.mapToEntities(dogDto, catDto)
                if (faunaFactEntities.isNotEmpty()) {
                    dao.insertFaunaFacts(faunaFactEntities)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getNatureWeatherLocal(): Flow<List<NatureWeatherDataEntity>> {
        return dao.getAllNatureWeather()
    }

    fun getFaunaFactsLocal(): Flow<List<FaunaFactDataEntity>> {
        return dao.getAllFaunaFacts()
    }

    suspend fun clearAllData() {
        dao.clearNatureWeather()
        dao.clearFaunaFacts()
    }
}
