package org.diiage.projet_rattrapage.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
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

/**
 * Affiche un Toast court avec une ressource string
 * 
 * Cette extension permet d'utiliser directement les ressources
 * string de l'application tout en gérant les erreurs
 * 
 * @param messageRes Identifiant de la ressource string
 * @return true si le Toast a été créé avec succès
 * 
 * @sample
 * ```kotlin
 * context.showToast(R.string.artist_added_to_favorites)
 * ```
 */
fun Context.showToast(@StringRes messageRes: Int): Boolean {
    return try {
        val message = getString(messageRes)
        showToast(message)
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors de l'affichage du Toast (ressource: $messageRes)")
        false
    }
}

/**
 * Affiche un Toast long avec un message de chaîne
 * 
 * Utilisé pour les messages importants qui nécessitent
 * plus de temps de lecture
 * 
 * @param message Message à afficher longuement
 * @return true si le Toast a été créé avec succès
 * 
 * @sample
 * ```kotlin
 * context.showLongToast("Connexion Internet requise pour cette fonctionnalité")
 * ```
 */
fun Context.showLongToast(message: String): Boolean {
    return try {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Timber.d("📱 Toast long affiché: '$message'")
        true
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors de l'affichage du Toast long")
        false
    }
}

// ================================
// EXTENSIONS POUR LES INTENTS
// ================================

/**
 * Ouvre une URL dans le navigateur par défaut
 * 
 * Cette extension encapsule la création d'Intent pour ouvrir des liens
 * externes avec gestion gracieuse des erreurs
 * 
 * @param url URL à ouvrir (doit commencer par http:// ou https://)
 * @return true si l'Intent a été lancé avec succès
 * 
 * @sample
 * ```kotlin
 * context.openUrl("https://www.deezer.com/artist/123456")
 * ```
 */
fun Context.openUrl(url: String): Boolean {
    return try {
        // Validation de l'URL
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            Timber.w("⚠️ URL invalide (doit commencer par http/https): $url")
            showToast("Lien invalide")
            return false
        }
        
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            // Assure que l'Intent s'ouvrira dans une nouvelle tâche
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        // Vérification qu'une application peut gérer cet Intent
        val packageManager = packageManager
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            Timber.i("🌐 URL ouverte: $url")
            true
        } else {
            Timber.w("⚠️ Aucune application trouvée pour ouvrir: $url")
            showToast("Aucune application disponible pour ouvrir ce lien")
            false
        }
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors de l'ouverture de l'URL: $url")
        showToast("Erreur lors de l'ouverture du lien")
        false
    }
}

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
fun Context.getStringOrDefault(@StringRes resId: Int, defaultValue: String = ""): String {
    return try {
        getString(resId)
    } catch (exception: Exception) {
        Timber.w(exception, "⚠️ Ressource string introuvable: $resId, utilisation de la valeur par défaut")
        defaultValue
    }
}

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
fun Context.logContextInfo(tag: String = "CONTEXT_INFO") {
    try {
        val info = mapOf(
            "package_name" to packageName,
            "class_name" to this::class.simpleName,
            "has_internet_permission" to hasPermission(android.Manifest.permission.INTERNET),
            "has_audio_permission" to hasPermission(android.Manifest.permission.MODIFY_AUDIO_SETTINGS),
            "has_vibrate_permission" to hasPermission(android.Manifest.permission.VIBRATE)
        )
        
        Timber.tag(tag).d("📋 Informations du Context: $info")
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors du logging des informations Context")
    }
} 