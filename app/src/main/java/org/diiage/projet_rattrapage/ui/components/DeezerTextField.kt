package org.diiage.projet_rattrapage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import org.diiage.projet_rattrapage.ui.theme.deezerPurple
import org.diiage.projet_rattrapage.ui.theme.Projet_RattrapageTheme

/**
 * Système de champs de saisie Deezer - Composants LEGO du Design System
 * 
 * Ces composants TextField appliquent la philosophie LEGO :
 * - Réutilisation dans tous les formulaires
 * - Cohérence visuelle Deezer
 * - Accessibilité intégrée
 * - Gestion d'erreurs standardisée
 * 
 * Types de champs disponibles :
 * - Search : recherche d'artistes, albums, pistes
 * - Standard : saisie générale
 * - Outlined : mise en valeur
 * - Error : avec gestion d'erreurs
 * 
 * Fonctionnalités intégrées :
 * - Validation en temps réel
 * - Actions clavier
 * - Icônes contextuelles
 * - Feedback visuel d'état
 * 
 * Design patterns appliqués :
 * - Builder Pattern : configuration flexible via Modifier
 * - Strategy Pattern : comportements différents selon le type
 * - Observer Pattern : réactivité aux changements de valeur
 * 
 * @since 1.0
 */

// ================================
// COMPOSANT DE RECHERCHE UTILISÉ
// ================================

// ================================
// CHAMP DE RECHERCHE PRINCIPAL
// ================================

/**
 * Champ de recherche Deezer optimisé pour la musique
 * 
 * Spécialement conçu pour :
 * - Recherche d'artistes, albums et pistes
 * - Suggestions automatiques en temps réel
 * - Validation de requête (minimum 2 caractères)
 * - Actions rapides (recherche, effacement)
 * 
 * Caractéristiques techniques :
 * - Validation en temps réel avec feedback visuel
 * - Gestion des états (loading, error, success)
 * - Support des actions clavier (recherche sur Entrée)
 * - Focus management automatique
 * 
 * @param value Valeur actuelle de la recherche
 * @param onValueChange Callback de changement de valeur (validation en temps réel)
 * @param onSearch Callback d'exécution de la recherche (validation finale)
 * @param modifier Modifier pour la customisation du layout et du style
 * @param placeholder Texte d'aide à la saisie (contexte de recherche)
 * @param enabled État activé/désactivé du champ
 * @param isLoading Indicateur de chargement pendant la recherche
 * @param error Message d'erreur optionnel (validation ou API)
 * 
 * @sample
 * ```kotlin
 * DeezerSearchField(
 *     value = searchQuery,
 *     onValueChange = { query -> 
 *         searchQuery = query
 *         validateQuery(query)
 *     },
 *     onSearch = { query -> 
 *         if (query.trim().length >= 2) {
 *             performSearch(query)
 *         }
 *     },
 *     isLoading = isSearching,
 *     error = searchError
 * )
 * ```
 * 

 * @since 1.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeezerSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Rechercher un artiste...",
    enabled: Boolean = true,
    isLoading: Boolean = false,
    error: String? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    
    // Validation de la requête de recherche
    val isValidQuery = value.trim().length >= 2
    val hasError = error != null || (value.isNotEmpty() && !isValidQuery)
    
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent ->
                    // Intercepter la touche Enter pour éviter la propagation
                    if (keyEvent.key == Key.Enter) {
                        if (isValidQuery) {
                            keyboardController?.hide()
                            onSearch(value.trim())
                        }
                        true // Consommer l'événement
                    } else {
                        false // Laisser passer les autres événements
                    }
                },
            enabled = enabled && !isLoading,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            leadingIcon = {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.deezerPurple
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Rechercher",
                        tint = if (hasError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.deezerPurple
                        }
                    )
                }
            },
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Effacer",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            isError = hasError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.deezerPurple,
                cursorColor = MaterialTheme.colorScheme.deezerPurple
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (isValidQuery) {
                        keyboardController?.hide()
                        onSearch(value.trim())
                    }
                }
            ),
            singleLine = true
        )
        
        // Message d'erreur ou d'aide
        if (hasError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error ?: "Saisissez au moins 2 caractères",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

// ================================
// FIN DU FICHIER
// ================================

// ================================
// PREVIEW DU COMPOSANT UTILISÉ
// ================================

@Preview(name = "Champ de recherche Deezer", showBackground = true)
@Composable
private fun DeezerSearchFieldPreview() {
    Projet_RattrapageTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var searchValue by remember { mutableStateOf("") }
            
            DeezerSearchField(
                value = searchValue,
                onValueChange = { searchValue = it },
                onSearch = { /* Action de recherche */ }
            )
        }
    }
} 