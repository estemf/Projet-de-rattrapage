package org.diiage.projet_rattrapage.domain.model

/**
 * Modèle de données représentant un artiste musical dans la couche Domain
 * 
 * Cette classe encapsule toutes les informations relatives à un artiste
 * et applique les principes de la programmation orientée objet :
 * - Encapsulation : propriétés immutables
 * - Abstraction : représentation simplifiée d'un artiste
 * - Single Responsibility : représente uniquement un artiste
 * 
 * @property id Identifiant unique de l'artiste dans Deezer
 * @property name Nom de l'artiste
 * @property picture URL de l'image de l'artiste
 * @property pictureSmall URL de la petite image de l'artiste (pour les listes)
 * @property pictureMedium URL de l'image moyenne de l'artiste  
 * @property pictureBig URL de la grande image de l'artiste (pour les détails)
 * @property numberOfAlbums Nombre d'albums de l'artiste
 * @property numberOfFans Nombre de fans de l'artiste
 * @property link Lien vers la page Deezer de l'artiste
 * 

 * @since 1.0
 */
data class Artist(
    val id: Long,
    val name: String,
    val picture: String = "",
    val pictureSmall: String = "",
    val pictureMedium: String = "",
    val pictureBig: String = "",
    val numberOfAlbums: Int = 0,
    val numberOfFans: Int = 0,
    val link: String = ""
) {
    
    /**
     * Retourne l'URL de l'image la plus appropriée selon la taille demandée
     * 
     * Cette méthode applique le principe de responsabilité unique en gérant
     * la logique de sélection d'image à l'intérieur du modèle
     * 
     * @param imageSize Taille de l'image souhaitée
     * @return URL de l'image correspondante ou image par défaut
     */
    fun getImageUrl(imageSize: ImageSize = ImageSize.MEDIUM): String {
        return when (imageSize) {
            ImageSize.SMALL -> pictureSmall.ifEmpty { pictureMedium.ifEmpty { picture } }
            ImageSize.MEDIUM -> pictureMedium.ifEmpty { picture.ifEmpty { pictureSmall } }
            ImageSize.BIG -> pictureBig.ifEmpty { pictureMedium.ifEmpty { picture } }
        }
    }
    

    
    /**
     * Formate le nombre de fans pour l'affichage
     * Exemple : 1000000 -> "1M fans"
     * 
     * @return Chaîne formatée du nombre de fans
     */
    fun getFormattedFansCount(): String {
        return when {
            numberOfFans >= 1_000_000 -> "${numberOfFans / 1_000_000}M fans"
            numberOfFans >= 1_000 -> "${numberOfFans / 1_000}K fans"
            else -> "$numberOfFans fans"
        }
    }
}

/**
 * Énumération des tailles d'images disponibles
 * 
 * Applique le principe d'encapsulation en définissant
 * clairement les tailles d'images supportées
 */
enum class ImageSize {
    SMALL,
    MEDIUM, 
    BIG
} 