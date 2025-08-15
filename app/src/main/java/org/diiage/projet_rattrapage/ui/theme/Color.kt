package org.diiage.projet_rattrapage.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Palette de couleurs personnalisées pour l'application Deezer Music
 * 
 * Ces couleurs définissent l'identité visuelle de l'application en respectant :
 * - Les guidelines Material Design 3
 * - L'identité de marque Deezer
 * - Les principes d'accessibilité
 * - Le support du mode sombre/clair
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */

// ================================
// COULEURS PRINCIPALES DEEZER
// ================================

/** Violet caractéristique de Deezer */
val DeezerPurple = Color(0xFF6200EE)
val DeezerPurpleLight = Color(0xFFBB86FC)
val DeezerPurpleDark = Color(0xFF3700B3)

/** Couleurs de support pour l'interface */
val DeezerBlack = Color(0xFF1A1A1A)
val DeezerGray = Color(0xFF757575)
val DeezerLightGray = Color(0xFFE0E0E0)
val DeezerWhite = Color(0xFFFFFFFF)

// ================================
// COULEURS SYSTÈME
// ================================

/** Couleurs pour les états de l'application */
val SuccessColor = Color(0xFF4CAF50)
val ErrorColor = Color(0xFFF44336)
val WarningColor = Color(0xFFFF9800)
val InfoColor = Color(0xFF2196F3)

// ================================
// COULEURS MUSICALES
// ================================

/** Couleurs spécifiques au contexte musical */
val PlayingColor = Color(0xFF4CAF50)
val PausedColor = Color(0xFFFF9800)
val StoppedColor = Color(0xFF757575)

/** Couleurs pour les genres musicaux */
val RockColor = Color(0xFFE91E63)
val PopColor = Color(0xFF9C27B0)
val JazzColor = Color(0xFF3F51B5)
val ClassicalColor = Color(0xFF795548)

// ================================
// EXTENSIONS POUR COLORSCHEME
// ================================

/**
 * Extension pour accéder à la couleur violette Deezer depuis un ColorScheme
 */
val androidx.compose.material3.ColorScheme.deezerPurple: Color
    get() = DeezerPurple

/**
 * Helper pour obtenir une couleur selon l'état de connectivité
 * 
 * @param isConnected État de la connexion
 * @param isLimited Si la connexion est limitée
 * @return Couleur appropriée à l'état
 */
fun androidx.compose.material3.ColorScheme.getConnectivityColor(isConnected: Boolean, isLimited: Boolean = false): Color {
    return when {
        !isConnected -> ErrorColor
        isLimited -> WarningColor
        else -> SuccessColor
    }
}

// ================================
// SCHÉMAS DE COULEURS MATERIAL DESIGN 3
// ================================

/**
 * Schéma de couleurs pour le mode clair
 */
val DeezerLightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = DeezerPurple,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = DeezerPurpleLight,
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF21005D),
    
    secondary = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFE8DCFF),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF1A0061),
    
    tertiary = androidx.compose.ui.graphics.Color(0xFF9C27B0),
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    
    background = androidx.compose.ui.graphics.Color(0xFFFFFBFF),
    onBackground = androidx.compose.ui.graphics.Color(0xFF201B16),
    surface = androidx.compose.ui.graphics.Color(0xFFFFFBFF),
    onSurface = androidx.compose.ui.graphics.Color(0xFF201B16),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF0E0CF),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF50453A),
    
    error = ErrorColor,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
    
    outline = androidx.compose.ui.graphics.Color(0xFF837469),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFD3C4B4)
)

/**
 * Schéma de couleurs pour le mode sombre
 */
val DeezerDarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = DeezerPurpleLight,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF21005D),
    primaryContainer = DeezerPurpleDark,
    onPrimaryContainer = DeezerPurpleLight,
    
    secondary = androidx.compose.ui.graphics.Color(0xFFB388FF),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF1A0061),
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF3F2A85),
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFFE8DCFF),
    
    tertiary = androidx.compose.ui.graphics.Color(0xFFE1BEE7),
    onTertiary = androidx.compose.ui.graphics.Color(0xFF4A148C),
    tertiaryContainer = androidx.compose.ui.graphics.Color(0xFF6A1B9A),
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFFF3E5F5),
    
    background = androidx.compose.ui.graphics.Color(0xFF17120D),
    onBackground = androidx.compose.ui.graphics.Color(0xFFEAE1DB),
    surface = androidx.compose.ui.graphics.Color(0xFF17120D),
    onSurface = androidx.compose.ui.graphics.Color(0xFFEAE1DB),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF50453A),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFD3C4B4),
    
    error = androidx.compose.ui.graphics.Color(0xFFFFB4AB),
    onError = androidx.compose.ui.graphics.Color(0xFF690005),
    errorContainer = androidx.compose.ui.graphics.Color(0xFF93000A),
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFFFFDAD6),
    
    outline = androidx.compose.ui.graphics.Color(0xFF9D8E82),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFF50453A)
)