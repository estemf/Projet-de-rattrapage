package org.diiage.projet_rattrapage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.diiage.projet_rattrapage.ui.navigation.DeezerNavHost
import org.diiage.projet_rattrapage.ui.navigation.NavigationManager
import org.diiage.projet_rattrapage.ui.theme.Projet_RattrapageTheme
import timber.log.Timber
import org.koin.android.ext.android.get

/**
 * Activité principale de l'application Deezer Music
 * 
 * Cette activité applique les règles du projet :
 * - Une seule Activity pour toute l'application
 * - Navigation gérée via NavigationManager centralisé
 * - UI moderne avec Jetpack Compose
 * - Injection de dépendances avec Koin
 * 
 * Architecture respectée :
 * - Single Activity Pattern (règle du projet)
 * - Composition over inheritance
 * - Dependency Injection
 * - Edge-to-edge design moderne
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
class MainActivity : ComponentActivity() {
    
    /**
     * NavigationManager injecté par Koin
     * 
     * Gère toute la navigation de l'application de manière centralisée
     * selon les bonnes pratiques définies dans les règles
     */
    private val navigationManager: NavigationManager by lazy { get<NavigationManager>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ================================
        // CONFIGURATION DE L'INTERFACE
        // ================================
        
        // Active le design edge-to-edge moderne
        enableEdgeToEdge()
        
        // Log du démarrage de l'activité
        Timber.i("🚀 MainActivity créée - Application Deezer Music démarrée")
        
        // ================================
        // CONFIGURATION DE L'UI COMPOSE
        // ================================
        
        setContent {
            // Application du thème personnalisé de l'application
            Projet_RattrapageTheme {
                // Configuration de l'interface principale avec navigation
                DeezerMusicApp(navigationManager = navigationManager)
            }
        }
        
        Timber.d("✅ Interface Compose configurée avec succès")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // ================================
        // NETTOYAGE DES RESSOURCES
        // ================================
        
        // Libère les ressources du NavigationManager
        navigationManager.dispose()
        Timber.d("🧹 NavigationManager libéré")
        
        Timber.i("👋 MainActivity détruite")
    }
}

/**
 * Composable principal de l'application Deezer Music
 * 
 * Cette composable orchestre l'interface utilisateur complète :
 * - Navigation centralisée
 * - Layout responsive avec Scaffold
 * - Gestion des insets système
 * 
 * @param navigationManager Manager de navigation injecté
 */
@Composable
private fun DeezerMusicApp(
    navigationManager: NavigationManager
) {
    // ================================
    // CRÉATION DU CONTRÔLEUR DE NAVIGATION
    // ================================
    
    val navController = rememberNavController()
    
    // ================================
    // STRUCTURE PRINCIPALE DE L'APP
    // ================================
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        
        // ================================
        // NAVIGATION HOST PRINCIPAL
        // ================================
        
        DeezerNavHost(
            navController = navController,
            navigationManager = navigationManager,
            modifier = Modifier.padding(innerPadding)
        )
    }
    
    Timber.d("🎨 Interface principale rendue avec succès")
}