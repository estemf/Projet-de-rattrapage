package org.diiage.projet_rattrapage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import org.diiage.projet_rattrapage.ui.navigation.NavigationManager



/**
 * Composables d'écran pour l'application Deezer Music
 * 
 * Ces composables représentent les écrans principaux de l'application
 * et respectent l'architecture LEGO définie dans les règles du projet :
 * - Composants réutilisables et modulaires
 * - Séparation claire des responsabilités
 * - Navigation centralisée via NavigationManager
 * - Design system cohérent avec la marque Deezer
 * 
 * Architecture appliquée :
 * - MVVM+ avec séparation UI/Logique
 * - Navigation centralisée via NavigationManager
 * - Composants LEGO assemblables
 * 
 * @since 1.0
 */





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