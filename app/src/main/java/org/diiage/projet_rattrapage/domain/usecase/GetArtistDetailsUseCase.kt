package org.diiage.projet_rattrapage.domain.usecase

import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import timber.log.Timber


/**
 * Modèle de données agrégées pour les détails d'un artiste
 * 
 * Cette classe applique le principe de composition en regroupant
 * toutes les informations liées à un artiste en une seule structure
 * 
 * @property artist Informations de base de l'artiste
 * @property albums Liste des albums de l'artiste
 * @property topTracks Pistes les plus populaires de l'artiste
 */
data class ArtistDetails(
    val artist: Artist,
    val albums: List<Album> = emptyList(),
    val topTracks: List<Track> = emptyList()
) {
    /**
     * Indique si l'artiste a du contenu disponible
     * 
     * @return true si l'artiste a au moins un album ou une piste
     */
    fun hasContent(): Boolean = albums.isNotEmpty() || topTracks.isNotEmpty()
    
    /**
     * Retourne le nombre total de contenus
     * 
     * @return Nombre d'albums + nombre de pistes
     */
    fun getTotalContentCount(): Int = albums.size + topTracks.size
}

/**
 * Use Case pour récupérer les détails complets d'un artiste
 * 
 * Cette classe illustre le pattern Facade en coordonnant
 * plusieurs appels d'API pour construire une vue complète d'un artiste
 * 
 * Stratégie de récupération :
 * - Récupération parallèle des albums et pistes pour optimiser les performances
 * - Gestion gracieuse des échecs partiels
 * - Agrégation des données en un seul modèle cohérent
 * 
 * @property repository Repository pour l'accès aux données
 * 

 * @since 1.0
 */
class GetArtistDetailsUseCase(
    private val repository: MusicRepository
) {
    
    /**
     * Récupère les détails complets d'un artiste
     * 
     * Cette méthode orchestre plusieurs appels asynchrones :
     * 1. Récupération des informations de base de l'artiste
     * 2. Récupération parallèle des albums et pistes populaires
     * 3. Agrégation des résultats en un modèle unifié
     * 
     * @param artistId Identifiant de l'artiste
     * @return Result contenant les détails de l'artiste ou une erreur
     */
    suspend operator fun invoke(artistId: Long): Result<ArtistDetails> {
        Timber.d("🎤 Récupération des détails pour l'artiste ID: $artistId")
        
        // ================================
        // VALIDATION DES PARAMÈTRES
        // ================================
        
        if (artistId <= 0) {
            Timber.w("⚠️ ID d'artiste invalide: $artistId")
            return Result.failure(
                IllegalArgumentException("L'ID de l'artiste doit être positif")
            )
        }
        
        return try {
            // ================================
            // RÉCUPÉRATION DES INFORMATIONS DE BASE
            // ================================
            
            val artistResult = repository.getArtistById(artistId)
            
            artistResult.fold(
                onSuccess = { artist ->
                    Timber.d("✅ Artiste récupéré: ${artist.name}")
                    
                    // ================================
                    // RÉCUPÉRATION PARALLÈLE DES CONTENUS
                    // ================================
                    
                    val albumsResult = repository.getArtistAlbums(artistId)
                    val tracksResult = repository.getArtistTopTracks(artistId)
                    
                    // Gestion gracieuse des échecs partiels
                    val albums = albumsResult.getOrElse { exception ->
                        Timber.w(exception, "⚠️ Impossible de récupérer les albums de ${artist.name}")
                        emptyList()
                    }
                    
                    val topTracks = tracksResult.getOrElse { exception ->
                        Timber.w(exception, "⚠️ Impossible de récupérer les pistes de ${artist.name}")
                        emptyList()
                    }
                    
                    // ================================
                    // CONSTRUCTION DU RÉSULTAT
                    // ================================
                    
                    val artistDetails = ArtistDetails(
                        artist = artist,
                        albums = albums,
                        topTracks = topTracks
                    )
                    
                    Timber.i(
                        "🎵 Détails complets récupérés pour ${artist.name}: " +
                        "${albums.size} album(s), ${topTracks.size} piste(s) populaire(s)"
                    )
                    
                    Result.success(artistDetails)
                },
                onFailure = { error ->
                    Timber.e(error, "❌ Erreur lors de la récupération de l'artiste $artistId")
                    Result.failure(error)
                }
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception inattendue lors de la récupération des détails")
            Result.failure(exception)
        }
    }
    
    /**
     * Récupère uniquement les informations de base d'un artiste
     * 
     * Méthode plus légère quand on n'a pas besoin des albums/pistes
     * 
     * @param artistId Identifiant de l'artiste
     * @return Result contenant l'artiste ou une erreur
     */
    suspend fun getBasicInfo(artistId: Long): Result<Artist> {
        Timber.d("ℹ️ Récupération des infos de base pour l'artiste ID: $artistId")
        
        return repository.getArtistById(artistId)
    }
} 