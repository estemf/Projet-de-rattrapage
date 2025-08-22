package org.diiage.projet_rattrapage.domain.usecase

import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import timber.log.Timber

/**
 * Use Case pour la recherche d'albums dans l'API Deezer
 * 
 * Cette classe applique les principes SOLID :
 * - Single Responsibility : uniquement la recherche d'albums
 * - Dependency Inversion : dépend de l'abstraction MusicRepository
 * - Interface Segregation : interface spécialisée pour les albums
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
class SearchAlbumsUseCase(
    private val repository: MusicRepository
) {
    
    /**
     * Recherche des albums par terme de recherche
     * 
     * @param query Terme de recherche
     * @param limit Nombre maximum de résultats (défaut: 25)
     * @return Result contenant la liste des albums ou une erreur
     */
    suspend operator fun invoke(
        query: String,
        limit: Int = 25
    ): Result<List<Album>> {
        
        Timber.d("🔍 Recherche d'albums pour: '$query' (limit: $limit)")
        
        return try {
            // Validation du terme de recherche
            if (!isValidQuery(query)) {
                Timber.w("⚠️ Requête invalide pour la recherche d'albums: '$query'")
                return Result.failure(
                    IllegalArgumentException("Le terme de recherche doit contenir au moins 2 caractères")
                )
            }
            
            // Validation de la limite
            if (limit <= 0 || limit > 100) {
                Timber.w("⚠️ Limite invalide pour la recherche d'albums: $limit")
                return Result.failure(
                    IllegalArgumentException("La limite doit être entre 1 et 100")
                )
            }
            
            // Exécution de la recherche
            val result = repository.searchAlbums(query.trim(), limit)
            
            result.fold(
                onSuccess = { albums ->
                    Timber.i("✅ Recherche d'albums réussie: ${albums.size} albums trouvés pour '$query'")
                },
                onFailure = { error ->
                    Timber.w("⚠️ Échec de la recherche d'albums: ${error.message}")
                }
            )
            
            result
            
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la recherche d'albums pour '$query'")
            Result.failure(exception)
        }
    }
    
    /**
     * Valide un terme de recherche pour les albums
     * 
     * @param query Terme à valider
     * @return true si le terme est valide
     */
    fun isValidQuery(query: String): Boolean {
        val cleanQuery = query.trim()
        return cleanQuery.length >= 2 && cleanQuery.isNotBlank()
    }
    
    /**
     * Retourne les suggestions de recherche pour les albums
     * 
     * @return Liste de suggestions populaires
     */
    fun getSearchSuggestions(): List<String> {
        return listOf(
            "Greatest Hits",
            "Best of",
            "Live",
            "Acoustic",
            "Unplugged",
            "Collection",
            "Essential",
            "Classics"
        )
    }
} 