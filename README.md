# Examen Tercer Parcial - Grupo 2: Explorador de Naturaleza y Clima

Estudiante: David Silva

## Diagrama de Clases

```mermaid
classDiagram
    class WeatherResponseDto {
        +Double latitude
        +Double longitude
        +String timezone
        +CurrentWeatherDto current
    }

    class CurrentWeatherDto {
        +Double temperature2m
        +Int relativeHumidity2m
        +Double windSpeed10m
    }

    class DogBreedsResponseDto {
        +Map message
        +String status
    }

    class CatFactsResponseDto {
        +List data
        +Int total
    }

    class CatFactDto {
        +String fact
        +Int length
    }

    class NatureWeatherMapper {
        +mapToEntity(WeatherResponseDto, DogBreedsResponseDto) NatureWeatherDataEntity
    }

    class FaunaFactMapper {
        +mapToEntities(DogBreedsResponseDto, CatFactsResponseDto) List
    }

    class NatureWeatherDataEntity {
        +Int id
        +Double temperature
        +Int humidity
        +Double windSpeed
        +String timezone
        +Int totalDogBreeds
        +String sampleBreedName
        +Long timestamp
    }

    class FaunaFactDataEntity {
        +Int id
        +String dogBreedName
        +String subBreedsInfo
        +String catFactText
        +Int catFactLength
        +Long timestamp
    }

    class NatureExplorerDao {
        +insertNatureWeather(NatureWeatherDataEntity)
        +getAllNatureWeather() Flow
        +insertFaunaFacts(List)
        +getAllFaunaFacts() Flow
    }

    class NatureExplorerDatabase {
        +natureExplorerDao() NatureExplorerDao
    }

    class OpenMeteoRepository {
        +fetchWeatherForecast() Result
    }

    class DogBreedRepository {
        +fetchDogBreeds() Result
    }

    class CatFactRepository {
        +fetchCatFacts() Result
    }

    class NatureExplorerCoordinatorRepository {
        +fetchAndStoreAllData() Result
        +getNatureWeatherLocal() Flow
        +getFaunaFactsLocal() Flow
    }

    class NatureExplorerViewModel {
        +StateFlow uiState
        +refreshDataFromNetwork()
        +clearDatabase()
    }

    class NatureExplorerScreen {
        +Composable UI
    }

    WeatherResponseDto --> NatureWeatherMapper
    DogBreedsResponseDto --> NatureWeatherMapper
    NatureWeatherMapper --> NatureWeatherDataEntity

    DogBreedsResponseDto --> FaunaFactMapper
    CatFactsResponseDto --> FaunaFactMapper
    FaunaFactMapper --> FaunaFactDataEntity

    NatureExplorerDatabase --> NatureExplorerDao
    NatureExplorerDao --> NatureWeatherDataEntity
    NatureExplorerDao --> FaunaFactDataEntity

    NatureExplorerCoordinatorRepository --> OpenMeteoRepository
    NatureExplorerCoordinatorRepository --> DogBreedRepository
    NatureExplorerCoordinatorRepository --> CatFactRepository
    NatureExplorerCoordinatorRepository --> NatureExplorerDao

    NatureExplorerViewModel --> NatureExplorerCoordinatorRepository
    NatureExplorerScreen --> NatureExplorerViewModel
```

## Ubicación de las Clases en el Proyecto

- DTOs de Red: `app/src/main/java/com/example/examentercerparcialdavidsilva/data/dto/`
- Entidades y DAO Room: `app/src/main/java/com/example/examentercerparcialdavidsilva/data/local/`
- Mappers: `app/src/main/java/com/example/examentercerparcialdavidsilva/data/mapper/`
- Servicios y Repositorios API: `app/src/main/java/com/example/examentercerparcialdavidsilva/data/api/` y `data/repository/`
- ViewModel y UI Compose: `app/src/main/java/com/example/examentercerparcialdavidsilva/ui/`
