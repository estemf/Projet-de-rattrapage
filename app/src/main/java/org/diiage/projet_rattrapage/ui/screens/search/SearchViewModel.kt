package org.diiage.projet_rattrapage.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import org.diiage.projet_rattrapage.data.hardware.AudioManager
import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track
import org.diiage.projet_rattrapage.domain.usecase.SearchArtistsUseCase
import org.diiage.projet_rattrapage.domain.usecase.SearchAlbumsUseCase
import org.diiage.projet_rattrapage.domain.usecase.SearchTracksUseCase
import org.diiage.projet_rattrapage.ui.theme.ThemeManager

import org.diiage.projet_rattrapage.utils.launchSafely
import org.diiage.projet_rattrapage.utils.logEvent
import org.diiage.projet_rattrapage.utils.logViewModelState
import timber.log.Timber

/**
 * ViewModel pour l'écran de recherche d'artistes
 * 
 * Cette classe applique parfaitement les règles du projet :
 * - "Nommer les actions du ViewModel de manière claire et informative"
 * - "Former les StateFlow avec des informations relatives à la représentation visuelle"
 * - "Utiliser la programmation réactive via les StateFlow"
 * - "Chaque composant d'écran doit avoir un rôle spécifique sans logique étrangère"
 * 
 * Responsabilités :
 * - Gestion de l'état de recherche
 * - Coordination des Use Cases

 * - Gestion des événements UI
 * - Logging et feedback haptique
 * 
 * Architecture MVVM + MVI :
 * - StateFlow pour l'état UI
 * - Actions typées pour les interactions
 * - Events pour les effets de bord
 * 
 * @property searchArtistsUseCase Use case pour la recherche d'artistes

 * @property audioManager Manager audio hardware
 * @property audioManager Manager audio pour feedback haptique
 * 

 * @since 1.0
 */
class SearchViewModel(
    private val searchArtistsUseCase: SearchArtistsUseCase,
    private val searchAlbumsUseCase: SearchAlbumsUseCase,
    private val searchTracksUseCase: SearchTracksUseCase,
    private val audioManager: AudioManager
) : ViewModel() {
    
    // ================================
    // STATEFLOW BIEN FORMÉS POUR L'UI
    // ================================
    
    /**
     * État UI principal - StateFlow avec informations de représentation visuelle
     * 
     * Selon la règle : "Former les StateFlow avec des informations relatives 
     * à la représentation visuelle"
     */
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    /**
     * Événements ponctuels pour les effets de bord
     * 
     * SharedFlow pour les actions non-persistantes comme la navigation
     */
    private val _events = MutableSharedFlow<SearchEvent>()
    val events: SharedFlow<SearchEvent> = _events.asSharedFlow()
    
    // ================================
    // HISTORIQUE DE RECHERCHE LOCAL
    // ================================
    
    /**
     * Historique des recherches récentes (max 10)
     * Persisté localement pour améliorer l'UX
     */
    private val searchHistory = mutableListOf<String>()
    
    // ================================
    // INITIALISATION ET SURVEILLANCE
    // ================================
    
    init {
        Timber.i("🔍 SearchViewModel initialisé")
        logViewModelState("INIT")
        

        
        // Log initial de l'état
        logViewModelState("READY", mapOf(
            "can_search" to _uiState.value.canSearch
        ))
    }
    

    
    // ================================
    // ACTIONS CLAIRES ET INFORMATIVES
    // ================================
    
    /**
     * Point d'entrée unique pour toutes les actions utilisateur
     * 
     * Selon la règle : "Nommer les actions du ViewModel de manière claire et informative"
     * 
     * @param action Action utilisateur à traiter
     */
    fun handleAction(action: SearchAction) {
        when (action) {
            is SearchAction.UpdateSearchQuery -> updateSearchQueryAction(action.query)
            is SearchAction.ChangeSearchType -> changeSearchTypeAction(action.type)
            is SearchAction.PerformSearch -> performSearchAction(action.query)
            is SearchAction.SelectArtist -> selectArtistAction(action.artist)
            is SearchAction.SelectAlbum -> selectAlbumAction(action.album)
            is SearchAction.SelectTrack -> selectTrackAction(action.track)
            is SearchAction.SelectHistoryItem -> selectHistoryItemAction(action.query)
            is SearchAction.ClearSearchHistory -> clearSearchHistoryAction()
            is SearchAction.ClearSearchQuery -> clearSearchQueryAction()
            is SearchAction.UpdateSearchFieldFocus -> updateSearchFieldFocusAction(action.isFocused)

            is SearchAction.RetryLastSearch -> retryLastSearchAction()
            is SearchAction.ShareArtist -> shareArtistAction(action.artist)
            is SearchAction.ShareAlbum -> shareAlbumAction(action.album)
            is SearchAction.ShareTrack -> shareTrackAction(action.track)
            is SearchAction.ToggleTheme -> toggleThemeAction()
        }
    }
    
    /**
     * Action : Mise à jour de la requête de recherche
     * 
     * Met à jour la requête en temps réel avec validation
     */
    private fun updateSearchQueryAction(query: String) {
        logEvent("UPDATE_SEARCH_QUERY", mapOf("query_length" to query.length))
        
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            error = null // Efface l'erreur lors de la saisie
        )
        
        // Feedback haptique léger lors de la saisie
        if (query.length == 2) { // Dès que la requête devient valide
            audioManager.performLightHapticFeedback()
        }
    }
    
    /**
     * Action : Exécution d'une recherche d'artistes
     * 
     * Lance la recherche avec gestion d'erreurs et logging
     */
    private fun performSearchAction(query: String) {
        val cleanQuery = query.trim()
        
        logEvent("PERFORM_SEARCH", mapOf(
            "query" to cleanQuery,
            "query_length" to cleanQuery.length
        ))
        
        // Validation préalable
        if (!searchArtistsUseCase.isValidQuery(cleanQuery)) {
            Timber.w("⚠️ Requête invalide pour la recherche: '$cleanQuery'")
            _events.tryEmit(SearchEvent.ShowToast("Saisissez au moins 2 caractères"))
            return
        }
        

        
        // Mise à jour de l'état : début de recherche
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null,
            hasSearched = true
        )
        
        // Feedback haptique et masquage du clavier
        audioManager.performMediumHapticFeedback()
        _events.tryEmit(SearchEvent.HideKeyboard)
        
        // Exécution asynchrone de la recherche selon le type
        launchSafely(
            onError = { error ->
                handleSearchError(error, cleanQuery)
            }
        ) {
            when (_uiState.value.selectedSearchType) {
                SearchType.ARTISTS -> {
                    val result = searchArtistsUseCase(cleanQuery)
                    result.fold(
                        onSuccess = { artists ->
                            handleArtistsSearchSuccess(artists, cleanQuery)
                        },
                        onFailure = { error ->
                            handleSearchError(error, cleanQuery)
                        }
                    )
                }
                SearchType.ALBUMS -> {
                    val result = searchAlbumsUseCase(cleanQuery)
                    result.fold(
                        onSuccess = { albums ->
                            handleAlbumsSearchSuccess(albums, cleanQuery)
                        },
                        onFailure = { error ->
                            handleSearchError(error, cleanQuery)
                        }
                    )
                }
                SearchType.TRACKS -> {
                    val result = searchTracksUseCase(cleanQuery)
                    result.fold(
                        onSuccess = { tracks ->
                            handleTracksSearchSuccess(tracks, cleanQuery)
                        },
                        onFailure = { error ->
                            handleSearchError(error, cleanQuery)
                        }
                    )
                }
            }
        }
    }
    
    /**
     * Gère le succès d'une recherche d'artistes
     */
    private fun handleArtistsSearchSuccess(artists: List<Artist>, query: String) {
        logEvent("SEARCH_SUCCESS", mapOf(
            "results_count" to artists.size,
            "query" to query
        ))
        
        // Mise à jour de l'état avec les résultats
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            searchResults = artists,
            error = null
        )
        
        // Ajout à l'historique si des résultats trouvés
        if (artists.isNotEmpty()) {
            addToSearchHistory(query)
            audioManager.performLightHapticFeedback() // Feedback de succès
        }
        
        // Notification du résultat
        val message = if (artists.isEmpty()) {
            "Aucun artiste trouvé pour \"$query\""
        } else {
            "${artists.size} artiste(s) trouvé(s)"
        }
        _events.tryEmit(SearchEvent.ShowToast(message))
    }
    
    /**
     * Gère le succès d'une recherche d'albums
     */
    private fun handleAlbumsSearchSuccess(albums: List<Album>, query: String) {
        logEvent("ALBUMS_SEARCH_SUCCESS", mapOf(
            "results_count" to albums.size,
            "query" to query
        ))
        
        // Mise à jour de l'état avec les résultats
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            albumResults = albums,
            error = null
        )
        
        // Ajout à l'historique si des résultats trouvés
        if (albums.isNotEmpty()) {
            addToSearchHistory(query)
            audioManager.performLightHapticFeedback() // Feedback de succès
        }
        
        // Notification du résultat
        val message = if (albums.isEmpty()) {
            "Aucun album trouvé pour \"$query\""
        } else {
            "${albums.size} album(s) trouvé(s)"
        }
        _events.tryEmit(SearchEvent.ShowToast(message))
    }
    
    /**
     * Gère le succès d'une recherche de pistes
     */
    private fun handleTracksSearchSuccess(tracks: List<Track>, query: String) {
        logEvent("TRACKS_SEARCH_SUCCESS", mapOf(
            "results_count" to tracks.size,
            "query" to query
        ))
        
        // Mise à jour de l'état avec les résultats
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            trackResults = tracks,
            error = null
        )
        
        // Ajout à l'historique si des résultats trouvés
        if (tracks.isNotEmpty()) {
            addToSearchHistory(query)
            audioManager.performLightHapticFeedback() // Feedback de succès
        }
        
        // Notification du résultat
        val message = if (tracks.isEmpty()) {
            "Aucune piste trouvée pour \"$query\""
        } else {
            "${tracks.size} piste(s) trouvée(s)"
        }
        _events.tryEmit(SearchEvent.ShowToast(message))
    }
    
    /**
     * Gère les erreurs de recherche
     */
    private fun handleSearchError(error: Throwable, query: String) {
        logEvent("SEARCH_ERROR", mapOf<String, Any>(
            "error_type" to (error::class.simpleName ?: "Unknown"),
            "query" to query
        ))
        
        Timber.e(error, "❌ Erreur lors de la recherche pour '$query'")
        
        // Mise à jour de l'état avec l'erreur
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = "Impossible de rechercher \"$query\". Vérifiez votre connexion."
        )
        
        // Feedback d'erreur
        audioManager.performHeavyHapticFeedback()
        _events.tryEmit(SearchEvent.ShowToast("Erreur de recherche"))
    }
    
    /**
     * Action : Sélection d'un artiste dans les résultats
     */
    private fun selectArtistAction(artist: Artist) {
        println("🎯 DEBUG: SelectArtistAction appelée pour ${artist.name} (ID: ${artist.id})")
        
        logEvent("SELECT_ARTIST", mapOf<String, Any>(
            "artist_id" to artist.id,
            "artist_name" to artist.name
        ))
        
        // Feedback haptique et navigation
        audioManager.performLightHapticFeedback()
        
        // Utilisation de viewModelScope.launch pour émission garantie
        viewModelScope.launch {
            println("🎯 DEBUG: Émission de l'événement NavigateToArtistDetails...")
            _events.emit(SearchEvent.NavigateToArtistDetails(artist.id))
            println("🎯 DEBUG: Événement NavigateToArtistDetails émis avec succès")
        }
    }
    
    /**
     * Action : Sélection d'un élément de l'historique
     */
    private fun selectHistoryItemAction(query: String) {
        logEvent("SELECT_HISTORY_ITEM", mapOf<String, Any>("query" to query))
        
        // Met à jour la requête et lance la recherche
        _uiState.value = _uiState.value.copy(searchQuery = query)
        performSearchAction(query)
    }
    
    /**
     * Action : Effacement de l'historique de recherche
     */
    private fun clearSearchHistoryAction() {
        logEvent("CLEAR_SEARCH_HISTORY", mapOf<String, Any>("history_size" to searchHistory.size))
        
        searchHistory.clear()
        _uiState.value = _uiState.value.copy(searchHistory = searchHistory.toList())
        
        audioManager.performLightHapticFeedback()
        _events.tryEmit(SearchEvent.ShowToast("Historique effacé"))
    }
    
    /**
     * Action : Effacement de la requête actuelle
     */
    private fun clearSearchQueryAction() {
        logEvent("CLEAR_SEARCH_QUERY")
        
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            error = null
        )
        
        audioManager.performLightHapticFeedback()
    }
    
    /**
     * Action : Mise à jour du focus du champ de recherche
     */
    private fun updateSearchFieldFocusAction(isFocused: Boolean) {
        _uiState.value = _uiState.value.copy(isSearchFieldFocused = isFocused)
    }
    

    
    /**
     * Action : Nouvelle tentative de la dernière recherche
     */
    private fun retryLastSearchAction() {
        val query = _uiState.value.searchQuery
        if (query.isNotEmpty()) {
            logEvent("RETRY_LAST_SEARCH", mapOf("query" to query))
            performSearchAction(query)
        }
    }
    
    /**
     * Action : Partage d'un artiste
     */
    private fun shareArtistAction(artist: Artist) {
        logEvent("SHARE_ARTIST", mapOf(
            "artist_id" to artist.id,
            "artist_name" to artist.name
        ))
        
        val shareText = "Découvre ${artist.name} sur Deezer !\n${artist.link}"
        _events.tryEmit(SearchEvent.ShareContent(shareText))
        
        audioManager.performLightHapticFeedback()
    }
    
    // ================================
    // GESTION DE L'HISTORIQUE
    // ================================
    
    /**
     * Ajoute une requête à l'historique de recherche
     * 
     * Maintient un historique limité à 10 éléments
     */
    private fun addToSearchHistory(query: String) {
        if (query.isBlank()) return
        
        // Supprime l'ancienne occurrence si elle existe
        searchHistory.remove(query)
        
        // Ajoute au début
        searchHistory.add(0, query)
        
        // Limite à 10 éléments
        if (searchHistory.size > 10) {
            searchHistory.removeAt(searchHistory.size - 1)
        }
        
        // Met à jour l'état UI
        _uiState.value = _uiState.value.copy(searchHistory = searchHistory.toList())
        
        Timber.d("📚 Historique mis à jour: ${searchHistory.size} éléments")
    }
    
    /**
     * Action : Changement du type de recherche
     */
    private fun changeSearchTypeAction(type: SearchType) {
        logEvent("CHANGE_SEARCH_TYPE", mapOf("new_type" to type.name))
        
        _uiState.value = _uiState.value.copy(selectedSearchType = type)
        audioManager.performLightHapticFeedback()
    }
    
    /**
     * Action : Sélection d'un album dans les résultats
     */
    private fun selectAlbumAction(album: Album) {
        logEvent("SELECT_ALBUM", mapOf(
            "album_id" to album.id,
            "album_title" to album.title
        ))
        
        audioManager.performLightHapticFeedback()
        
        // Utilisation de viewModelScope.launch pour émission garantie
        viewModelScope.launch {
            _events.emit(SearchEvent.NavigateToAlbumDetails(album.id))
        }
    }
    
    /**
     * Action : Sélection d'une piste dans les résultats
     */
    private fun selectTrackAction(track: Track) {
        logEvent("SELECT_TRACK", mapOf(
            "track_id" to track.id,
            "track_title" to track.title
        ))
        
        audioManager.performLightHapticFeedback()
        
        // Utilisation de viewModelScope.launch pour émission garantie
        viewModelScope.launch {
            _events.emit(SearchEvent.NavigateToTrackDetails(track.id))
        }
    }
    
    /**
     * Action : Partage d'un album
     */
    private fun shareAlbumAction(album: Album) {
        logEvent("SHARE_ALBUM", mapOf(
            "album_id" to album.id,
            "album_title" to album.title
        ))
        
        val shareText = "Découvre l'album ${album.title} de ${album.getArtistName()} sur Deezer !\n${album.link}"
        _events.tryEmit(SearchEvent.ShareContent(shareText))
        
        audioManager.performLightHapticFeedback()
    }
    
    /**
     * Action : Partage d'une piste
     */
    private fun shareTrackAction(track: Track) {
        logEvent("SHARE_TRACK", mapOf(
            "track_id" to track.id,
            "track_title" to track.title
        ))
        
        val shareText = "Écoute ${track.title} de ${track.getArtistName()} sur Deezer !\n${track.link}"
        _events.tryEmit(SearchEvent.ShareContent(shareText))
        
        audioManager.performLightHapticFeedback()
    }
    
    /**
     * Action : Basculer le thème
     */
    private fun toggleThemeAction() {
        logEvent("TOGGLE_THEME")
        
        ThemeManager.toggleTheme()
        val newTheme = ThemeManager.getCurrentThemeDescription()
        
        audioManager.performMediumHapticFeedback()
        _events.tryEmit(SearchEvent.ShowToast("Thème changé : $newTheme"))
    }

    // ================================
    // NETTOYAGE DES RESSOURCES
    // ================================
    
    override fun onCleared() {
        super.onCleared()
        
        logViewModelState("CLEARED", mapOf(
            "search_history_size" to searchHistory.size,
            "last_query" to _uiState.value.searchQuery
        ))
        
        Timber.i("🧹 SearchViewModel nettoyé")
    }
} 