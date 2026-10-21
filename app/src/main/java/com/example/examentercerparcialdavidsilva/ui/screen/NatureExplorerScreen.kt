package com.example.examentercerparcialdavidsilva.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.examentercerparcialdavidsilva.data.local.FaunaFactDataEntity
import com.example.examentercerparcialdavidsilva.data.local.NatureWeatherDataEntity
import com.example.examentercerparcialdavidsilva.ui.viewmodel.NatureExplorerViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NatureExplorerScreen(
    viewModel: NatureExplorerViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = listOf("Clima & Razas (T1)", "Fauna & Facts (T2)", "Estado Room DB")

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { errorMsg ->
            snackbarHostState.showSnackbar(errorMsg)
            viewModel.dismissError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Explorador de Naturaleza",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Button(
                        onClick = { viewModel.refreshDataFromNetwork() },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text("Actualizar")
                    }
                    OutlinedButton(
                        onClick = { viewModel.clearDatabase() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Borrar DB")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }

            if (uiState.isLoading && uiState.weatherList.isEmpty() && uiState.faunaFactsList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTabIndex) {
                    0 -> WeatherDogSection(weatherList = uiState.weatherList)
                    1 -> FaunaFactsSection(faunaFactsList = uiState.faunaFactsList)
                    2 -> DatabaseStatusSection(
                        weatherCount = uiState.weatherList.size,
                        faunaCount = uiState.faunaFactsList.size,
                        latestWeather = uiState.weatherList.firstOrNull()
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherDogSection(weatherList: List<NatureWeatherDataEntity>) {
    if (weatherList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No hay datos en la Tabla 1 (Clima & Razas)")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(weatherList) { item ->
                WeatherDogCard(item)
            }
        }
    }
}

@Composable
fun WeatherDogCard(data: NatureWeatherDataEntity) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()) }
    val formattedDate = dateFormat.format(Date(data.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Clima La Paz & Fauna Canina",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Temperatura: ${data.temperature} °C", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Humedad Relativa: ${data.humidity}%", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Velocidad del Viento: ${data.windSpeed} km/h", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Zona Horaria: ${data.timezone}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Total de Razas de Perro: ${data.totalDogBreeds}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Raza Muestra: ${data.sampleBreedName}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Guardado: $formattedDate", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun FaunaFactsSection(faunaFactsList: List<FaunaFactDataEntity>) {
    if (faunaFactsList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No hay datos en la Tabla 2 (Fauna & Facts)")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(faunaFactsList) { item ->
                FaunaFactCard(item)
            }
        }
    }
}

@Composable
fun FaunaFactCard(data: FaunaFactDataEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Perro: ${data.dogBreedName}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Subrazas: ${data.subBreedsInfo}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dato Curioso Gato:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "\"${data.catFactText}\"",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Longitud del texto: ${data.catFactLength} caracteres",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun DatabaseStatusSection(
    weatherCount: Int,
    faunaCount: Int,
    latestWeather: NatureWeatherDataEntity?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Estado de Base de Datos Room",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Registros en Tabla 1 (NatureWeather): $weatherCount")
                Text(text = "Registros en Tabla 2 (FaunaFacts): $faunaCount")
            }
        }

        latestWeather?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Última Actualización Registrada:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Temperatura: ${it.temperature} °C")
                    Text(text = "Zona: ${it.timezone}")
                    Text(text = "Razas detectadas: ${it.totalDogBreeds}")
                }
            }
        }
    }
}
