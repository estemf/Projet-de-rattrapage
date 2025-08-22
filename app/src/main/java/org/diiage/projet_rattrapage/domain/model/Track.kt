package org.diiage.projet_rattrapage.domain.model

import java.util.Locale

/**
 * Modèle de données représentant une piste musicale dans la couche Domain
 * 
 * Cette classe illustre l'héritage conceptuel des propriétés communes
 * avec les albums (durée, artiste) tout en maintenant sa spécificité
 * 
 * Principes POO appliqués :
 * - Encapsulation : propriétés immutables avec accesseurs contrôlés
 * - Abstraction : représentation simplifiée d'une piste
 * - Composition : relation avec Artist et Album
 * 
 * @property id Identifiant unique de la piste
 * @property title Titre de la piste
 * @property duration Durée de la piste en secondes
 * @property artist Artiste de la piste
 * @property album Album contenant cette piste
 * @property trackPosition Position de la piste dans l'album
 * @property preview URL d'aperçu de 30 secondes
 * @property link Lien vers la page Deezer de la piste
 * @property explicit Indique si la piste contient du contenu explicite
 * 

 * @since 1.0
 */
data class Track(
    val id: Long,
    val title: String,
    val duration: Int = 0,
    val artist: Artist? = null,
    val album: Album? = null,
    val trackPosition: Int = 0,
    val preview: String = "",
    val link: String = "",
    val explicit: Boolean = false
) {
    
    /**
     * Formate la durée de la piste en format MM:SS
     * Exemple : 185 secondes -> "3:05"
     * 
     * Cette méthode applique le principe de responsabilité unique
     * en gérant le formatage au niveau du modèle
     * 
     * @return Durée formatée en minutes:secondes
     */
    fun getFormattedDuration(): String {
        if (duration <= 0) return "--:--"
        
        val minutes = duration / 60
        val seconds = duration % 60
        
        return String.format(Locale.ROOT, "%d:%02d", minutes, seconds)
    }
    
    /**
     * Retourne le nom de l'artiste avec gestion des cas null
     * Principe de défense programmative
     * 
     * @return Nom de l'artiste ou "Artiste inconnu"
     */
    fun getArtistName(): String = artist?.name ?: "Artiste inconnu"
    
    /**
     * Retourne le titre de l'album avec gestion des cas null
     * 
     * @return Titre de l'album ou "Album inconnu"
     */
    fun getAlbumTitle(): String = album?.title ?: "Album inconnu"
    
    /**
     * Indique si la piste a un aperçu disponible
     * 
     * @return true si un URL d'aperçu est disponible
     */
    fun hasPreview(): Boolean = preview.isNotEmpty()
    
    /**
     * Retourne l'URL de la couverture de l'album associé
     * Délégation vers l'album pour respecter le principe DRY
     * 
     * @param imageSize Taille de l'image souhaitée
     * @return URL de la couverture ou chaîne vide si pas d'album
     */
    fun getCoverUrl(imageSize: ImageSize = ImageSize.MEDIUM): String {
        return album?.getCoverUrl(imageSize) ?: ""
    }
    
    /**
     * Formate les informations complètes de la piste
     * Utile pour l'affichage et le logging
     * 
     * @return Chaîne formatée avec artiste - titre (durée)
     */
    fun getDisplayInfo(): String {
        val artistName = getArtistName()
        val formattedDuration = getFormattedDuration()
        val explicitTag = if (explicit) " [E]" else ""
        
        return "$artistName - $title ($formattedDuration)$explicitTag"
    }
    
    /**
     * Indique si cette piste fait partie d'un album
     * 
     * @return true si un album est associé à cette piste
     */
    fun isPartOfAlbum(): Boolean = album != null
    
    /**
     * Indique si cette piste a une couverture disponible via son album
     * Délègue la logique à l'album associé
     * 
     * @return true si l'album a une couverture disponible
     */
    fun hasCover(): Boolean = album?.hasCover() ?: false
} 