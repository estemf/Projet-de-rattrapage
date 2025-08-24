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
)

 