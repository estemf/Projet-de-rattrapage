package org.diiage.projet_rattrapage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.diiage.projet_rattrapage.ui.screens.search.SearchScreen
import org.diiage.projet_rattrapage.ui.screens.details.DetailsScreen
import org.diiage.projet_rattrapage.ui.screens.details.DetailsType
import org.diiage.projet_rattrapage.ui.screens.details.DetailsViewModel
import org.diiage.projet_rattrapage.ui.components.*
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

/**
 * NavHost principal de l'application Deezer Music
 * 
 * Cette composable centralise toute la configuration de navigation
 * en appliquant les principes suivants :
 * 
 * - Configuration centralisée : toutes les routes en un seul endroit
 * - Type Safety : utilisation des destinations typées
 * - Injection de dépendances : ViewModels injectés via Hilt
 * - Logging : traçabilité complète de la navigation
 * 
 * Architecture respectée :
 * - Une seule Activity (comme demandé dans les règles)
 * - Navigation via NavigationManager
 * - Écrans nommés de manière claire
 * 
 * @param navController Contrôleur de navigation Compose
 * @param navigationManager Manager de navigation centralisé
 * @param modifier Modifier pour la customisation
 * 

 * @since 1.0
 */
@Composable
fun DeezerNavHost(
    navController: NavHostController,
    navigationManager: NavigationManager,
    modifier: Modifier = Modifier
) {
    
    // ================================
    // INITIALISATION DU NAVIGATION MANAGER
    // ================================
    
    LaunchedEffect(navController) {
        // Initialise le NavigationManager avec le NavController
        // Ceci permet au manager de surveiller et contrôler la navigation
        navigationManager.initialize(navController)
        Timber.i("🧭 NavHost initialisé avec NavigationManager")
    }
    
    // ================================
    // CONFIGURATION DU NAVHOST
    // ================================
    
    NavHost(
        navController = navController,
        startDestination = DeezerDestinations.startDestination.route,
        modifier = modifier
    ) {
        
        // ================================
        // ÉCRAN DE RECHERCHE (RACINE)
        // ================================
        
        composable(
            route = DeezerDestinations.SearchScreen.route
        ) {
            Timber.d("🎯 Navigation vers l'écran de recherche")
            
            // Écran de recherche réel avec ViewModel et NavigationManager
            SearchScreen(
                navigationManager = navigationManager
            )
        }
        
        // ================================
        // ÉCRAN DES RÉSULTATS DE RECHERCHE
        // ================================
        
        composable(
            route = DeezerDestinations.SearchResultsScreen.route
        ) { backStackEntry ->
            // Extraction du paramètre de recherche depuis les arguments
            val query = backStackEntry.arguments?.getString(
                DeezerDestinations.SearchResultsScreen.ARG_QUERY
            ) ?: ""
            
            Timber.d("🎯 Navigation vers les résultats pour: '$query'")
            
            // TODO: Créer SearchResultsScreen dédié
            // Pour l'instant, redirige vers SearchScreen avec query pré-remplie
            SearchScreen(
                navigationManager = navigationManager
            )
        }
        
        // ================================
        // ÉCRAN DES DÉTAILS D'ARTISTE
        // ================================
        
        composable(
            route = DeezerDestinations.ArtistDetailsScreen.route
        ) { backStackEntry ->
            // Extraction de l'ID artiste depuis les arguments
            val artistIdString = backStackEntry.arguments?.getString(
                DeezerDestinations.ArtistDetailsScreen.ARG_ARTIST_ID
            ) ?: "0"
            
            val artistId = artistIdString.toLongOrNull() ?: 0L
            
            Timber.d("🎯 Navigation vers les détails de l'artiste ID: $artistId")
            
            if (artistId <= 0) {
                Timber.w("⚠️ ID d'artiste invalide, retour à la recherche")
                LaunchedEffect(Unit) {
                    navigationManager.navigateToSearch()
                }
                return@composable
            }
            
            val detailsViewModel: DetailsViewModel = koinViewModel()
            
            DetailsScreen(
                detailsType = DetailsType.ARTIST,
                itemId = artistId,
                viewModel = detailsViewModel,
                navigationManager = navigationManager
            )
        }
        
        // ================================
        // ÉCRAN DES DÉTAILS D'ALBUM
        // ================================
        
        composable(
            route = DeezerDestinations.AlbumDetailsScreen.route
        ) { backStackEntry ->
            // Extraction de l'ID album depuis les arguments
            val albumIdString = backStackEntry.arguments?.getString(
                DeezerDestinations.AlbumDetailsScreen.ARG_ALBUM_ID
            ) ?: "0"
            
            val albumId = albumIdString.toLongOrNull() ?: 0L
            
            Timber.d("🎯 Navigation vers les détails de l'album ID: $albumId")
            
            if (albumId <= 0) {
                Timber.w("⚠️ ID d'album invalide, retour à la recherche")
                LaunchedEffect(Unit) {
                    navigationManager.navigateToSearch()
                }
                return@composable
            }
            
            val detailsViewModel: DetailsViewModel = koinViewModel()
            
            DetailsScreen(
                detailsType = DetailsType.ALBUM,
                itemId = albumId,
                viewModel = detailsViewModel,
                navigationManager = navigationManager
            )
        }
        
        // ================================
        // ÉCRAN DES DÉTAILS DE PISTE
        // ================================
        
        composable(
            route = DeezerDestinations.TrackDetailsScreen.route
        ) { backStackEntry ->
            // Extraction de l'ID piste depuis les arguments
            val trackIdString = backStackEntry.arguments?.getString(
                DeezerDestinations.TrackDetailsScreen.ARG_TRACK_ID
            ) ?: "0"
            
            val trackId = trackIdString.toLongOrNull() ?: 0L
            
            Timber.d("🎯 Navigation vers les détails de la piste ID: $trackId")
            
            if (trackId <= 0) {
                Timber.w("⚠️ ID de piste invalide, retour à la recherche")
                LaunchedEffect(Unit) {
                    navigationManager.navigateToSearch()
                }
                return@composable
            }
            
            val detailsViewModel: DetailsViewModel = koinViewModel()
            
            DetailsScreen(
                detailsType = DetailsType.TRACK,
                itemId = trackId,
                viewModel = detailsViewModel,
                navigationManager = navigationManager
            )
        }
        

        
        // ================================
        // ÉCRAN DES PARAMÈTRES
        // ================================
        
        composable(
            route = DeezerDestinations.SettingsScreen.route
        ) {
            Timber.d("🎯 Navigation vers les paramètres")
            
            SettingsScreen(navigationManager = navigationManager)
        }
    }
} 