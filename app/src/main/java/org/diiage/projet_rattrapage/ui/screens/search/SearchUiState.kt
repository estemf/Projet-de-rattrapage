package org.diiage.projet_rattrapage.ui.screens.search

import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track

/**
 * État UI pour l'écran de recherche étendu
 * 
 * Cette classe applique les principes de programmation réactive :
 * - État immutable pour éviter les mutations accidentelles
 * - Propriétés calculées pour optimiser les re-compositions
 * - Séparation claire entre état de données et état d'UI
 * 
 * Selon la règle : "Former les StateFlow avec des informations relatives 
 * à la représentation visuelle"
 * 
 * @property searchQuery Requête de recherche actuelle
 * @property selectedSearchType Type de recherche sélectionné
 * @property isLoading Indicateur de chargement
 * @property searchResults Résultats de recherche d'artistes
 * @property albumResults Résultats de recherche d'albums
 * @property trackResults Résultats de recherche de pistes
 * @property error Message d'erreur ou null
 * @property searchHistory Historique des recherches récentes


 * @property hasSearched Indique si une recherche a été effectuée
 * @property isSearchFieldFocused État du focus du champ de recherche
 * 

 * @since 1.0
 */
data class SearchUiState(
    // ================================
    // ÉTAT DE RECHERCHE
    // ================================
    val searchQuery: String = "",
    val selectedSearchType: SearchType = SearchType.ARTISTS,
    val isLoading: Boolean = false,
    
    // ================================
    // RÉSULTATS PAR TYPE
    // ================================
    val searchResults: List<Artist> = emptyList(),
    val albumResults: List<Album> = emptyList(),
    val trackResults: List<Track> = emptyList(),
    
    // ================================
    // GESTION D'ERREURS
    // ================================
    val error: String? = null,
    
    // ================================
    // HISTORIQUE ET SUGGESTIONS
    // ================================
    val searchHistory: List<String> = emptyList(),
    

    
    // ================================
    // ÉTAT DE L'UI
    // ================================
    val hasSearched: Boolean = false,
    val isSearchFieldFocused: Boolean = false
) {
    
    // ================================
    // PROPRIÉTÉS CALCULÉES POUR L'UI
    // ================================
    
    /**
     * Indique si l'écran peut effectuer une recherche
     */
    val canSearch: Boolean
        get() = searchQuery.trim().length >= 2 && !isLoading
    
    /**
     * Indique s'il y a des résultats à afficher selon le type sélectionné
     */
    val hasResults: Boolean
        get() = when (selectedSearchType) {
            SearchType.ARTISTS -> searchResults.isNotEmpty()
            SearchType.ALBUMS -> albumResults.isNotEmpty()
            SearchType.TRACKS -> trackResults.isNotEmpty()
        }
    
    /**
     * Indique s'il faut afficher l'historique de recherche
     */
    val showSearchHistory: Boolean
        get() = !hasSearched && searchQuery.isEmpty() && searchHistory.isNotEmpty()
    
    /**
     * Indique s'il faut afficher l'état vide
     */
    val showEmptyState: Boolean
        get() = !hasSearched && searchQuery.isEmpty() && searchHistory.isEmpty()
    
    /**
     * Message de statut selon l'état actuel
     */
    val statusMessage: String?
        get() = when {
            error != null -> error
            hasSearched && !hasResults && !isLoading -> 
                "Aucun ${selectedSearchType.displayName.lowercase()} trouvé"
            else -> null
        }
    
    /**
     * Nombre total de résultats selon le type sélectionné
     */
    val resultCount: Int
        get() = when (selectedSearchType) {
            SearchType.ARTISTS -> searchResults.size
            SearchType.ALBUMS -> albumResults.size
            SearchType.TRACKS -> trackResults.size
        }
    
    /**
     * Texte du bouton de recherche
     */
    val searchButtonText: String
        get() = if (isLoading) {
            "Recherche..."
        } else {
            "Rechercher ${selectedSearchType.displayName}"
        }
}

/**
 * Types de recherche disponibles
 * 
 * Énumération avec propriétés pour l'affichage
 */
enum class SearchType(
    val displayName: String,
    val pluralName: String,
    val icon: String
) {
    ARTISTS("Artiste", "Artistes", "🎤"),
    ALBUMS("Album", "Albums", "💿"),
    TRACKS("Piste", "Pistes", "🎵");
    
    /**
     * Retourne le type suivant dans l'ordre cyclique
     */
    fun next(): SearchType = when (this) {
        ARTISTS -> ALBUMS
        ALBUMS -> TRACKS
        TRACKS -> ARTISTS
    }
}

/**
 * Événements ponctuels pour les effets de bord
 * 
 * Sealed class pour une gestion type-safe des événements
 */
sealed class SearchEvent {
    data class NavigateToArtistDetails(val artistId: Long) : SearchEvent()
    data class NavigateToAlbumDetails(val albumId: Long) : SearchEvent()
    data class NavigateToTrackDetails(val trackId: Long) : SearchEvent()
    data class ShowToast(val message: String) : SearchEvent()
    data class ShareContent(val text: String) : SearchEvent()
    object HideKeyboard : SearchEvent()
    object HapticFeedback : SearchEvent()
}

/**
 * Actions utilisateur typées
 * 
 * Selon la règle : "Nommer les actions du ViewModel de manière claire et informative"
 */
sealed class SearchAction {
    data class UpdateSearchQuery(val query: String) : SearchAction()
    data class ChangeSearchType(val type: SearchType) : SearchAction()
    data class PerformSearch(val query: String) : SearchAction()
    data class SelectArtist(val artist: Artist) : SearchAction()
    data class SelectAlbum(val album: Album) : SearchAction()
    data class SelectTrack(val track: Track) : SearchAction()
    data class SelectHistoryItem(val query: String) : SearchAction()
    data class ShareArtist(val artist: Artist) : SearchAction()
    data class ShareAlbum(val album: Album) : SearchAction()
    data class ShareTrack(val track: Track) : SearchAction()
    data class UpdateSearchFieldFocus(val isFocused: Boolean) : SearchAction()
    object ClearSearchHistory : SearchAction()
    object ClearSearchQuery : SearchAction()
    object RetryLastSearch : SearchAction()
    object ToggleTheme : SearchAction()
} 