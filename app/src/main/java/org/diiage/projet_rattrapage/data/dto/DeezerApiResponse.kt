package org.diiage.projet_rattrapage.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO générique pour les réponses de l'API Deezer
 * 
 * Cette classe applique le pattern Generic pour réutiliser
 * la structure de réponse commune à toutes les API Deezer
 * 
 * @param T Type des données contenues dans la réponse
 * @property data Liste des éléments retournés
 * @property total Nombre total d'éléments disponibles
 * @property next URL pour la page suivante (pagination)
 * @property prev URL pour la page précédente (pagination)
 * 

 * @since 1.0
 */
@Serializable
data class DeezerApiResponse<T>(
    @SerialName("data")
    val data: List<T> = emptyList(),
    
    @SerialName("total")
    val total: Int = 0,
    
    @SerialName("next")
    val next: String? = null,
    
    @SerialName("prev")
    val prev: String? = null
) {
    /**
     * Indique si des résultats sont disponibles
     */
    fun hasResults(): Boolean = data.isNotEmpty()
    
    /**
     * Indique si une page suivante est disponible
     */
    fun hasNextPage(): Boolean = next != null
    
    /**
     * Indique si une page précédente est disponible  
     */
    fun hasPreviousPage(): Boolean = prev != null
}

/**
 * DTO pour les réponses d'erreur de l'API Deezer
 * 
 * @property error Informations sur l'erreur
 */
@Serializable
data class DeezerErrorResponse(
    @SerialName("error")
    val error: DeezerError
)

/**
 * DTO pour les détails d'une erreur Deezer
 * 
 * @property type Type d'erreur
 * @property message Message d'erreur
 * @property code Code d'erreur numérique
 */
@Serializable
data class DeezerError(
    @SerialName("type")
    val type: String = "",
    
    @SerialName("message") 
    val message: String = "",
    
    @SerialName("code")
    val code: Int = 0
) 