package org.diiage.projet_rattrapage.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.diiage.projet_rattrapage.ui.theme.ThemeManager
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.diiage.projet_rattrapage.R
import org.koin.androidx.compose.koinViewModel
import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track
import org.diiage.projet_rattrapage.ui.components.DeezerPrimaryButton
import org.diiage.projet_rattrapage.ui.components.DeezerSearchField
import org.diiage.projet_rattrapage.ui.components.DeezerTextButton

import org.diiage.projet_rattrapage.ui.navigation.NavigationManager
import org.diiage.projet_rattrapage.ui.theme.Projet_RattrapageTheme
import org.diiage.projet_rattrapage.utils.showToast
import org.diiage.projet_rattrapage.utils.shareText

/**
 * Écran de recherche d'artistes Deezer
 * 
 * Cette composable applique les règles du projet :
 * - "Découper les composables selon la philosophie des LEGOs"
 * - "Placer les composants au bon endroit dans l'architecture"
 * - "Chaque composant d'écran doit avoir un rôle spécifique sans logique étrangère"
 * - "Utiliser la programmation réactive via les StateFlow"
 * 
 * Architecture :
 * - Composable principal orchestrateur
 * - Sous-composables LEGO spécialisés
 * - Gestion d'état réactive via StateFlow
 * - Séparation claire des responsabilités
 * 
 * @param navigationManager Manager de navigation centralisé
 * @param viewModel ViewModel de recherche injecté par Hilt
 * 

 * @since 1.0
 */
@Composable
fun SearchScreen(
    navigationManager: NavigationManager,
    viewModel: SearchViewModel = koinViewModel()
) {
    
    // ================================
    // COLLECTE D'ÉTAT RÉACTIVE
    // ================================
    
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
    // ================================
    // GESTION DES ÉVÉNEMENTS
    // ================================
    
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SearchEvent.NavigateToArtistDetails -> {
                    navigationManager.navigateToArtistDetails(event.artistId)
                }
                is SearchEvent.NavigateToAlbumDetails -> {
                    navigationManager.navigateToAlbumDetails(event.albumId)
                }
                is SearchEvent.NavigateToTrackDetails -> {
                    navigationManager.navigateToTrackDetails(event.trackId)
                }

                is SearchEvent.ShowToast -> {
                    context.showToast(event.message)
                }
                is SearchEvent.HideKeyboard -> {
                    keyboardController?.hide()
                }
                is SearchEvent.ShareContent -> {
                    context.shareText(event.text, "Partager")
                }
                is SearchEvent.HapticFeedback -> {
                    // Feedback géré par le ViewModel via AudioManager
                }
            }
        }
    }
    
    // ================================
    // INTERFACE PRINCIPALE
    // ================================
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        // ================================
        // HEADER AVEC THÈME
        // ================================
        
        SearchHeaderSection(
            uiState = uiState,
            onThemeToggle = {
                viewModel.handleAction(SearchAction.ToggleTheme)
            }
        )
        
        // ================================
        // SÉLECTEUR DE TYPE DE RECHERCHE
        // ================================
        
        SearchTypeSelectorSection(
            selectedType = uiState.selectedSearchType,
            onTypeSelected = { type: SearchType ->
                viewModel.handleAction(SearchAction.ChangeSearchType(type))
            }
        )
        
        // ================================
        // CHAMP DE RECHERCHE PRINCIPAL
        // ================================
        
        DeezerSearchField(
            value = uiState.searchQuery,
            onValueChange = { query ->
                viewModel.handleAction(SearchAction.UpdateSearchQuery(query))
            },
            onSearch = { query ->
                viewModel.handleAction(SearchAction.PerformSearch(query))
            },
            isLoading = uiState.isLoading,
            error = uiState.error
        )
        
        
        // ================================
        // CONTENU CONDITIONNEL
        // ================================
        
        when {
            // État de chargement
            uiState.isLoading -> {
                SearchLoadingSection()
            }
            
            // Affichage des résultats selon le type sélectionné
            uiState.hasResults -> {
                when (uiState.selectedSearchType) {
                    SearchType.ARTISTS -> {
                        SearchResultsSection(
                            artists = uiState.searchResults,
                            onArtistClick = { artist ->
                                viewModel.handleAction(SearchAction.SelectArtist(artist))
                            }
                        )
                    }
                    SearchType.ALBUMS -> {
                        AlbumResultsSection(
                            albums = uiState.albumResults,
                            onAlbumClick = { album ->
                                viewModel.handleAction(SearchAction.SelectAlbum(album))
                            }
                        )
                    }
                    SearchType.TRACKS -> {
                        TrackResultsSection(
                            tracks = uiState.trackResults,
                            onTrackClick = { track ->
                                viewModel.handleAction(SearchAction.SelectTrack(track))
                            }
                        )
                    }
                }
            }
            
            // Affichage de l'historique
            uiState.showSearchHistory -> {
                SearchHistorySection(
                    searchHistory = uiState.searchHistory,
                    onHistoryItemClick = { query ->
                        viewModel.handleAction(SearchAction.SelectHistoryItem(query))
                    },
                    onClearHistory = {
                        viewModel.handleAction(SearchAction.ClearSearchHistory)
                    }
                )
            }
            
            // État vide initial
            uiState.showEmptyState -> {
                SearchEmptyStateSection()
            }
            
            // Message de statut
            uiState.statusMessage != null -> {
                SearchStatusSection(
                    message = uiState.statusMessage!!,
                    canRetry = uiState.error != null,
                    onRetry = {
                        viewModel.handleAction(SearchAction.RetryLastSearch)
                    }
                )
            }
        }
    }
}

// ================================
// COMPOSABLES LEGO SPÉCIALISÉS
// ================================

/**
 * Section d'en-tête avec informations de thème
 * 
 * Composable LEGO focalisé sur l'affichage des informations
 * de thème et de navigation
 */
@Composable
private fun SearchHeaderSection(
    uiState: SearchUiState,
    onThemeToggle: () -> Unit
) {
    val isDarkTheme by ThemeManager.isDarkTheme.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Text(
            text = "Recherche Deezer",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bouton de changement de thème avec icône lune/soleil
            IconButton(onClick = onThemeToggle) {
                Icon(
                    painter = painterResource(
                        id = if (isDarkTheme) {
                            R.drawable.sun_icon // Soleil pour passer au mode clair
                        } else {
                            R.drawable.moon_icon // Lune pour passer au mode sombre
                        }
                    ),
                    contentDescription = if (isDarkTheme) {
                        "Passer au mode clair"
                    } else {
                        "Passer au mode sombre"
                    },
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            

        }
    }
}

/**
 * Section de chargement avec indicateur
 * 
 * Composable LEGO pour l'état de chargement
 */
@Composable
private fun SearchLoadingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Recherche en cours...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Section d'affichage des résultats de recherche
 * 
 * Composable LEGO pour la liste des artistes trouvés
 */
@Composable
private fun SearchResultsSection(
    artists: List<Artist>,
    onArtistClick: (Artist) -> Unit
) {
    Column {
        Text(
            text = "${artists.size} artiste(s) trouvé(s)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(artists) { artist ->
                ArtistResultCard(
                    artist = artist,
                    onClick = { onArtistClick(artist) }
                )
            }
        }
    }
}

/**
 * Carte d'affichage d'un artiste dans les résultats
 * 
 * Composable LEGO réutilisable pour un artiste
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistResultCard(
    artist: Artist,
    onClick: () -> Unit
) {
    Card(
        onClick = {
            println("🎯 DEBUG: Clic sur carte artiste ${artist.name} (ID: ${artist.id})")
            onClick()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (artist.numberOfFans > 0) {
                    Text(
                        text = artist.getFormattedFansCount(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            
            // Icône de partage retirée
        }
    }
}

/**
 * Section d'historique de recherche
 * 
 * Composable LEGO pour l'affichage de l'historique
 */
@Composable
private fun SearchHistorySection(
    searchHistory: List<String>,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recherches récentes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            DeezerTextButton(
                onClick = onClearHistory,
                icon = Icons.Filled.Clear
            ) {
                Text("Effacer")
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(searchHistory) { query ->
                HistoryItemCard(
                    query = query,
                    onClick = { onHistoryItemClick(query) }
                )
            }
        }
    }
}

/**
 * Carte d'élément d'historique
 * 
 * Composable LEGO pour un élément d'historique
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryItemCard(
    query: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Historique de recherche",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = query,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Section d'état vide initial
 * 
 * Composable LEGO pour l'état vide
 */
@Composable
private fun SearchEmptyStateSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Text(
                text = "Recherchez votre artiste préféré",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Saisissez au moins 2 caractères pour commencer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Section de message de statut avec retry
 * 
 * Composable LEGO pour les messages d'état
 */
@Composable
private fun SearchStatusSection(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            if (canRetry) {
                DeezerPrimaryButton(
                    onClick = onRetry,
                    icon = Icons.Filled.Refresh
                ) {
                    Text("Réessayer")
                }
            }
        }
    }
}

// ================================
// PREVIEWS POUR LE DESIGN SYSTEM
// ================================

/**
 * Section d'affichage des résultats d'albums
 * 
 * Composable LEGO pour la liste des albums trouvés
 */
@Composable
private fun AlbumResultsSection(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit
) {
    Column {
        Text(
            text = "${albums.size} album(s) trouvé(s)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(albums) { album ->
                AlbumResultCard(
                    album = album,
                    onClick = { onAlbumClick(album) }
                )
            }
        }
    }
}

/**
 * Carte d'affichage d'un album dans les résultats
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumResultCard(
    album: Album,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = album.getArtistName(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                
                if (album.numberOfTracks > 0) {
                    Text(
                        text = "${album.numberOfTracks} piste(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * Section d'affichage des résultats de pistes
 * 
 * Composable LEGO pour la liste des pistes trouvées
 */
@Composable
private fun TrackResultsSection(
    tracks: List<Track>,
    onTrackClick: (Track) -> Unit
) {
    Column {
        Text(
            text = "${tracks.size} piste(s) trouvée(s)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tracks) { track ->
                TrackResultCard(
                    track = track,
                    onClick = { onTrackClick(track) }
                )
            }
        }
    }
}

/**
 * Carte d'affichage d'une piste dans les résultats
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackResultCard(
    track: Track,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "${track.getArtistName()} - ${track.getAlbumTitle()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
                
                Text(
                    text = track.getFormattedDuration(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Section de sélection du type de recherche
 * 
 * Composable LEGO pour choisir entre artistes, albums et pistes
 */
@Composable
private fun SearchTypeSelectorSection(
    selectedType: SearchType,
    onTypeSelected: (SearchType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SearchType.values().forEach { type ->
            FilterChip(
                selected = type == selectedType,
                onClick = { onTypeSelected(type) },
                label = { 
                    Text(
                        text = type.pluralName,
                        style = MaterialTheme.typography.labelMedium
                    ) 
                },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(name = "Écran de recherche", showBackground = true)
@Composable
private fun SearchScreenPreview() {
    Projet_RattrapageTheme(darkTheme = false) {
        // Preview avec état simulé
        Box(modifier = Modifier.fillMaxSize()) {
            SearchEmptyStateSection()
        }
    }
} 