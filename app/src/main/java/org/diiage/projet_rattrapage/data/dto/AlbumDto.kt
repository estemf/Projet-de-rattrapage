package org.diiage.projet_rattrapage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.diiage.projet_rattrapage.domain.model.Album

/**
 * DTO représentant un album dans les réponses de l'API Deezer
 * 
 * Cette classe suit les mêmes principes que ArtistDto :
 * - Séparation claire entre couche Data et Domain
 * - Annotations de sérialisation explicites
 * - Méthodes de transformation et validation
 * 
 * @property id Identifiant unique de l'album
 * @property title Titre de l'album
 * @property cover URL de la couverture par défaut
 * @property coverSmall URL de la petite couverture
 * @property coverMedium URL de la couverture moyenne
 * @property coverBig URL de la grande couverture
 * @property coverXl URL de la couverture extra large
 * @property releaseDate Date de sortie (format YYYY-MM-DD)
 * @property artist Artiste associé (peut être null selon l'endpoint)
 * @property nbTracks Nombre de pistes dans l'album
 * @property duration Durée totale en secondes
 * @property fans Nombre de fans de cet album
 * @property link Lien vers la page Deezer de l'album
 * @property tracklist URL de la liste des pistes
 * @property explicit Contenu explicite
 * @property type Type d'objet (toujours "album")
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
@Serializable
data class AlbumDto(
    @SerialName("id")
    val id: Long,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("cover")
    val cover: String = "",
    
    @SerialName("cover_small")
    val coverSmall: String = "",
    
    @SerialName("cover_medium")
    val coverMedium: String = "",
    
    @SerialName("cover_big")
    val coverBig: String = "",
    
    @SerialName("cover_xl")
    val coverXl: String = "",
    
    @SerialName("release_date")
    val releaseDate: String = "",
    
    @SerialName("artist")
    val artist: ArtistDto? = null,
    
    @SerialName("nb_tracks")
    val nbTracks: Int = 0,
    
    @SerialName("duration")
    val duration: Int = 0,
    
    @SerialName("fans")
    val fans: Int = 0,
    
    @SerialName("link")
    val link: String = "",
    
    @SerialName("tracklist")
    val tracklist: String = "",
    
    @SerialName("explicit_lyrics")
    val explicit: Boolean = false,
    
    @SerialName("type")
    val type: String = ""
) {
    
    /**
     * Convertit ce DTO en modèle de domaine Album
     * 
     * Applique les transformations nécessaires :
     * - Validation des données numériques
     * - Conversion de l'artiste si présent
     * - Nettoyage des chaînes de caractères
     * 
     * @return Instance du modèle Album correspondant
     */
    fun toDomainModel(): Album {
        return Album(
            id = id,
            title = title.trim(),
            cover = cover,
            coverSmall = coverSmall,
            coverMedium = coverMedium,
            coverBig = coverXl.ifEmpty { coverBig }, // Préférer XL
            releaseDate = releaseDate,
            artist = artist?.toDomainModel(), // Conversion conditionnelle
            numberOfTracks = maxOf(0, nbTracks),
            duration = maxOf(0, duration),
            link = link
        )
    }
    
    /**
     * Valide si ce DTO contient des données minimales valides
     * 
     * Un album est considéré valide s'il a :
     * - Un ID positif
     * - Un titre non vide
     * 
     * @return true si l'album est valide
     */
    fun isValid(): Boolean {
        return id > 0 && title.isNotBlank()
    }
    
    /**
     * Retourne l'URL de couverture la plus appropriée
     * 
     * @return URL de la meilleure couverture disponible
     */
    fun getBestCoverUrl(): String {
        return coverXl.ifEmpty { 
            coverBig.ifEmpty { 
                coverMedium.ifEmpty { 
                    coverSmall.ifEmpty { cover } 
                } 
            } 
        }
    }
}

/**
 * Extensions pour les collections d'AlbumDto
 */

/**
 * Convertit une liste d'AlbumDto en liste d'Album du domaine
 * 
 * @return Liste d'Album valides
 */
fun List<AlbumDto>.toDomainModels(): List<Album> {
    return this
        .filter { it.isValid() }
        .map { it.toDomainModel() }
}

/**
 * Filtre les albums par validité et trie par date de sortie (plus récent en premier)
 * 
 * @return Liste des AlbumDto valides triés par date
 */
fun List<AlbumDto>.filterValidAndSort(): List<AlbumDto> {
    return this
        .filter { it.isValid() }
        .sortedByDescending { it.releaseDate }
} 