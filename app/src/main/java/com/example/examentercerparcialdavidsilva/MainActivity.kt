package com.example.examentercerparcialdavidsilva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.examentercerparcialdavidsilva.data.api.RetrofitClient
import com.example.examentercerparcialdavidsilva.data.local.NatureExplorerDatabase
import com.example.examentercerparcialdavidsilva.data.repository.CatFactRepository
import com.example.examentercerparcialdavidsilva.data.repository.DogBreedRepository
import com.example.examentercerparcialdavidsilva.data.repository.NatureExplorerCoordinatorRepository
import com.example.examentercerparcialdavidsilva.data.repository.OpenMeteoRepository
import com.example.examentercerparcialdavidsilva.ui.screen.NatureExplorerScreen
import com.example.examentercerparcialdavidsilva.ui.theme.ExamenTercerParcialDavidSilvaTheme
import com.example.examentercerparcialdavidsilva.ui.viewmodel.NatureExplorerViewModel
import com.example.examentercerparcialdavidsilva.ui.viewmodel.NatureExplorerViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = NatureExplorerDatabase.getInstance(this)
        val dao = database.natureExplorerDao()

        val openMeteoRepository = OpenMeteoRepository(RetrofitClient.openMeteoApiService)
        val dogBreedRepository = DogBreedRepository(RetrofitClient.dogCeoApiService)
        val catFactRepository = CatFactRepository(RetrofitClient.catFactsApiService)

        val coordinatorRepository = NatureExplorerCoordinatorRepository(
            openMeteoRepository = openMeteoRepository,
            dogBreedRepository = dogBreedRepository,
            catFactRepository = catFactRepository,
            dao = dao
        )

        val factory = NatureExplorerViewModelFactory(coordinatorRepository)
        val viewModel = ViewModelProvider(this, factory)[NatureExplorerViewModel::class.java]

        setContent {
            ExamenTercerParcialDavidSilvaTheme {
                NatureExplorerScreen(viewModel = viewModel)
            }
        }
    }
}
