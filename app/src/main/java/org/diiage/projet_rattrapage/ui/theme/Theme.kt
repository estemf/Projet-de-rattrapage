package org.diiage.projet_rattrapage.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Composable de thème principal pour l'application Deezer Music
 * 
 * Ce thème applique les principes du Design System Deezer et Material Design 3 :
 * - Couleurs brandées avec support dark/light mode automatique
 * - Typographie musicale spécialisée et accessible
 * - Cohérence visuelle garantie sur tous les écrans
 * - Support des couleurs dynamiques Material You (Android 12+)
 * - Configuration edge-to-edge moderne
 * 
 * Design patterns appliqués :
 * - Theme Pattern : gestion centralisée des thèmes
 * - Dynamic Color Pattern : support des couleurs système Android 12+
 * - Brand Integration Pattern : intégration cohérente de l'identité Deezer
 * - Edge-to-Edge Pattern : design moderne sans bordures
 * - Strategy Pattern : sélection automatique du schéma de couleurs
 * 
 * Fonctionnalités avancées :
 * - Détection automatique du thème système
 * - Intégration avec ThemeManager pour la persistance
 * - Support des couleurs dynamiques Material You
 * - Configuration automatique de la status bar
 * - Design edge-to-edge pour une expérience immersive
 * 
 * @param darkTheme Force le mode sombre (null = utilise ThemeManager)
 * @param dynamicColor Active les couleurs dynamiques Material You sur Android 12+
 * @param content Contenu de l'application à thématiser
 * 
 * @since 1.0
 * @see ThemeManager
 * @see DeezerLightColorScheme
 * @see DeezerDarkColorScheme
 * @see DeezerTypography
 */
@Composable
fun Projet_RattrapageTheme(
    darkTheme: Boolean? = null, // null = utilise ThemeManager
    // Les couleurs dynamiques sont disponibles sur Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    
    // ================================
    // GESTION DU THÈME VIA THEMEMANAGER
    // ================================
    
    val themeManagerState by ThemeManager.isDarkTheme.collectAsStateWithLifecycle()
    val effectiveDarkTheme = darkTheme ?: themeManagerState
    
    // ================================
    // SÉLECTION DU SCHÉMA DE COULEURS
    // ================================
    
    val colorScheme = when {
        // Android 12+ avec couleurs dynamiques activées
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (effectiveDarkTheme) {
                // Couleurs dynamiques sombres du système
                dynamicDarkColorScheme(context)
            } else {
                // Couleurs dynamiques claires du système
                dynamicLightColorScheme(context)
            }
        }
        
        // Mode sombre avec couleurs Deezer
        effectiveDarkTheme -> DeezerDarkColorScheme
        
        // Mode clair avec couleurs Deezer (par défaut)
        else -> DeezerLightColorScheme
    }
    
    // ================================
    // CONFIGURATION DE LA STATUS BAR
    // ================================
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            
            // Configuration de la status bar compatible avec toutes les versions
            // Utilise WindowCompat pour une approche moderne et rétrocompatible
            WindowCompat.setDecorFitsSystemWindows(window, false)
            
            // Configure les icônes de la status bar (clair/sombre)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !effectiveDarkTheme
        }
    }
    
    // ================================
    // APPLICATION DU THÈME MATERIAL
    // ================================
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = DeezerTypography,
        content = content
    )
}