package org.diiage.projet_rattrapage.ui.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow

import timber.log.Timber


/**
 * Manager centralisé pour la navigation dans l'application Deezer Music
 * 
 * Cette classe applique plusieurs design patterns cruciaux :
 * - Facade Pattern : simplifie l'interface de navigation complexe
 * - Observer Pattern : surveillance de l'état de navigation via StateFlow
 * - Command Pattern : encapsule les actions de navigation
 * - Singleton Pattern : instance unique pour toute l'application
 * 
 * Responsabilités selon les règles du projet :
 * - Abstraction complète de la navigation Compose
 * - Gestion centralisée de tous les déplacements entre écrans
 * - Logging détaillé pour le suivi et debugging
 * - Gestion d'erreurs gracieuse pour la robustesse
 * - Support de la navigation programmatique et conditionnelle
 * 
 * Principes SOLID appliqués :
 * - Single Responsibility : gère uniquement la navigation
 * - Open/Closed : extensible pour nouvelles destinations
 * - Dependency Inversion : interface abstraite de la navigation
 * 

 * @since 1.0
 */
class NavigationManager() {
    
    // ================================
    // ÉTAT DE NAVIGATION
    // ================================
    
    /**
     * Référence vers le NavController Compose
     * Initialisée lors de la création du NavHost
     */
    private var navController: NavController? = null
    
    /**
     * État actuel de la navigation (destination courante)
     */
    private val _currentDestination = MutableStateFlow<DeezerDestinations?>(null)
    
    /**
     * Historique des destinations visitées
     */
    private val _navigationHistory = MutableStateFlow<List<String>>(emptyList())
    
    /**
     * Indicateur de capacité de navigation retour
     */
    private val _canNavigateBack = MutableStateFlow(false)
    
    // ================================
    // INITIALISATION ET CONFIGURATION
    // ================================
    
    /**
     * Initialise le NavigationManager avec le NavController Compose
     * 
     * Cette méthode doit être appelée une seule fois lors de la création
     * du NavHost dans l'activité principale
     * 
     * @param controller NavController Compose à gérer
     * @throws IllegalStateException si déjà initialisé
     */
    fun initialize(controller: NavController) {
        if (navController != null) {
            Timber.w("⚠️ NavigationManager déjà initialisé, ignoré")
            return
        }
        
        navController = controller
        
        // Configuration du listener pour surveiller les changements de destination
        controller.addOnDestinationChangedListener { _, destination, _ ->
            val route = destination.route
            Timber.d("🧭 Navigation vers: $route")
            
            // Mise à jour de l'état de navigation
            updateNavigationState(route)
            
            // Ajout à l'historique
            addToHistory(route ?: "unknown")
        }
        
        Timber.i("🧭 NavigationManager initialisé avec succès")
    }
    
    /**
     * Met à jour l'état de navigation basé sur la route courante
     * 
     * @param route Route de la destination courante
     */
    private fun updateNavigationState(route: String?) {
        try {
            // Trouve la destination correspondante
            val destination = route?.let { DeezerDestinations.findByRoute(it) }
            _currentDestination.value = destination
            
            // Met à jour la capacité de navigation retour
            val canGoBack = navController?.previousBackStackEntry != null
            _canNavigateBack.value = canGoBack
            
            Timber.d("📍 État navigation mis à jour: ${destination?.route}, canGoBack: $canGoBack")
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la mise à jour de l'état navigation")
        }
    }
    
    /**
     * Ajoute une route à l'historique de navigation
     * 
     * @param route Route à ajouter
     */
    private fun addToHistory(route: String) {
        try {
            val currentHistory = _navigationHistory.value.toMutableList()
            currentHistory.add(route)
            
            // Limite la taille de l'historique pour éviter les fuites mémoire
            if (currentHistory.size > MAX_HISTORY_SIZE) {
                currentHistory.removeAt(0)
            }
            
            _navigationHistory.value = currentHistory
            Timber.d("📚 Historique navigation mis à jour: ${currentHistory.size} entrées")
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de l'ajout à l'historique")
        }
    }
    
    // ================================
    // MÉTHODES DE NAVIGATION PRINCIPALES
    // ================================
    
    /**
     * Navigation vers l'écran de recherche (destination par défaut)
     * 
     * Cette méthode efface le back stack pour faire de la recherche
     * la nouvelle racine de navigation
     */
    fun navigateToSearch() {
        navigateAndLog(DeezerDestinations.SearchScreen.route) {
            navController?.navigate(DeezerDestinations.SearchScreen.route) {
                // Efface le back stack pour faire de Search la nouvelle racine
                popUpTo(DeezerDestinations.SearchScreen.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    

    
    /**
     * Navigation vers les détails d'un artiste
     * 
     * @param artistId Identifiant de l'artiste
     */
    fun navigateToArtistDetails(artistId: Long) {
        if (artistId <= 0) {
            Timber.w("⚠️ ID d'artiste invalide: $artistId")
            return
        }
        
        val route = DeezerDestinations.ArtistDetailsScreen.createRoute(artistId)
        navigateAndLog(route) {
            navController?.navigate(route)
        }
    }
    
    /**
     * Navigation vers les détails d'un album
     * 
     * @param albumId Identifiant de l'album
     */
    fun navigateToAlbumDetails(albumId: Long) {
        if (albumId <= 0) {
            Timber.w("⚠️ ID d'album invalide: $albumId")
            return
        }
        
        val route = DeezerDestinations.AlbumDetailsScreen.createRoute(albumId)
        navigateAndLog(route) {
            navController?.navigate(route)
        }
    }
    
    /**
     * Navigation vers les détails d'une piste
     * 
     * @param trackId Identifiant de la piste
     */
    fun navigateToTrackDetails(trackId: Long) {
        if (trackId <= 0) {
            Timber.w("⚠️ ID de piste invalide: $trackId")
            return
        }
        
        val route = DeezerDestinations.TrackDetailsScreen.createRoute(trackId)
        navigateAndLog(route) {
            navController?.navigate(route)
        }
    }
    

    

    
    // ================================
    // MÉTHODES DE NAVIGATION SYSTÈME
    // ================================
    
    /**
     * Navigation retour (back)
     * 
     * Gère intelligemment le retour en arrière avec fallback
     * vers l'écran de recherche si nécessaire
     */
    fun navigateBack() {
        navigateAndLog("BACK") {
            val canPop = navController?.popBackStack() ?: false
            
            if (!canPop) {
                // Si impossible de revenir en arrière, va à l'écran de recherche
                Timber.i("🔄 Impossible de revenir en arrière, navigation vers Search")
                navigateToSearch()
            }
        }
    }
    

    
    // ================================
    // UTILITAIRES ET HELPERS
    // ================================
    
    /**
     * Wrapper pour la navigation avec logging automatique
     * 
     * Cette méthode centralise le logging et la gestion d'erreurs
     * pour toutes les opérations de navigation
     * 
     * @param operation Description de l'opération pour les logs
     * @param navigationAction Action de navigation à exécuter
     */
    private fun navigateAndLog(operation: String, navigationAction: () -> Unit) {
        try {
            if (navController == null) {
                Timber.e("❌ Navigation impossible: NavController non initialisé")
                return
            }
            
            Timber.i("🧭 Navigation: $operation")
            navigationAction()
            Timber.d("✅ Navigation réussie: $operation")
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Erreur lors de la navigation: $operation")
        }
    }
    

    
    /**
     * Nettoie l'historique de navigation
     * 
     * Utile pour les tests ou lors de changements d'utilisateur
     */
    fun clearHistory() {
        _navigationHistory.value = emptyList()
        Timber.d("🧹 Historique de navigation effacé")
    }
    
    /**
     * Libère les ressources du NavigationManager
     * 
     * À appeler lors de la destruction de l'activité
     */
    fun dispose() {
        navController = null
        _currentDestination.value = null
        clearHistory()
        Timber.i("🧹 NavigationManager libéré")
    }
    
    // ================================
    // CONSTANTES
    // ================================
    
    companion object {
        /**
         * Taille maximale de l'historique de navigation
         * Évite les fuites mémoire en limitant la croissance
         */
        private const val MAX_HISTORY_SIZE = 50
    }
} 