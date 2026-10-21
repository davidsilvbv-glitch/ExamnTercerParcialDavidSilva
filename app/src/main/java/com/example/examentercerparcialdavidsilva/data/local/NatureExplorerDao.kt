package com.example.examentercerparcialdavidsilva.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NatureExplorerDao {

    // Operaciones Tabla 1 (NatureWeatherDataEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNatureWeather(entity: NatureWeatherDataEntity)

    @Query("SELECT * FROM nature_weather_table ORDER BY id DESC")
    fun getAllNatureWeather(): Flow<List<NatureWeatherDataEntity>>

    @Query("SELECT * FROM nature_weather_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestNatureWeather(): NatureWeatherDataEntity?

    @Query("DELETE FROM nature_weather_table")
    suspend fun clearNatureWeather()

    // Operaciones Tabla 2 (FaunaFactDataEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaunaFacts(entities: List<FaunaFactDataEntity>)

    @Query("SELECT * FROM fauna_fact_table ORDER BY id DESC")
    fun getAllFaunaFacts(): Flow<List<FaunaFactDataEntity>>

    @Query("DELETE FROM fauna_fact_table")
    suspend fun clearFaunaFacts()
}
