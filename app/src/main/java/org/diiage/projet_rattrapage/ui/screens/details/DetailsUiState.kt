package org.diiage.projet_rattrapage.ui.screens.details

import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Track

/**
 * État de l'interface utilisateur pour l'écran de détails
 * 
 * Cette data class applique les principes de l'architecture MVVM+ UDF :
 * - Immutabilité des données d'état
 * - Représentation claire de tous les états possibles
 * - Séparation entre données et logique de présentation
 * - Support de la programmation réactive avec StateFlow
 * 
 * Compétences mises en œuvre :
 * - CRIT-DMA-D1-S-3 : Gestion des États avec Compose
 * - CRIT-DMA-D1-SE-3 : StateFlow bien formés
 * - DI1-GLO-C4-1 : Concepts fondamentaux de la POO (encapsulation)
 * 
 * @property isLoading Indicateur de chargement des données
 * @property error Message d'erreur optionnel
 * @property artist Données de l'artiste (si type ARTIST)
 * @property album Données de l'album (si type ALBUM)  
 * @property track Données de la piste (si type TRACK)
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
data class DetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val artist: Artist? = null,
    val album: Album? = null,
    val track: Track? = null
) {
    
    /**
     * Indique si des données sont actuellement disponibles
     * 
     * Cette méthode applique le principe de responsabilité unique
     * en centralisant la logique de vérification des données
     * 
     * @return true si au moins un type de données est présent
     */
    fun hasData(): Boolean = artist != null || album != null || track != null
    
    /**
     * Indique si l'état est en erreur
     * 
     * @return true si une erreur est présente
     */
    fun isInError(): Boolean = error != null
    
    /**
     * Retourne le titre principal selon le type de contenu
     * 
     * Applique le principe de polymorphisme conceptuel
     * en adaptant l'affichage selon le type de données
     * 
     * @return Titre principal ou chaîne vide si pas de données
     */
    fun getMainTitle(): String = when {
        artist != null -> artist.name
        album != null -> album.title
        track != null -> track.title
        else -> ""
    }
    
    /**
     * Retourne le sous-titre selon le type de contenu
     * 
     * @return Sous-titre contextuel ou chaîne vide
     */
    fun getSubtitle(): String = when {
        album != null -> album.getArtistName()
        track != null -> "${track.getArtistName()} • ${track.getAlbumTitle()}"
        else -> ""
    }
    
    /**
     * Retourne l'URL de l'image principale selon le type de contenu
     * 
     * Délégation vers les méthodes appropriées des modèles
     * 
     * @return URL de l'image ou chaîne vide si indisponible
     */
    fun getImageUrl(): String = when {
        artist != null -> artist.getImageUrl()
        album != null -> album.getCoverUrl()
        track != null -> track.getCoverUrl()
        else -> ""
    }
    
    /**
     * Indique si une image est disponible pour l'affichage
     * 
     * @return true si une URL d'image est disponible
     */
    fun hasImage(): Boolean = getImageUrl().isNotEmpty()
    

    

    

    

    

    
    companion object {
        
        /**
         * État initial avec indicateur de chargement
         * 
         * Factory method appliquant le pattern de création
         * pour un état cohérent au démarrage
         * 
         * @return État initial configuré pour le chargement
         */
        fun loading(): DetailsUiState = DetailsUiState(isLoading = true)
        
        /**
         * État d'erreur avec message
         * 
         * @param errorMessage Message d'erreur à afficher
         * @return État configuré pour l'affichage d'erreur
         */
        fun error(errorMessage: String): DetailsUiState = DetailsUiState(
            isLoading = false,
            error = errorMessage
        )
        
        /**
         * État avec données d'artiste
         * 
         * @param artist Données de l'artiste à afficher
         * @return État configuré pour l'affichage d'artiste
         */
        fun withArtist(artist: Artist): DetailsUiState = DetailsUiState(
            isLoading = false,
            artist = artist
        )
        
        /**
         * État avec données d'album
         * 
         * @param album Données de l'album à afficher
         * @return État configuré pour l'affichage d'album
         */
        fun withAlbum(album: Album): DetailsUiState = DetailsUiState(
            isLoading = false,
            album = album
        )
        
        /**
         * État avec données de piste
         * 
         * @param track Données de la piste à afficher
         * @return État configuré pour l'affichage de piste
         */
        fun withTrack(track: Track): DetailsUiState = DetailsUiState(
            isLoading = false,
            track = track
        )
    }
}
