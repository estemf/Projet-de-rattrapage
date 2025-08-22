package org.diiage.projet_rattrapage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.diiage.projet_rattrapage.domain.model.Artist

/**
 * DTO représentant un artiste dans les réponses de l'API Deezer
 * 
 * Cette classe applique le pattern Data Transfer Object (DTO) :
 * - Sépare la représentation API de la représentation métier
 * - Permet d'évoluer indépendamment l'API et le domaine
 * - Facilite la sérialisation/désérialisation
 * 
 * Les annotations @SerialName permettent de mapper les noms JSON
 * vers des noms Kotlin plus explicites
 * 
 * @property id Identifiant unique de l'artiste
 * @property name Nom de l'artiste
 * @property picture URL de l'image par défaut
 * @property pictureSmall URL de la petite image
 * @property pictureMedium URL de l'image moyenne
 * @property pictureBig URL de la grande image
 * @property pictureXl URL de l'image extra large
 * @property nbAlbum Nombre d'albums
 * @property nbFan Nombre de fans
 * @property radio Disponibilité radio
 * @property tracklist URL de la liste des pistes
 * @property type Type d'objet (toujours "artist")
 * 

 * @since 1.0
 */
@Serializable
data class ArtistDto(
    @SerialName("id")
    val id: Long,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("link")
    val link: String = "",
    
    @SerialName("picture")
    val picture: String = "",
    
    @SerialName("picture_small")
    val pictureSmall: String = "",
    
    @SerialName("picture_medium")
    val pictureMedium: String = "",
    
    @SerialName("picture_big")
    val pictureBig: String = "",
    
    @SerialName("picture_xl")
    val pictureXl: String = "",
    
    @SerialName("nb_album")
    val nbAlbum: Int = 0,
    
    @SerialName("nb_fan")
    val nbFan: Int = 0,
    
    @SerialName("radio")
    val radio: Boolean = false,
    
    @SerialName("tracklist")
    val tracklist: String = "",
    
    @SerialName("type")
    val type: String = ""
) {
    
    /**
     * Convertit ce DTO en modèle de domaine Artist
     * 
     * Cette méthode applique le pattern Mapper en transformant
     * les données de la couche Data vers la couche Domain
     * 
     * Transformations appliquées :
     * - Mappage des noms de propriétés
     * - Validation et nettoyage des données
     * - Application de valeurs par défaut appropriées
     * 
     * @return Instance du modèle Artist correspondant
     */
    fun toDomainModel(): Artist {
        return Artist(
            id = id,
            name = name.trim(),
            picture = picture,
            pictureSmall = pictureSmall,
            pictureMedium = pictureMedium,
            pictureBig = pictureXl.ifEmpty { pictureBig }, // Préférer XL à Big
            numberOfAlbums = maxOf(0, nbAlbum), // S'assurer que c'est positif
            numberOfFans = maxOf(0, nbFan), // S'assurer que c'est positif
            link = link
        )
    }
    
    /**
     * Valide si ce DTO contient des données minimales valides
     * 
     * @return true si l'artiste a au minimum un ID et un nom valides
     */
    fun isValid(): Boolean {
        return id > 0 && name.isNotBlank()
    }
}

/**
 * Fonctions d'extension pour les collections d'ArtistDto
 * 
 * Ces extensions appliquent les principes fonctionnels et facilitent
 * les transformations de collections
 */

/**
 * Convertit une liste d'ArtistDto en liste d'Artist du domaine
 * Filtre automatiquement les éléments invalides
 * 
 * @return Liste d'Artist valides
 */
fun List<ArtistDto>.toDomainModels(): List<Artist> {
    return this
        .filter { it.isValid() }
        .map { it.toDomainModel() }
}

/**
 * Filtre les artistes par validité
 * 
 * @return Liste des ArtistDto valides uniquement
 */
fun List<ArtistDto>.filterValid(): List<ArtistDto> {
    return this.filter { it.isValid() }
} 