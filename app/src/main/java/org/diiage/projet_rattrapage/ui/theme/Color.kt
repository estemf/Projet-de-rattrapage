package org.diiage.projet_rattrapage.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Palette de couleurs personnalisées pour l'application Deezer Music
 * 
 * Cette palette applique les principes de Material Design 3 et respecte :
 * - Les guidelines Material Design 3 pour la cohérence visuelle
 * - L'identité de marque Deezer avec le violet caractéristique
 * - Les principes d'accessibilité (contraste, lisibilité)
 * - Le support complet du mode sombre/clair avec thèmes adaptés
 * 
 * Design patterns appliqués :
 * - Color Scheme Pattern : thèmes clair/sombre cohérents
 * - Brand Color Pattern : palette centrée sur l'identité Deezer
 * - Accessibility Pattern : couleurs respectant les standards WCAG
 * 
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
val DeezerGray = Color(0xFF757575)
val DeezerWhite = Color(0xFFFFFFFF)

// ================================
// COULEURS SYSTÈME ET ACCENT
// ================================

/** Couleurs d'accent pour l'interface */
val DeezerSecondary = Color(0xFF7C4DFF)
val DeezerSecondaryLight = Color(0xFFE8DCFF)
val DeezerSecondaryDark = Color(0xFF3F2A85)

val DeezerTertiary = Color(0xFF9C27B0)
val DeezerTertiaryLight = Color(0xFFF3E5F5)
val DeezerTertiaryDark = Color(0xFF6A1B9A)

/** Couleurs d'état système */
val DeezerError = Color(0xFFF44336)
val DeezerErrorLight = Color(0xFFFFDAD6)
val DeezerErrorDark = Color(0xFF93000A)

/** Couleurs neutres pour les thèmes */
val DeezerBackgroundLight = Color(0xFFFFFBFF)
val DeezerBackgroundDark = Color(0xFF17120D)
val DeezerSurfaceLight = Color(0xFFFFFBFF)
val DeezerSurfaceDark = Color(0xFF17120D)



// ================================
// EXTENSIONS POUR COLORSCHEME
// ================================

/**
 * Extension pour accéder à la couleur violette Deezer depuis un ColorScheme
 */
val androidx.compose.material3.ColorScheme.deezerPurple: Color
    get() = DeezerPurple



// ================================
// SCHÉMAS DE COULEURS MATERIAL DESIGN 3
// ================================

/**
 * Schéma de couleurs pour le mode clair
 * 
 * Ce schéma applique le Material Design 3 Color System avec :
 * - Couleurs primaires basées sur l'identité Deezer
 * - Contraste optimal pour la lisibilité
 * - Harmonisation des couleurs d'accent et tertiaires
 * - Support complet de l'accessibilité
 * 
 * Design patterns appliqués :
 * - Light Theme Pattern : palette claire et lumineuse
 * - Brand Integration Pattern : intégration cohérente de l'identité Deezer
 * - Accessibility Pattern : respect des ratios de contraste
 * 
 * @since 1.0
 */
val DeezerLightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = DeezerPurple,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = DeezerPurpleLight,
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF21005D),
    
    secondary = DeezerSecondary,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = DeezerSecondaryLight,
    onSecondaryContainer = androidx.compose.ui.graphics.Color(0xFF1A0061),
    
    tertiary = DeezerTertiary,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = DeezerTertiaryLight,
    onTertiaryContainer = androidx.compose.ui.graphics.Color(0xFF4A148C),
    
    background = DeezerBackgroundLight,
    onBackground = androidx.compose.ui.graphics.Color(0xFF201B16),
    surface = DeezerSurfaceLight,
    onSurface = androidx.compose.ui.graphics.Color(0xFF201B16),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF0E0CF),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF50453A),
    
    error = DeezerError,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = DeezerErrorLight,
    onErrorContainer = androidx.compose.ui.graphics.Color(0xFF410002),
    
    outline = androidx.compose.ui.graphics.Color(0xFF837469),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFFD3C4B4)
)

/**
 * Schéma de couleurs pour le mode sombre
 * 
 * Ce schéma applique le Material Design 3 Color System avec :
 * - Adaptation des couleurs Deezer pour l'environnement sombre
 * - Contraste optimal pour la lisibilité en conditions de faible luminosité
 * - Harmonisation des couleurs d'accent et tertiaires
 * - Support complet de l'accessibilité
 * 
 * Design patterns appliqués :
 * - Dark Theme Pattern : palette sombre et reposante
 * - Brand Integration Pattern : adaptation de l'identité Deezer au mode sombre
 * - Accessibility Pattern : respect des ratios de contraste en mode sombre
 * 
 * @since 1.0
 */
val DeezerDarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = DeezerPurpleLight,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF21005D),
    primaryContainer = DeezerPurpleDark,
    onPrimaryContainer = DeezerPurpleLight,
    
    secondary = androidx.compose.ui.graphics.Color(0xFFB388FF),
    onSecondary = androidx.compose.ui.graphics.Color(0xFF1A0061),
    secondaryContainer = DeezerSecondaryDark,
    onSecondaryContainer = DeezerSecondaryLight,
    
    tertiary = androidx.compose.ui.graphics.Color(0xFFE1BEE7),
    onTertiary = androidx.compose.ui.graphics.Color(0xFF4A148C),
    tertiaryContainer = DeezerTertiaryDark,
    onTertiaryContainer = DeezerTertiaryLight,
    
    background = DeezerBackgroundDark,
    onBackground = androidx.compose.ui.graphics.Color(0xFFEAE1DB),
    surface = DeezerSurfaceDark,
    onSurface = androidx.compose.ui.graphics.Color(0xFFEAE1DB),
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF50453A),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFD3C4B4),
    
    error = androidx.compose.ui.graphics.Color(0xFFFFB4AB),
    onError = androidx.compose.ui.graphics.Color(0xFF690005),
    errorContainer = DeezerErrorDark,
    onErrorContainer = DeezerErrorLight,
    
    outline = androidx.compose.ui.graphics.Color(0xFF9D8E82),
    outlineVariant = androidx.compose.ui.graphics.Color(0xFF50453A)
)