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
 * @author Équipe DIIAGE
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
 * Écran de statut de connectivité avec logos WiFi
 * 
 * @param navigationManager Manager de navigation
 */
@Composable
fun ConnectivityStatusScreen(
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
            text = "📶 État de connectivité WiFi",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Exemples des différents états WiFi
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // WiFi excellent
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.wifi),
                    contentDescription = "WiFi excellent",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "WiFi Excellent/Bon",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // WiFi faible
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.wifi_low),
                    contentDescription = "WiFi faible",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "WiFi Faible",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // WiFi déconnecté
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.wifi_disconnect),
                    contentDescription = "WiFi déconnecté",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "WiFi Déconnecté",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
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