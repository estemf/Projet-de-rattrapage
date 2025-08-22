package org.diiage.projet_rattrapage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.diiage.projet_rattrapage.domain.model.Track

/**
 * DTO représentant une piste musicale dans les réponses de l'API Deezer
 * 
 * Cette classe complète la famille des DTOs avec la même approche :
 * - Mapping précis des champs API
 * - Validation et transformation vers le domaine
 * - Gestion des relations avec Artist et Album
 * 
 * @property id Identifiant unique de la piste
 * @property title Titre de la piste
 * @property titleShort Version courte du titre
 * @property titleVersion Version du titre (remix, live, etc.)
 * @property duration Durée de la piste en secondes
 * @property rank Popularité de la piste (0-1000000)
 * @property explicit Contenu explicite
 * @property preview URL d'aperçu de 30 secondes
 * @property artist Artiste de la piste
 * @property album Album contenant cette piste
 * @property trackPosition Position dans l'album
 * @property diskNumber Numéro du disque (pour les albums multiples)
 * @property link Lien vers la page Deezer de la piste
 * @property type Type d'objet (toujours "track")
 * 

 * @since 1.0
 */
@Serializable
data class TrackDto(
    @SerialName("id")
    val id: Long,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("title_short")
    val titleShort: String = "",
    
    @SerialName("title_version")
    val titleVersion: String = "",
    
    @SerialName("duration")
    val duration: Int = 0,
    
    @SerialName("rank")
    val rank: Int = 0,
    
    @SerialName("explicit_lyrics")
    val explicit: Boolean = false,
    
    @SerialName("preview")
    val preview: String = "",
    
    @SerialName("artist")
    val artist: ArtistDto? = null,
    
    @SerialName("album")
    val album: AlbumDto? = null,
    
    @SerialName("track_position")
    val trackPosition: Int = 0,
    
    @SerialName("disk_number")
    val diskNumber: Int = 1,
    
    @SerialName("link")
    val link: String = "",
    
    @SerialName("type")
    val type: String = ""
) {
    
    /**
     * Convertit ce DTO en modèle de domaine Track
     * 
     * Transformations appliquées :
     * - Nettoyage et validation du titre
     * - Conversion des objets liés (artist, album)
     * - Validation des données numériques
     * - Gestion des valeurs par défaut
     * 
     * @return Instance du modèle Track correspondant
     */
    fun toDomainModel(): Track {
        return Track(
            id = id,
            title = getCleanTitle(),
            duration = maxOf(0, duration),
            artist = artist?.toDomainModel(),
            album = album?.toDomainModel(),
            trackPosition = maxOf(0, trackPosition),
            preview = preview,
            link = link,
            explicit = explicit
        )
    }
    
    /**
     * Retourne le titre le plus approprié pour l'affichage
     * 
     * Logique de sélection :
     * 1. Titre complet si différent du titre court
     * 2. Titre court sinon
     * 3. Nettoyage des espaces en trop
     * 
     * @return Titre nettoyé et formaté
     */
    private fun getCleanTitle(): String {
        val mainTitle = if (title.isNotBlank() && title != titleShort) {
            title
        } else {
            titleShort.ifEmpty { title }
        }
        
        return mainTitle.trim()
    }
    
    /**
     * Valide si ce DTO contient des données minimales valides
     * 
     * Une piste est considérée valide si elle a :
     * - Un ID positif
     * - Un titre non vide
     * - Une durée positive (optionnel, certains aperçus n'ont pas de durée)
     * 
     * @return true si la piste est valide
     */
    fun isValid(): Boolean {
        return id > 0 && (title.isNotBlank() || titleShort.isNotBlank())
    }
    
    /**
     * Indique si cette piste a un aperçu audio disponible
     * 
     * @return true si un URL d'aperçu est disponible
     */
    fun hasPreview(): Boolean {
        return preview.isNotBlank() && preview.startsWith("http")
    }
    
    /**
     * Calcule le score de popularité normalisé
     * 
     * Le rank Deezer va de 0 à 1000000, on le normalise sur 100
     * 
     * @return Score de popularité entre 0 et 100
     */
    fun getPopularityScore(): Int {
        return (rank / 10000.0).toInt().coerceIn(0, 100)
    }
    
    /**
     * Retourne le titre complet avec version si applicable
     * 
     * @return Titre formaté avec version (ex: "Song (Remix)")
     */
    fun getFullTitle(): String {
        val baseTitle = getCleanTitle()
        return if (titleVersion.isNotBlank() && !baseTitle.contains(titleVersion)) {
            "$baseTitle ($titleVersion)"
        } else {
            baseTitle
        }
    }
}

/**
 * Extensions pour les collections de TrackDto
 */

/**
 * Convertit une liste de TrackDto en liste de Track du domaine
 * 
 * @return Liste de Track valides
 */
fun List<TrackDto>.toDomainModels(): List<Track> {
    return this
        .filter { it.isValid() }
        .map { it.toDomainModel() }
}

/**
 * Filtre les pistes valides et trie par popularité (rank décroissant)
 * 
 * @return Liste des TrackDto valides triées par popularité
 */
fun List<TrackDto>.filterValidAndSortByPopularity(): List<TrackDto> {
    return this
        .filter { it.isValid() }
        .sortedByDescending { it.rank }
}

/**
 * Filtre uniquement les pistes ayant un aperçu audio
 * 
 * @return Liste des TrackDto avec aperçu disponible
 */
fun List<TrackDto>.filterWithPreview(): List<TrackDto> {
    return this.filter { it.hasPreview() }
} 