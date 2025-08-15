package org.diiage.projet_rattrapage.domain.usecase

import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import timber.log.Timber


/**
 * Use Case pour la recherche d'artistes
 * 
 * Cette classe applique plusieurs design patterns :
 * - Use Case Pattern : encapsule la logique métier spécifique
 * - Command Pattern : représente une action que l'on peut exécuter
 * - Single Responsibility : ne fait qu'une seule chose - rechercher des artistes
 * 
 * Principes SOLID appliqués :
 * - Single Responsibility : gère uniquement la recherche d'artistes
 * - Dependency Inversion : dépend de l'abstraction MusicRepository
 * - Open/Closed : extensible sans modification
 * 
 * @property repository Repository pour l'accès aux données
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
class SearchArtistsUseCase(
    private val repository: MusicRepository
) {
    
    /**
     * Exécute la recherche d'artistes avec validation et logging
     * 
     * Cette méthode :
     * - Valide les paramètres d'entrée
     * - Applique la logique métier (filtrage, transformation)
     * - Gère les cas d'erreur de manière appropriée
     * - Log les opérations pour le suivi
     * 
     * @param query Terme de recherche (minimum 2 caractères)
     * @param limit Nombre maximum de résultats (défaut: 25)
     * @return Result contenant la liste des artistes ou une erreur
     */
    suspend operator fun invoke(query: String, limit: Int = 25): Result<List<Artist>> {
        Timber.d("🔍 Recherche d'artistes pour la requête: '$query'")
        
        // ================================
        // VALIDATION DES PARAMÈTRES D'ENTRÉE
        // ================================
        
        if (query.isBlank()) {
            Timber.w("⚠️ Recherche rejetée : requête vide")
            return Result.failure(
                IllegalArgumentException("La requête de recherche ne peut pas être vide")
            )
        }
        
        if (query.length < 2) {
            Timber.w("⚠️ Recherche rejetée : requête trop courte ($query)")
            return Result.failure(
                IllegalArgumentException("La requête doit contenir au moins 2 caractères")
            )
        }
        
        // ================================
        // PRÉPARATION DE LA REQUÊTE
        // ================================
        
        val cleanedQuery = query.trim()
        Timber.d("🧹 Requête nettoyée: '$cleanedQuery'")
        
        // ================================
        // EXÉCUTION DE LA RECHERCHE
        // ================================
        
        return try {
            val result = repository.searchArtists(cleanedQuery, limit)
            
            result.fold(
                onSuccess = { artists ->
                    Timber.i("✅ Recherche réussie : ${artists.size} artiste(s) trouvé(s)")
                    
                    // Logique métier : filtrer les artistes sans nom ou ID invalide
                    val validArtists = artists.filter { artist ->
                        artist.id > 0 && artist.name.isNotBlank()
                    }
                    
                    if (validArtists.size != artists.size) {
                        Timber.w("🧹 ${artists.size - validArtists.size} artiste(s) invalide(s) filtré(s)")
                    }
                    
                    Result.success(validArtists)
                },
                onFailure = { error ->
                    Timber.e(error, "❌ Erreur lors de la recherche d'artistes pour '$cleanedQuery'")
                    Result.failure(error)
                }
            )
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception inattendue lors de la recherche")
            Result.failure(exception)
        }
    }
    
    /**
     * Vérifie si une requête est valide pour la recherche
     * 
     * Méthode utilitaire pour les validations côté UI
     * 
     * @param query Requête à valider
     * @return true si la requête est valide
     */
    fun isValidQuery(query: String): Boolean {
        return query.isNotBlank() && query.trim().length >= 2
    }
} 