package org.diiage.projet_rattrapage.domain.model

// Imports supprimés pour éviter les problèmes d'API

/**
 * Modèle de données représentant un album musical dans la couche Domain
 * 
 * Cette classe respecte les principes SOLID :
 * - Single Responsibility : représente uniquement un album
 * - Open/Closed : extensible via héritage si nécessaire
 * - Interface Segregation : propriétés spécifiques aux albums
 * 
 * @property id Identifiant unique de l'album
 * @property title Titre de l'album
 * @property cover URL de la couverture de l'album
 * @property coverSmall URL de la petite couverture
 * @property coverMedium URL de la couverture moyenne
 * @property coverBig URL de la grande couverture
 * @property releaseDate Date de sortie de l'album
 * @property artist Artiste associé à cet album
 * @property numberOfTracks Nombre de pistes dans l'album
 * @property duration Durée totale de l'album en secondes
 * @property link Lien vers la page Deezer de l'album
 * 

 * @since 1.0
 */
data class Album(
    val id: Long,
    val title: String,
    val cover: String = "",
    val coverSmall: String = "",
    val coverMedium: String = "",
    val coverBig: String = "",
    val releaseDate: String = "",
    val artist: Artist? = null,
    val numberOfTracks: Int = 0,
    val duration: Int = 0,
    val link: String = ""
) {
    
    /**
     * Retourne l'URL de la couverture selon la taille demandée
     * Principe de polymorphisme appliqué via l'énumération ImageSize
     * 
     * @param imageSize Taille de l'image souhaitée
     * @return URL de la couverture correspondante
     */
    fun getCoverUrl(imageSize: ImageSize = ImageSize.MEDIUM): String {
        return when (imageSize) {
            ImageSize.SMALL -> coverSmall.ifEmpty { coverMedium.ifEmpty { cover } }
            ImageSize.MEDIUM -> coverMedium.ifEmpty { cover.ifEmpty { coverSmall } }
            ImageSize.BIG -> coverBig.ifEmpty { coverMedium.ifEmpty { cover } }
        }
    }
    
    /**
     * Formate la durée de l'album en format lisible
     * Exemple : 3661 secondes -> "1h 1m"
     * 
     * @return Durée formatée en heures et minutes
     */
    fun getFormattedDuration(): String {
        if (duration <= 0) return "Durée inconnue"
        
        val hours = duration / 3600
        val minutes = (duration % 3600) / 60
        
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            minutes > 0 -> "${minutes}m"
            else -> "< 1m"
        }
    }
    
    /**
     * Formate la date de sortie pour l'affichage
     * 
     * @return Date formatée ou "Date inconnue" si vide
     */
    fun getFormattedReleaseDate(): String {
        if (releaseDate.isEmpty()) return "Date inconnue"
        
        // Retourner la date brute pour éviter les problèmes d'API
        return releaseDate
    }
    

                              
    /**
     * Retourne le nom de l'artiste ou "Artiste inconnu"
     * Principe de défense en cas d'artiste null
     * 
     * @return Nom de l'artiste ou valeur par défaut
     */
    fun getArtistName(): String = artist?.name ?: "Artiste inconnu"
} 