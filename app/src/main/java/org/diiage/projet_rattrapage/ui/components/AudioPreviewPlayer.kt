package org.diiage.projet_rattrapage.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.diiage.projet_rattrapage.R
import org.diiage.projet_rattrapage.data.hardware.AudioPlayer
import org.diiage.projet_rattrapage.data.hardware.PlaybackState
import org.diiage.projet_rattrapage.ui.theme.deezerPurple
import java.util.Locale

/**
 * Lecteur d'extraits audio pour les pistes Deezer
 * 
 * Ce composant applique les principes de l'architecture MVVM+ :
 * - Réactivité via StateFlow et collectAsState
 * - Séparation des préoccupations (UI vs logique)
 * - Composant LEGO réutilisable et modulaire
 * 
 * Design patterns appliqués :
 * - Observer Pattern : surveillance des états de lecture
 * - State Pattern : gestion des différents états UI
 * - Strategy Pattern : comportements différents selon l'état
 * 
 * @param audioPlayer Instance du lecteur audio injectée
 * @param previewUrl URL de l'extrait à lire
 * @param trackTitle Titre de la piste pour l'accessibilité
 * @param modifier Modifier pour la customisation
 * 

 * @since 1.0
 */
@Composable
fun AudioPreviewPlayer(
    audioPlayer: AudioPlayer,
    previewUrl: String,
    trackTitle: String,
    modifier: Modifier = Modifier
) {
    // ================================
    // ÉTATS RÉACTIFS
    // ================================
    
    val playbackState by audioPlayer.playbackState.collectAsState()
    val currentPreviewUrl by audioPlayer.currentPreviewUrl.collectAsState()
    val playbackProgress by audioPlayer.playbackProgress.collectAsState()
    
    // État local pour l'interface
    var isExpanded by remember { mutableStateOf(false) }
    
    // Vérification si cet extrait est en cours de lecture OU en pause
    val isCurrentlyPlaying = currentPreviewUrl == previewUrl && 
                            (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.PAUSED)
    
    // Vérification si la lecture est en pause
    val isPaused = currentPreviewUrl == previewUrl && playbackState == PlaybackState.PAUSED
    
    // ================================
    // ANIMATIONS
    // ================================
    
    val progressAnimation by animateFloatAsState(
        targetValue = if (isCurrentlyPlaying) playbackProgress else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "progress_animation"
    )
    
    // ================================
    // GESTION DES ACTIONS
    // ================================
    
    val handlePlayPause: () -> Unit = {
        if (isCurrentlyPlaying) {
            if (isPaused) {
                // Si en pause, reprendre la lecture
                audioPlayer.resumePlayback()
            } else {
                // Si en cours de lecture, mettre en pause
                audioPlayer.pausePlayback()
            }
        } else {
            // Arrêt de toute lecture en cours et démarrage de celle-ci
            if (currentPreviewUrl != previewUrl) {
                audioPlayer.stopPlayback()
            }
            // Lancement de la lecture
            audioPlayer.playPreview(previewUrl)
        }
    }
    
    val handleStop: () -> Unit = {
        audioPlayer.stopPlayback()
    }
    
    // ================================
    // EXPANSION AUTOMATIQUE
    // ================================
    
    LaunchedEffect(isCurrentlyPlaying) {
        if (isCurrentlyPlaying) {
            isExpanded = true
        }
    }
    
    // ================================
    // INTERFACE UTILISATEUR
    // ================================
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ================================
            // BOUTON PRINCIPAL DE LECTURE
            // ================================
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Bouton play/pause principal
                IconButton(
                    onClick = handlePlayPause,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isPaused -> MaterialTheme.colorScheme.deezerPurple
                                isCurrentlyPlaying -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.deezerPurple
                            }
                        )
                ) {
                    Image(
                        painter = painterResource(
                            when {
                                isPaused -> R.drawable.play_icon
                                isCurrentlyPlaying -> R.drawable.break_icon
                                else -> R.drawable.play_icon
                            }
                        ),
                        contentDescription = when {
                            isPaused -> "Reprendre la lecture"
                            isCurrentlyPlaying -> "Mettre en pause"
                            else -> "Lancer la lecture"
                        },
                        modifier = Modifier.size(24.dp),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                    )
                }
                
                // Informations de la piste
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = trackTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )
                    
                    Text(
                        text = "Extrait de 30 secondes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Bouton stop (visible seulement en cours de lecture)
                AnimatedVisibility(
                    visible = isCurrentlyPlaying,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    IconButton(
                        onClick = handleStop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.stop_icon),
                            contentDescription = "Arrêter la lecture",
                            modifier = Modifier.size(20.dp),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                    }
                }
            }
            
            // ================================
            // BARRE DE PROGRESSION
            // ================================
            
            AnimatedVisibility(
                visible = isCurrentlyPlaying,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    // Barre de progression
                    LinearProgressIndicator(
                        progress = { progressAnimation },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.deezerPurple,
                        trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Informations de progression
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatDuration(audioPlayer.getCurrentPosition()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Text(
                            text = formatDuration(audioPlayer.getCurrentDuration()),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // ================================
            // INDICATEUR D'ÉTAT
            // ================================
            
            AnimatedVisibility(
                visible = playbackState == PlaybackState.PREPARING,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.deezerPurple
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "Préparation de l'extrait...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // ================================
            // MESSAGES D'ÉTAT
            // ================================
            
            AnimatedVisibility(
                visible = playbackState == PlaybackState.ERROR,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Erreur de lecture",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

/**
 * Formate une durée en millisecondes en format MM:SS
 * 
 * @param milliseconds Durée en millisecondes
 * @return Durée formatée
 */
private fun formatDuration(milliseconds: Int): String {
    if (milliseconds <= 0) return "0:00"
    
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    
    return String.format(Locale.ROOT, "%d:%02d", minutes, remainingSeconds)
}

/**
 * Preview du composant AudioPreviewPlayer
 */
@Preview(name = "Lecteur d'extraits audio", showBackground = true)
@Composable
private fun AudioPreviewPlayerPreview() {
    // Preview avec un mock AudioPlayer
    // Note: En réalité, ce composant nécessite une vraie instance d'AudioPlayer
    Text(
        text = "AudioPreviewPlayer Preview",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}
