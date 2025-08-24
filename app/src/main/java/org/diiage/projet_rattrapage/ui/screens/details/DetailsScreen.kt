package org.diiage.projet_rattrapage.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star

import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.diiage.projet_rattrapage.domain.model.*
import org.diiage.projet_rattrapage.ui.components.DeezerPrimaryButton
import org.diiage.projet_rattrapage.ui.navigation.NavigationManager
import org.diiage.projet_rattrapage.ui.components.AudioPreviewPlayer
import org.diiage.projet_rattrapage.data.hardware.AudioPlayer
import org.diiage.projet_rattrapage.ui.theme.deezerPurple
import org.diiage.projet_rattrapage.ui.theme.Projet_RattrapageTheme

/**
 * Écran de détails unifiés pour Artiste, Album ou Titre
 * 
 * Cette screen applique les principes de l'architecture MVVM+ UDF :
 * - Séparation des responsabilités UI/Business Logic
 * - Composants LEGO réutilisables
 * - Gestion d'état centralisée
 * - Navigation découplée
 * 
 * Compétences mises en œuvre :
 * - CRIT-DMA-D1-SF-3 : Architecture UI avec Jetpack Compose
 * - DI1-GLO-C4-2 : Application des concepts POO
 * - DI1-GLO-C2-3 : Respect des bonnes pratiques
 * 
 * @param detailsType Type d'élément à afficher (Artiste, Album, Titre)
 * @param itemId Identifiant de l'élément
 * @param viewModel ViewModel gérant la logique business
 * @param navigationManager Manager centralisé de navigation
 * 

 * @since 1.0
 */
@Composable
fun DetailsScreen(
    detailsType: DetailsType,
    itemId: Long,
    viewModel: DetailsViewModel,
    navigationManager: NavigationManager
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Chargement des données au premier affichage
    LaunchedEffect(detailsType, itemId) {
        viewModel.loadDetails(detailsType, itemId)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when {
            uiState.isLoading -> {
                LoadingContent()
            }
            
            uiState.error != null -> {
                ErrorContent(
                    error = uiState.error!!,
                    onRetry = { viewModel.loadDetails(detailsType, itemId) },
                    onBack = { navigationManager.navigateBack() }
                )
            }
            
            else -> {
                DetailsContent(
                    uiState = uiState,
                    detailsType = detailsType,
                    onBack = { navigationManager.navigateBack() },
                    onRelatedItemClick = { type, id -> 
                        // Navigation vers un élément lié (artiste d'un album, etc.)
                        when (type) {
                            DetailsType.ARTIST -> navigationManager.navigateToArtistDetails(id)
                            DetailsType.ALBUM -> navigationManager.navigateToAlbumDetails(id)
                            DetailsType.TRACK -> navigationManager.navigateToTrackDetails(id)
                        }
                    },
                    audioPlayer = viewModel.audioPlayerInstance
                )
            }
        }
    }
}

/**
 * Contenu principal des détails
 */
@Composable
private fun DetailsContent(
    uiState: DetailsUiState,
    detailsType: DetailsType,
    onBack: () -> Unit,
    onRelatedItemClick: (DetailsType, Long) -> Unit,
    audioPlayer: AudioPlayer
) {
            LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Header avec bouton retour
            DetailsHeader(
                title = when (detailsType) {
                    DetailsType.ARTIST -> "Détails de l'artiste"
                    DetailsType.ALBUM -> "Détails de l'album"
                    DetailsType.TRACK -> "Détails de la piste"
                },
                onBack = onBack
            )
        }
        

        
        item {
            when (detailsType) {
                DetailsType.ARTIST -> uiState.artist?.let { artist ->
                    ArtistDetailsCard(artist = artist)
                }
                
                DetailsType.ALBUM -> uiState.album?.let { album ->
                    AlbumDetailsCard(
                        album = album,
                        onArtistClick = { artistId: Long ->
                            onRelatedItemClick(DetailsType.ARTIST, artistId)
                        }
                    )
                }
                
                DetailsType.TRACK -> uiState.track?.let { track ->
                    TrackDetailsCard(
                        track = track,
                        onArtistClick = { artistId: Long ->
                            onRelatedItemClick(DetailsType.ARTIST, artistId)
                        },
                        onAlbumClick = { albumId: Long ->
                            onRelatedItemClick(DetailsType.ALBUM, albumId)
                        },
                        audioPlayer = audioPlayer
                    )
                }
            }
        }
    }
}

/**
 * Header avec titre et bouton retour
 */
@Composable
private fun DetailsHeader(
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Bouton retour aligné à gauche
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // Titre centré
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Carte de détails d'un artiste
 * 
 * Applique le principe de composition des composables LEGO
 */
@Composable
private fun ArtistDetailsCard(
    artist: Artist
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Image de l'artiste
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artist.getImageUrl(ImageSize.BIG))
                    .crossfade(true)
                    .build(),
                contentDescription = "Photo de ${artist.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Nom de l'artiste
            Text(
                text = artist.name,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Statistiques - compactes
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                StatisticItem(
                    icon = Icons.Filled.Info,
                    label = "Albums",
                    value = artist.numberOfAlbums.toString()
                )
                
                StatisticItem(
                    icon = Icons.Filled.Favorite,
                    label = "Fans",
                    value = artist.getFormattedFansCount()
                )
            }
        }
    }
}

/**
 * Carte de détails d'un album
 * 
 * Respecte l'architecture en couches et la séparation des préoccupations
 */
@Composable
private fun AlbumDetailsCard(
    album: Album,
    onArtistClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Couverture de l'album
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.getCoverUrl(ImageSize.BIG))
                    .crossfade(true)
                    .build(),
                contentDescription = "Couverture de ${album.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Titre de l'album
            Text(
                text = album.title,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Nom de l'artiste cliquable - optimisé
            album.artist?.let { artist ->
                TextButton(
                    onClick = { onArtistClick(artist.id) },
                    modifier = Modifier.padding(vertical = 0.dp)
                ) {
                    Text(
                        text = artist.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.deezerPurple,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Informations de l'album - compactes
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                StatisticItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    label = "Pistes",
                    value = album.numberOfTracks.toString()
                )
                
                StatisticItem(
                    icon = Icons.Filled.Star,
                    label = "Durée",
                    value = album.getFormattedDuration()
                )
            }
            
            // Date de sortie - compacte
            if (album.releaseDate.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.deezerPurple,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Sortie : ${album.getFormattedReleaseDate()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Carte de détails d'une piste
 * 
 * Démontre l'utilisation des principes SOLID et POO
 */
@Composable
private fun TrackDetailsCard(
    track: Track,
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    audioPlayer: AudioPlayer
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Couverture de l'album (si disponible)
            if (track.album != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(track.getCoverUrl(ImageSize.BIG))
                        .crossfade(true)
                        .build(),
                    contentDescription = "Couverture de ${track.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Titre de la piste
            Text(
                text = track.title,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Liens vers artiste et album - resserrés et plus gros
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                track.artist?.let { artist ->
                    TextButton(
                        onClick = { onArtistClick(artist.id) },
                        modifier = Modifier.padding(vertical = 0.dp)
                    ) {
                        Text(
                            text = artist.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.deezerPurple,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                track.album?.let { album ->
                    TextButton(
                        onClick = { onAlbumClick(album.id) },
                        modifier = Modifier.padding(vertical = 0.dp)
                    ) {
                        Text(
                            text = album.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.deezerPurple,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // Informations de la piste - compactes
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                StatisticItem(
                    icon = Icons.Filled.Star,
                    label = "Durée",
                    value = track.getFormattedDuration()
                )
                
                if (track.trackPosition > 0) {
                    StatisticItem(
                        icon = Icons.Filled.PlayArrow,
                        label = "Piste",
                        value = track.trackPosition.toString()
                    )
                }
            }
            
            // Lecteur audio pour l'extrait (si disponible)
            if (track.hasPreview()) {
                Spacer(modifier = Modifier.height(8.dp))
                AudioPreviewPlayer(
                    audioPlayer = audioPlayer,
                    previewUrl = track.preview,
                    trackTitle = track.title,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Date de sortie de l'album - compacte
            track.album?.let { album ->
                if (album.releaseDate.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.deezerPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Sortie : ${album.getFormattedReleaseDate()}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Composant réutilisable pour afficher une statistique
 * 
 * Applique la philosophie LEGO des composables
 */
@Composable
private fun StatisticItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.deezerPurple,
            modifier = Modifier.size(32.dp)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Contenu de chargement avec indicateur centré
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.deezerPurple,
                strokeWidth = 3.dp
            )
            
            Text(
                text = "Chargement des détails...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Contenu d'erreur avec possibilité de réessayer
 */
@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            
            Text(
                text = "Erreur de chargement",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("Retour")
                }
                
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
// ÉNUMÉRATIONS
// ================================

/**
 * Types d'éléments pouvant être affichés en détail
 */
enum class DetailsType {
    ARTIST,
    ALBUM,
    TRACK
}

// ================================
// PREVIEWS
// ================================

@Preview(showBackground = true)
@Composable
private fun ArtistDetailsCardPreview() {
    Projet_RattrapageTheme {
        ArtistDetailsCard(
            artist = Artist(
                id = 1,
                name = "David Bowie",
                numberOfAlbums = 27,
                numberOfFans = 1500000
            )
        )
    }
}
