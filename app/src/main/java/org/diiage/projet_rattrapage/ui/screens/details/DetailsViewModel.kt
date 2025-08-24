package org.diiage.projet_rattrapage.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.diiage.projet_rattrapage.domain.usecase.GetArtistDetailsUseCase
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import org.diiage.projet_rattrapage.data.hardware.AudioPlayer
import timber.log.Timber

/**
 * ViewModel pour l'écran de détails d'éléments musicaux
 * 
 * Cette classe applique l'architecture MVVM+ avec flux de données unidirectionnel (UDF) :
 * - Séparation claire entre UI et logique business
 * - Gestion d'état centralisée via StateFlow
 * - Intégration avec la couche Domain via Use Cases
 * - Logging complet pour debugging et monitoring
 * 
 * Design Patterns appliqués :
 * - Observer Pattern : StateFlow pour la réactivité UI
 * - Command Pattern : Actions via méthodes publiques
 * - Facade Pattern : Interface simplifiée pour l'UI
 * - Dependency Injection : Use Cases injectés
 * 
 * Compétences mises en œuvre :
 * - CRIT-DMA-D1-S-3 : Architecture MVVM+ UDF
 * - CRIT-DMA-D1-SE-3 : StateFlow bien formés
 * - DI1-GLO-C6-2 : Design patterns respectant SOLID
 * - DI1-GLO-C2-3 : Bonnes pratiques de développement
 * 
 * @param getArtistDetailsUseCase Use case pour récupérer les détails d'artiste
 * @param musicRepository Repository pour l'accès direct aux données musicales
 * 

 * @since 1.0
 */
class DetailsViewModel(
    private val getArtistDetailsUseCase: GetArtistDetailsUseCase,
    private val musicRepository: MusicRepository,
    audioPlayer: AudioPlayer
) : ViewModel() {
    
    /**
     * Lecteur audio pour les extraits de pistes
     */
    val audioPlayerInstance: AudioPlayer = audioPlayer
    
    // ================================
    // ÉTAT PRIVÉ ET PUBLIC
    // ================================
    
    /**
     * État interne mutable de l'interface utilisateur
     * 
     * Encapsulation : accès en écriture limité au ViewModel
     * selon le principe d'encapsulation de la POO
     */
    private val _uiState = MutableStateFlow(DetailsUiState())
    
    /**
     * État public en lecture seule pour l'UI
     * 
     * Applique le principe de l'interface segregation (SOLID)
     * en exposant uniquement les capacités de lecture
     */
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()
    

    
    // ================================
    // ACTIONS PUBLIQUES
    // ================================
    
    /**
     * Charge les détails d'un élément selon son type
     * 
     * Cette méthode applique le pattern Strategy en sélectionnant
     * la logique de chargement appropriée selon le type
     * 
     * @param detailsType Type d'élément à charger (ARTIST, ALBUM, TRACK)
     * @param itemId Identifiant de l'élément
     */
    fun loadDetails(detailsType: DetailsType, itemId: Long) {
        Timber.d("🎯 Chargement des détails - Type: $detailsType, ID: $itemId")
        
        if (itemId <= 0) {
            Timber.w("⚠️ ID invalide pour le chargement: $itemId")
            _uiState.value = DetailsUiState.error("Identifiant invalide")
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = DetailsUiState.loading()
                
                when (detailsType) {
                    DetailsType.ARTIST -> loadArtistDetails(itemId)
                    DetailsType.ALBUM -> loadAlbumDetails(itemId)
                    DetailsType.TRACK -> loadTrackDetails(itemId)
                }
                
            } catch (exception: Exception) {
                Timber.e(exception, "💥 Erreur lors du chargement des détails")
                handleError(exception)
            }
        }
    }
    

    
    // ================================
    // MÉTHODES PRIVÉES DE CHARGEMENT
    // ================================
    
    /**
     * Charge les détails d'un artiste
     * 
     * Utilise le Use Case dédié pour respecter l'architecture en couches
     * 
     * @param artistId Identifiant de l'artiste
     */
    private suspend fun loadArtistDetails(artistId: Long) {
        Timber.d("🎤 Chargement des détails de l'artiste ID: $artistId")
        
        try {
            val result = getArtistDetailsUseCase(artistId)
            result.fold(
                onSuccess = { artistDetails ->
                    _uiState.value = DetailsUiState.withArtist(artistDetails.artist)
                    Timber.i("✅ Détails de l'artiste '${artistDetails.artist.name}' chargés avec succès")
                },
                onFailure = { exception ->
                    Timber.e(exception, "💥 Erreur lors du chargement de l'artiste $artistId")
                    throw exception
                }
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Erreur lors du chargement de l'artiste $artistId")
            throw exception
        }
    }
    
    /**
     * Charge les détails d'un album depuis l'API Deezer
     * 
     * Utilise le repository pour récupérer les vraies données de l'album
     * 
     * @param albumId Identifiant de l'album
     */
    private suspend fun loadAlbumDetails(albumId: Long) {
        Timber.d("💿 Chargement des détails de l'album ID: $albumId")
        
        try {
            val result = musicRepository.getAlbumById(albumId)
            result.fold(
                onSuccess = { album ->
                    _uiState.value = DetailsUiState.withAlbum(album)
                    Timber.i("✅ Détails de l'album '${album.title}' chargés avec succès")
                },
                onFailure = { exception ->
                    Timber.e(exception, "💥 Erreur lors du chargement de l'album $albumId")
                    throw exception
                }
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Erreur lors du chargement de l'album $albumId")
            throw exception
        }
    }
    
    /**
     * Charge les détails d'une piste depuis l'API Deezer
     * 
     * Utilise le repository pour récupérer les vraies données de la piste
     * 
     * @param trackId Identifiant de la piste
     */
    private suspend fun loadTrackDetails(trackId: Long) {
        Timber.d("🎵 Chargement des détails de la piste ID: $trackId")
        
        try {
            val result = musicRepository.getTrackById(trackId)
            result.fold(
                onSuccess = { track ->
                    _uiState.value = DetailsUiState.withTrack(track)
                    Timber.i("✅ Détails de la piste '${track.title}' chargés avec succès")
                },
                onFailure = { exception ->
                    Timber.e(exception, "💥 Erreur lors du chargement de la piste $trackId")
                    throw exception
                }
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Erreur lors du chargement de la piste $trackId")
            throw exception
        }
    }
    
    // ================================
    // GESTION D'ERREURS
    // ================================
    
    /**
     * Gère les erreurs et met à jour l'état d'UI approprié
     * 
     * Applique le principe de responsabilité unique en centralisant
     * la logique de gestion d'erreurs
     * 
     * @param exception Exception à traiter
     */
    private fun handleError(exception: Exception) {
        val errorMessage = when (exception) {
            is java.net.UnknownHostException -> "Vérifiez votre connexion internet"
            is java.net.SocketTimeoutException -> "Le serveur met trop de temps à répondre"
            is retrofit2.HttpException -> {
                when (exception.code()) {
                    404 -> "Élément introuvable"
                    401 -> "Accès non autorisé"
                    500 -> "Erreur du serveur"
                    else -> "Erreur HTTP ${exception.code()}"
                }
            }
            else -> "Une erreur inattendue est survenue"
        }
        
        _uiState.value = DetailsUiState.error(errorMessage)
        Timber.e("🔴 Erreur traitée : $errorMessage")
    }
    
    // ================================
    // MÉTHODES UTILITAIRES
    // ================================
    

    
    override fun onCleared() {
        super.onCleared()
        Timber.d("🗑️ DetailsViewModel nettoyé")
    }
}
