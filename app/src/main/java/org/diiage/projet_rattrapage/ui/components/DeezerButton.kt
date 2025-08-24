package org.diiage.projet_rattrapage.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.tooling.preview.Preview
import org.diiage.projet_rattrapage.ui.theme.Projet_RattrapageTheme

/**
 * Bouton principal Deezer avec support d'icône
 * 
 * Ce composant applique les principes du design system Deezer :
 * - Couleurs primaires de la marque
 * - Hauteur standardisée de 48dp
 * - Coins arrondis de 8dp
 * - Support d'icônes avec espacement automatique
 * 
 * Design patterns appliqués :
 * - Builder Pattern : configuration flexible via Modifier
 * - Strategy Pattern : comportements différents selon l'état enabled
 * - Composition Pattern : assemblage d'éléments UI
 * 
 * @param onClick Callback exécuté lors du clic sur le bouton
 * @param modifier Modifier pour la customisation du layout et du style
 * @param enabled État d'activation du bouton (désactivé si false)
 * @param icon Icône optionnelle affichée à gauche du contenu
 * @param content Contenu du bouton (généralement du texte)
 * 
 * @sample
 * ```kotlin
 * DeezerPrimaryButton(
 *     onClick = { /* action */ },
 *     icon = Icons.Default.Search
 * ) {
 *     Text("Rechercher")
 * }
 * ```
 * 
 * @see DeezerTextButton pour un style de bouton texte
 * @since 1.0
 */
@Composable
fun DeezerPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(
            horizontal = if (icon != null) 12.dp else 16.dp,
            vertical = 8.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { iconVector ->
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            content()
        }
    }
}

/**
 * Bouton texte simple avec icône optionnelle
 * 
 * Ce composant fournit un style de bouton texte pour les actions secondaires :
 * - Couleur primaire de la marque Deezer
 * - Support d'icônes avec espacement réduit
 * - Style minimaliste pour les actions non-principales
 * 
 * Utilisé pour :
 * - Actions secondaires dans les formulaires
 * - Liens de navigation
 * - Boutons d'annulation ou de retour
 * 
 * @param onClick Callback exécuté lors du clic sur le bouton
 * @param modifier Modifier pour la customisation du layout et du style
 * @param enabled État d'activation du bouton (désactivé si false)
 * @param icon Icône optionnelle affichée à gauche du contenu
 * @param content Contenu du bouton (généralement du texte)
 * 
 * @sample
 * ```kotlin
 * DeezerTextButton(
 *     onClick = { /* action */ },
 *     icon = Icons.Default.ArrowBack
 * ) {
 *     Text("Retour")
 * }
 * ```
 * 
 * @see DeezerPrimaryButton pour le style de bouton principal
 * @since 1.0
 */
@Composable
fun DeezerTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    content: @Composable () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        contentPadding = PaddingValues(
            horizontal = if (icon != null) 8.dp else 12.dp,
            vertical = 8.dp
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { iconVector ->
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            content()
        }
    }
}

// ================================
// COMPOSANTS RÉUTILISÉS DANS L'APPLICATION
// ================================

// ================================
// PREVIEWS
// ================================

@Preview(showBackground = true)
@Composable
fun DeezerPrimaryButtonPreview() {
    Projet_RattrapageTheme {
        DeezerPrimaryButton(
            onClick = {},
            icon = Icons.Filled.Search
        ) {
            Text("Rechercher")
        }
    }
} 