package org.diiage.projet_rattrapage.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.diiage.projet_rattrapage.ui.theme.DeezerDarkColorScheme
import org.diiage.projet_rattrapage.ui.theme.DeezerLightColorScheme
import org.diiage.projet_rattrapage.ui.theme.DeezerTypography

/**
 * Thème principal de l'application Deezer Music
 * 
 * Ce thème applique les principes du Design System Deezer :
 * - Couleurs brandées avec support dark/light mode
 * - Typographie musicale spécialisée
 * - Cohérence visuelle garantie
 * - Support des couleurs dynamiques Android 12+
 * 
 * Design patterns appliqués :
 * - Theme Pattern : gestion centralisée des thèmes
 * - Dynamic Color Pattern : support des couleurs système Android 12+
 * - Brand Integration Pattern : intégration cohérente de l'identité Deezer
 * - Edge-to-Edge Pattern : design moderne sans bordures
 * 
 * Fonctionnalités :
 * - Détection automatique du thème système
 * - Support des couleurs dynamiques Material You
 * - Configuration de la status bar
 * - Edge-to-edge design
 * 
 * @param darkTheme Force le mode sombre (null = automatique)
 * @param dynamicColor Active les couleurs dynamiques sur Android 12+
 * @param content Contenu de l'application
 * @since 1.0
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
            
            // Définit la couleur de la status bar selon le thème
            window.statusBarColor = colorScheme.primary.toArgb()
            
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