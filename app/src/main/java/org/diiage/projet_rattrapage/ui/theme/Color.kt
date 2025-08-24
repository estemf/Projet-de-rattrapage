package org.diiage.projet_rattrapage.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme

/**
 * Système de couleurs Material Design 3 pour l'application Deezer Music
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
 * - Material You Pattern : support des couleurs dynamiques Android 12+
 
 * @since 1.0
 * @see DeezerLightColorScheme
 * @see DeezerDarkColorScheme
 * @see androidx.compose.material3.ColorScheme
 */

// ================================
// COULEURS PRINCIPALES DEEZER
// ================================

/** Violet caractéristique de Deezer */
val DeezerPurple = Color(0xFF6200EE)
val DeezerPurpleLight = Color(0xFFBB86FC)
val DeezerPurpleDark = Color(0xFF3700B3)

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
 * 
 * Cette extension fait partie du Design System Deezer et permet d'accéder
 * facilement à la couleur de marque depuis n'importe quel ColorScheme Material 3.
 * 
 * Utilisée dans les composants UI pour maintenir la cohérence visuelle :
 * - DeezerTextField : bordures et curseur
 * - AudioPreviewPlayer : icônes et contrôles
 * - DetailsScreen : éléments d'interface brandés
 * 
 * Design patterns appliqués :
 * - Extension Function Pattern : étend ColorScheme avec des couleurs brandées
 * - Brand Integration Pattern : intégration transparente de l'identité Deezer
 * 
 * @receiver ColorScheme Le schéma de couleurs Material 3 à étendre
 * @return Color La couleur violette caractéristique de Deezer
 * @since 1.0
 * @see DeezerPurple
 * @see androidx.compose.material3.ColorScheme
 */
val ColorScheme.deezerPurple: Color
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
val DeezerLightColorScheme = lightColorScheme(
    primary = DeezerPurple,
    onPrimary = Color.White,
    primaryContainer = DeezerPurpleLight,
    onPrimaryContainer = Color(0xFF21005D),
    
    secondary = DeezerSecondary,
    onSecondary = Color.White,
    secondaryContainer = DeezerSecondaryLight,
    onSecondaryContainer = Color(0xFF1A0061),
    
    tertiary = DeezerTertiary,
    onTertiary = Color.White,
    tertiaryContainer = DeezerTertiaryLight,
    onTertiaryContainer = Color(0xFF4A148C),
    
    background = DeezerBackgroundLight,
    onBackground = Color(0xFF201B16),
    surface = DeezerSurfaceLight,
    onSurface = Color(0xFF201B16),
    surfaceVariant = Color(0xFFF0E0CF),
    onSurfaceVariant = Color(0xFF50453A),
    
    error = DeezerError,
    onError = Color.White,
    errorContainer = DeezerErrorLight,
    onErrorContainer = Color(0xFF410002),
    
    outline = Color(0xFF837469),
    outlineVariant = Color(0xFFD3C4B4)
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
val DeezerDarkColorScheme = darkColorScheme(
    primary = DeezerPurpleLight,
    onPrimary = Color(0xFF21005D),
    primaryContainer = DeezerPurpleDark,
    onPrimaryContainer = DeezerPurpleLight,
    
    secondary = Color(0xFFB388FF),
    onSecondary = Color(0xFF1A0061),
    secondaryContainer = DeezerSecondaryDark,
    onSecondaryContainer = DeezerSecondaryLight,
    
    tertiary = Color(0xFFE1BEE7),
    onTertiary = Color(0xFF4A148C),
    tertiaryContainer = DeezerTertiaryDark,
    onTertiaryContainer = DeezerTertiaryLight,
    
    background = DeezerBackgroundDark,
    onBackground = Color(0xFFEAE1DB),
    surface = DeezerSurfaceDark,
    onSurface = Color(0xFFEAE1DB),
    surfaceVariant = Color(0xFF50453A),
    onSurfaceVariant = Color(0xFFD3C4B4),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = DeezerErrorDark,
    onErrorContainer = DeezerErrorLight,
    
    outline = Color(0xFF9D8E82),
    outlineVariant = Color(0xFF50453A)
)