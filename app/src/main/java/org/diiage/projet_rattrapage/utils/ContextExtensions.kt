package org.diiage.projet_rattrapage.utils

import android.content.Context
import android.content.Intent

import android.widget.Toast
import androidx.annotation.StringRes
import timber.log.Timber

/**
 * Fonctions d'extension pour la classe Context Android
 * 
 * Ces extensions appliquent les principes de la programmation fonctionnelle
 * et améliorent l'expérience développeur en ajoutant des méthodes utilitaires
 * directement sur la classe Context.
 * 
 * Principes appliqués :
 * - Extension Pattern : ajoute des fonctionnalités sans modifier la classe
 * - Single Responsibility : chaque extension a un rôle spécifique
 * - Defensive Programming : gestion des cas d'erreur
 * 

 * @since 1.0
 */

// ================================
// EXTENSIONS POUR LES TOASTS
// ================================

/**
 * Affiche un Toast court avec un message de chaîne
 * 
 * Cette extension simplifie l'affichage de Toasts en encapsulant
 * la logique de création et de gestion d'erreurs
 * 
 * @param message Message à afficher
 * @return true si le Toast a été créé avec succès
 * 
 * @sample
 * ```kotlin
 * context.showToast("Artiste ajouté aux favoris")
 * ```
 */
fun Context.showToast(message: String): Boolean {
    return try {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Timber.d("📱 Toast affiché: '$message'")
        true
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors de l'affichage du Toast")
        false
    }
}



// ================================
// EXTENSIONS POUR LES INTENTS
// ================================



/**
 * Partage du texte via les applications de partage du système
 * 
 * Cette extension facilite le partage de contenu musical
 * (liens vers artistes, albums, etc.)
 * 
 * @param text Texte à partager
 * @param title Titre du sélecteur de partage (optionnel)
 * @return true si l'Intent de partage a été lancé avec succès
 * 
 * @sample
 * ```kotlin
 * context.shareText(
 *     text = "Découvre cet artiste génial: https://deezer.com/artist/123",
 *     title = "Partager cet artiste"
 * )
 * ```
 */
fun Context.shareText(text: String, title: String = "Partager"): Boolean {
    return try {
        if (text.isBlank()) {
            Timber.w("⚠️ Tentative de partage d'un texte vide")
            return false
        }
        
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        val chooserIntent = Intent.createChooser(shareIntent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        startActivity(chooserIntent)
        Timber.i("📤 Contenu partagé: '$text'")
        true
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors du partage")
        showToast("Erreur lors du partage")
        false
    }
}

// ================================
// EXTENSIONS POUR LA VALIDATION
// ================================

/**
 * Vérifie si une permission spécifique est accordée
 * 
 * Cette extension simplifie la vérification des permissions
 * avec logging automatique
 * 
 * @param permission Nom de la permission à vérifier
 * @return true si la permission est accordée
 * 
 * @sample
 * ```kotlin
 * val hasAudioPermission = context.hasPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS)
 * ```
 */
fun Context.hasPermission(permission: String): Boolean {
    return try {
        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
            this, 
            permission
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        
        Timber.d("🔐 Permission '$permission': ${if (hasPermission) "accordée" else "refusée"}")
        hasPermission
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors de la vérification de permission: $permission")
        false
    }
}

/**
 * Récupère une String depuis les ressources avec gestion d'erreur
 * 
 * Cette extension évite les crashes en cas de ressource manquante
 * 
 * @param resId Identifiant de la ressource string
 * @param defaultValue Valeur par défaut si la ressource n'existe pas
 * @return String de la ressource ou valeur par défaut
 * 
 * @sample
 * ```kotlin
 * val title = context.getStringOrDefault(R.string.app_name, "Application Musique")
 * ```
 */


// ================================
// EXTENSIONS POUR LE LOGGING ET DEBUG
// ================================

/**
 * Log les informations détaillées du Context
 * 
 * Cette extension est utile pour le debugging en affichant
 * des informations contextuelles détaillées
 * 
 * @param tag Tag personnalisé pour le log (optionnel)
 * 
 * @sample
 * ```kotlin
 * context.logContextInfo("DEBUG_SCREEN")
 * ```
 */
 