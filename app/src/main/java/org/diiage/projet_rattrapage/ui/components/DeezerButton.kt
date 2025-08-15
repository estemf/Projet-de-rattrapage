package org.diiage.projet_rattrapage.ui.components

import androidx.compose.foundation.BorderStroke
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
 * Bouton principal Deezer
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
 * Bouton texte simple avec icône
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