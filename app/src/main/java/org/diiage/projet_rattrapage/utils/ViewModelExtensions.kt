package org.diiage.projet_rattrapage.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Fonctions d'extension pour la classe ViewModel
 * 
 * Ces extensions améliorent la gestion des coroutines dans les ViewModels
 * en ajoutant une gestion d'erreurs centralisée et un logging détaillé
 * 
 * Principes appliqués :
 * - Exception Handling : gestion centralisée des erreurs async
 * - Logging Pattern : traçabilité complète des opérations
 * - Functional Programming : extensions réutilisables
 * 

 * @since 1.0
 */

// ================================
// EXTENSIONS POUR LA GESTION D'ERREURS
// ================================

/**
 * Lance une coroutine dans le viewModelScope avec gestion d'erreurs centralisée
 * 
 * Cette extension applique le pattern "Defensive Programming" en encapsulant
 * toutes les coroutines ViewModel avec un gestionnaire d'erreurs standardisé
 * 
 * @param onError Callback optionnel pour traiter les erreurs spécifiques
 * @param block Code suspendable à exécuter
 * @return Job de la coroutine lancée
 * 
 * @sample
 * ```kotlin
 * // Dans un ViewModel
 * fun searchArtists(query: String) {
 *     launchSafely(
 *         onError = { error -> 
 *             _uiState.value = UiState.Error("Erreur de recherche")
 *         }
 *     ) {
 *         val artists = repository.searchArtists(query).getOrThrow()
 *         _uiState.value = UiState.Success(artists)
 *     }
 * }
 * ```
 */
fun ViewModel.launchSafely(
    onError: ((Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val viewModelName = this::class.simpleName ?: "UnknownViewModel"
    
    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception, "💥 Exception dans $viewModelName")
        
        // Appel du handler personnalisé si fourni
        onError?.invoke(exception) ?: run {
            // Gestion par défaut des erreurs
            Timber.w("⚠️ Aucun gestionnaire d'erreur fourni pour $viewModelName")
        }
    }
    
    return viewModelScope.launch(exceptionHandler) {
        try {
            Timber.d("🚀 Démarrage d'opération async dans $viewModelName")
            block()
            Timber.d("✅ Opération async terminée avec succès dans $viewModelName")
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Exception capturée dans $viewModelName")
            // L'exception sera aussi gérée par l'ExceptionHandler
            throw exception
        }
    }
}



// ================================
// EXTENSIONS POUR LE LOGGING AVANCÉ
// ================================

/**
 * Log l'état actuel du ViewModel avec ses propriétés principales
 * 
 * Cette extension facilite le debugging en affichant automatiquement
 * les informations importantes du ViewModel
 * 
 * @param tag Tag personnalisé pour le log
 * @param additionalInfo Informations supplémentaires à logger
 * 
 * @sample
 * ```kotlin
 * // Dans un ViewModel
 * fun onSearchQueryChanged(query: String) {
 *     logViewModelState("SEARCH", mapOf("query" to query, "results_count" to artists.size))
 * }
 * ```
 */
fun ViewModel.logViewModelState(
    tag: String = "VM_STATE",
    additionalInfo: Map<String, Any> = emptyMap()
) {
    try {
        val viewModelName = this::class.simpleName ?: "UnknownViewModel"
        val baseInfo = mapOf(
            "viewmodel_class" to viewModelName,
            "hash_code" to hashCode(),
            "active_jobs" to getActiveJobsCount()
        )
        
        val fullInfo = baseInfo + additionalInfo
        
        Timber.tag(tag).d("📋 État du ViewModel: $fullInfo")
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors du logging de l'état ViewModel")
    }
}

/**
 * Obtient le nombre de jobs actifs dans le viewModelScope
 * 
 * Méthode utilitaire pour surveiller les coroutines en cours
 * 
 * @return Nombre de jobs actifs (approximatif)
 */
private fun ViewModel.getActiveJobsCount(): Int {
    return try {
        // Note: il n'y a pas de moyen direct d'obtenir le nombre exact de jobs
        // Cette implémentation est une approximation basée sur l'état du scope
        if (viewModelScope.coroutineContext[Job]?.isActive == true) 1 else 0
    } catch (exception: Exception) {
        Timber.w(exception, "⚠️ Impossible de compter les jobs actifs")
        -1
    }
}

/**
 * Log un événement spécifique dans le ViewModel avec horodatage
 * 
 * Utilisé pour tracer les actions utilisateur et les changements d'état
 * 
 * @param eventName Nom de l'événement
 * @param eventData Données associées à l'événement
 * 
 * @sample
 * ```kotlin
 * fun onArtistClicked(artist: Artist) {
 *     logEvent("ARTIST_CLICKED", mapOf(
 *         "artist_id" to artist.id,
 *         "artist_name" to artist.name
 *     ))
 * }
 * ```
 */
fun ViewModel.logEvent(
    eventName: String,
    eventData: Map<String, Any> = emptyMap()
) {
    try {
        val viewModelName = this::class.simpleName ?: "UnknownViewModel"
        val timestamp = System.currentTimeMillis()
        
        val eventInfo = mapOf(
            "event" to eventName,
            "viewmodel" to viewModelName,
            "timestamp" to timestamp,
            "data" to eventData
        )
        
        Timber.tag("VM_EVENT").i("📊 Événement ViewModel: $eventInfo")
    } catch (exception: Exception) {
        Timber.e(exception, "❌ Erreur lors du logging d'événement: $eventName")
    }
}

// ================================
// EXTENSIONS POUR LA GESTION DES RESSOURCES
// ================================

 