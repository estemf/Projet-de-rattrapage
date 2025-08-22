package org.diiage.projet_rattrapage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.diiage.projet_rattrapage.R
import org.diiage.projet_rattrapage.ui.navigation.NavigationManager

/**
 * Composables d'écran pour l'application Deezer Music
 * 
 * Ces composables représentent les écrans principaux de l'application
 * et respectent l'architecture LEGO définie dans les règles du projet :
 * - Composants réutilisables
 * - Séparation des responsabilités
 * - Navigation centralisée
 * 

 * @since 1.0
 */

/**
 * Écran de détails d'un artiste
 * 
 * @param artistId Identifiant de l'artiste
 * @param navigationManager Manager de navigation
 */
@Composable
fun ArtistDetailsScreen(
    artistId: Long,
    navigationManager: NavigationManager
) {
    // Cette fonction sera remplacée par la nouvelle DetailsScreen
    // Redirection temporaire vers l'ancienne implémentation
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎤 Détails de l'artiste",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "ID: $artistId",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navigationManager.navigateBack() }
        ) {
            Text("Retour")
        }
    }
}

/**
 * Écran de détails d'un album
 * 
 * @param albumId Identifiant de l'album
 * @param navigationManager Manager de navigation
 */
@Composable
fun AlbumDetailsScreen(
    albumId: Long,
    navigationManager: NavigationManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💿 Détails de l'album",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "ID: $albumId",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navigationManager.navigateBack() }
        ) {
            Text("Retour")
        }
    }
}



/**
 * Écran des paramètres
 * 
 * @param navigationManager Manager de navigation
 */
@Composable
fun SettingsScreen(
    navigationManager: NavigationManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚙️ Paramètres",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navigationManager.navigateBack() }
        ) {
            Text("Retour")
        }
    }
} 