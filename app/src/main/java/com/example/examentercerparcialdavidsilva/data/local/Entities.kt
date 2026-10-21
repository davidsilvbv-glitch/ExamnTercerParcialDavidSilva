package com.example.examentercerparcialdavidsilva.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla 1 - Entidad combinada 1 (Clima de API 1 + Datos de Perros de API 2)
 */
@Entity(tableName = "nature_weather_table")
data class NatureWeatherDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val timezone: String,
    val totalDogBreeds: Int,
    val sampleBreedName: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Tabla 2 - Entidad combinada 2 (Razas de Perros de API 2 + Facts de Gatos de API 3)
 */
@Entity(tableName = "fauna_fact_table")
data class FaunaFactDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dogBreedName: String,
    val subBreedsInfo: String,
    val catFactText: String,
    val catFactLength: Int,
    val timestamp: Long = System.currentTimeMillis()
)
