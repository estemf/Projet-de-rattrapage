package org.diiage.projet_rattrapage.di

import org.diiage.projet_rattrapage.data.api.DeezerApiService
import org.diiage.projet_rattrapage.data.hardware.AudioManager
import org.diiage.projet_rattrapage.data.hardware.ConnectivityManager
import org.diiage.projet_rattrapage.data.repository.MusicRepositoryImpl
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import org.diiage.projet_rattrapage.domain.usecase.GetArtistDetailsUseCase
import org.diiage.projet_rattrapage.domain.usecase.SearchArtistsUseCase
import org.diiage.projet_rattrapage.domain.usecase.SearchAlbumsUseCase
import org.diiage.projet_rattrapage.domain.usecase.SearchTracksUseCase
import org.diiage.projet_rattrapage.ui.navigation.NavigationManager
import org.diiage.projet_rattrapage.ui.screens.search.SearchViewModel
import org.diiage.projet_rattrapage.ui.screens.details.DetailsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/**
 * Module Koin pour l'injection de dépendances
 * 
 * Remplace les modules Hilt par une configuration Koin plus simple
 * et sans conflit JavaPoet.
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */

val networkModule = module {
    
    // Configuration JSON pour Retrofit
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }
    
    // Client Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    // Service API Deezer
    single<DeezerApiService> {
        get<Retrofit>().create(DeezerApiService::class.java)
    }
}

val repositoryModule = module {
    
    // Repository de musique
    single<MusicRepository> {
        MusicRepositoryImpl(
            apiService = get(),
            audioManager = get(),
            connectivityManager = get()
        )
    }
}

val useCaseModule = module {
    
    // Use Cases
    single {
        SearchArtistsUseCase(repository = get())
    }
    
    single {
        SearchAlbumsUseCase(repository = get())
    }
    
    single {
        SearchTracksUseCase(repository = get())
    }
    
    single {
        GetArtistDetailsUseCase(repository = get())
    }
}

val hardwareModule = module {
    
    // Managers hardware
    single {
        AudioManager(androidContext())
    }
    
    single {
        ConnectivityManager(androidContext())
    }
}

val navigationModule = module {
    
    // Navigation Manager
    single {
        NavigationManager()
    }
}

val viewModelModule = module {
    
    // ViewModels
    viewModel {
        SearchViewModel(
            searchArtistsUseCase = get(),
            searchAlbumsUseCase = get(),
            searchTracksUseCase = get(),
            navigationManager = get(),
            connectivityManager = get(),
            audioManager = get()
        )
    }
    
    viewModel {
        DetailsViewModel(
            getArtistDetailsUseCase = get(),
            musicRepository = get()
        )
    }
} 