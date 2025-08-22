package org.diiage.projet_rattrapage.domain.usecase

import org.diiage.projet_rattrapage.domain.model.Track
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import timber.log.Timber

/**
 * Use Case pour la recherche de pistes dans l'API Deezer
 * 
 * Cette classe applique les principes SOLID :
 * - Single Responsibility : uniquement la recherche de pistes
 * - Dependency Inversion : dépend de l'abstraction MusicRepository
 * - Interface Segregation : interface spécialisée pour les pistes
 * 
 * Responsabilités :
 * - Validation des paramètres de recherche
 * - Coordination avec le repository
 * - Gestion des erreurs métier
 * - Logging des actions
 * 
 * @property repository Repository pour l'accès aux données
 * 

 * @since 1.0
 */
class SearchTracksUseCase(
    private val repository: MusicRepository
) {
    
    /**
     * Recherche des pistes par terme de recherche
     * 
     * @param query Terme de recherche
     * @param limit Nombre maximum de résultats (défaut: 25)
     * @return Result contenant la liste des pistes ou une erreur
     */
    suspend operator fun invoke(
        query: String,
        limit: Int = 25
    ): Result<List<Track>> {
        
        Timber.d("🔍 Recherche de pistes pour: '$query' (limit: $limit)")
        
        return try {
            // Validation du terme de recherche
            if (!isValidQuery(query)) {
                Timber.w("⚠️ Requête invalide pour la recherche de pistes: '$query'")
                return Result.failure(
                    IllegalArgumentException("Le terme de recherche doit contenir au moins 2 caractères")
                )
            }
            
            // Validation de la limite
            if (limit <= 0 || limit > 100) {
                Timber.w("⚠️ Limite invalide pour la recherche de pistes: $limit")
                return Result.failure(
                    IllegalArgumentException("La limite doit être entre 1 et 100")
                )
            }
            
            // Exécution de la recherche
            val result = repository.searchTracks(query.trim(), limit)
            
            result.fold(
                onSuccess = { tracks ->
                    Timber.i("✅ Recherche de pistes réussie: ${tracks.size} pistes trouvées pour '$query'")
                },
                onFailure = { error ->
                    Timber.w("⚠️ Échec de la recherche de pistes: ${error.message}")
                }
            )
            
            result
            
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la recherche de pistes pour '$query'")
            Result.failure(exception)
        }
    }
    
    /**
     * Valide un terme de recherche pour les pistes
     * 
     * @param query Terme à valider
     * @return true si le terme est valide
     */
    fun isValidQuery(query: String): Boolean {
        val cleanQuery = query.trim()
        return cleanQuery.length >= 2 && cleanQuery.isNotBlank()
    }
    
    /**
     * Retourne les suggestions de recherche pour les pistes
     * 
     * @return Liste de suggestions populaires
     */
    fun getSearchSuggestions(): List<String> {
        return listOf(
            "Love",
            "Dance",
            "Night",
            "Summer",
            "Fire",
            "Dream",
            "Rock",
            "Pop"
        )
    }
} 