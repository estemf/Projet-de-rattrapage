package org.diiage.projet_rattrapage.ui.navigation

/**
 * Définition des destinations de navigation de l'application Deezer Music
 * 
 * Cette classe applique le pattern Sealed Class pour créer une hiérarchie
 * type-safe des destinations de navigation, facilitant :
 * - La navigation sécurisée entre écrans
 * - Le passage de paramètres typés
 * - La gestion centralisée des routes
 * 
 * Principe Single Responsibility : chaque destination a un rôle précis
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
sealed class DeezerDestinations(val route: String) {
    
    // ================================
    // ÉCRANS PRINCIPAUX
    // ================================
    
    /**
     * Écran d'accueil avec recherche d'artistes
     * 
     * Fonctionnalités :
     * - Champ de recherche principal
     * - Suggestions d'artistes populaires
     * - Historique de recherche
     */
    object SearchScreen : DeezerDestinations("search") {
        const val TITLE = "Recherche d'artistes"
    }
    
    /**
     * Écran d'affichage des résultats de recherche
     * 
     * Paramètres :
     * - query : terme de recherche utilisé
     * 
     * Fonctionnalités :
     * - Liste des artistes trouvés
     * - Possibilité de filtrer/trier
     * - Navigation vers les détails d'artiste
     */
    object SearchResultsScreen : DeezerDestinations("search_results/{query}") {
        const val TITLE = "Résultats de recherche"
        const val ARG_QUERY = "query"
        
        /**
         * Construit la route avec paramètre pour la navigation
         * 
         * @param query Terme de recherche à passer
         * @return Route complète avec paramètre
         */
        fun createRoute(query: String): String {
            return "search_results/${query}"
        }
    }
    
    /**
     * Écran de détails d'un artiste
     * 
     * Paramètres :
     * - artistId : identifiant unique de l'artiste
     * 
     * Fonctionnalités :
     * - Informations complètes de l'artiste
     * - Liste des albums
     * - Pistes populaires
     * - Actions (partage, favoris)
     */
    object ArtistDetailsScreen : DeezerDestinations("artist_details/{artistId}") {
        const val TITLE = "Détails de l'artiste"
        const val ARG_ARTIST_ID = "artistId"
        
        /**
         * Construit la route avec paramètre pour la navigation
         * 
         * @param artistId Identifiant de l'artiste
         * @return Route complète avec paramètre
         */
        fun createRoute(artistId: Long): String {
            return "artist_details/$artistId"
        }
    }
    
    /**
     * Écran de détails d'un album
     * 
     * Paramètres :
     * - albumId : identifiant unique de l'album
     * 
     * Fonctionnalités :
     * - Informations de l'album
     * - Liste des pistes
     * - Aperçus audio
     */
    object AlbumDetailsScreen : DeezerDestinations("album_details/{albumId}") {
        const val TITLE = "Détails de l'album"
        const val ARG_ALBUM_ID = "albumId"
        
        /**
         * Construit la route avec paramètre pour la navigation
         * 
         * @param albumId Identifiant de l'album
         * @return Route complète avec paramètre
         */
        fun createRoute(albumId: Long): String {
            return "album_details/$albumId"
        }
    }
    
    /**
     * Écran de détails d'une piste musicale
     * 
     * Paramètres :
     * - trackId : identifiant unique de la piste
     * 
     * Fonctionnalités :
     * - Informations complètes de la piste
     * - Aperçu audio de 30 secondes
     * - Liens vers artiste et album
     * - Actions (favoris, partage)
     */
    object TrackDetailsScreen : DeezerDestinations("track_details/{trackId}") {
        const val TITLE = "Détails de la piste"
        const val ARG_TRACK_ID = "trackId"
        
        /**
         * Construit la route avec paramètre pour la navigation
         * 
         * @param trackId Identifiant de la piste
         * @return Route complète avec paramètre
         */
        fun createRoute(trackId: Long): String {
            return "track_details/$trackId"
        }
    }
    
    // ================================
    // ÉCRANS UTILITAIRES
    // ================================
    
    /**
     * Écran d'état de connectivité
     * 
     * Fonctionnalités :
     * - Affichage de l'état réseau en temps réel
     * - Informations de debugging
     * - Test de connectivité
     */
    object ConnectivityStatusScreen : DeezerDestinations("connectivity_status") {
        const val TITLE = "État de la connectivité"
    }
    
    /**
     * Écran des paramètres de l'application
     * 
     * Fonctionnalités :
     * - Paramètres audio
     * - Préférences utilisateur
     * - Informations sur l'application
     */
    object SettingsScreen : DeezerDestinations("settings") {
        const val TITLE = "Paramètres"
    }
    
    // ================================
    // MÉTHODES UTILITAIRES
    // ================================
    
    companion object {
        
        /**
         * Liste de toutes les destinations disponibles
         * 
         * Utile pour la configuration de navigation et le debugging
         */
        val allDestinations = listOf(
            SearchScreen,
            SearchResultsScreen,
            ArtistDetailsScreen,
            AlbumDetailsScreen,
            TrackDetailsScreen,
            ConnectivityStatusScreen,
            SettingsScreen
        )
        
        /**
         * Destination par défaut au démarrage de l'application
         */
        val startDestination = SearchScreen
        
        /**
         * Trouve une destination par sa route
         * 
         * @param route Route à rechercher
         * @return Destination correspondante ou null
         */
        fun findByRoute(route: String): DeezerDestinations? {
            return allDestinations.find { destination ->
                // Correspondance exacte ou pattern match pour les routes avec paramètres
                route == destination.route || 
                route.matches(Regex(destination.route.replace("{[^}]+}".toRegex(), "[^/]+")))
            }
        }
        
        /**
         * Vérifie si une route nécessite des paramètres
         * 
         * @param route Route à vérifier
         * @return true si la route contient des paramètres
         */
        fun requiresParameters(route: String): Boolean {
            return route.contains("{") && route.contains("}")
        }
        
        /**
         * Extrait les noms de paramètres d'une route
         * 
         * @param route Route à analyser
         * @return Liste des noms de paramètres
         */
        fun extractParameterNames(route: String): List<String> {
            val regex = "\\{([^}]+)\\}".toRegex()
            return regex.findAll(route)
                .map { it.groupValues[1] }
                .toList()
        }
    }
} 